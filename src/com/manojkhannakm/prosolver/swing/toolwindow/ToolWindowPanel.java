package com.manojkhannakm.prosolver.swing.toolwindow;

import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBScrollPane;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.file.*;
import com.manojkhannakm.prosolver.model.Contest;
import com.manojkhannakm.prosolver.model.Context;
import com.manojkhannakm.prosolver.model.Platform;
import com.manojkhannakm.prosolver.model.Problem;
import com.manojkhannakm.prosolver.swing.tree.Tree;
import com.manojkhannakm.prosolver.swing.tree.TreeModel;
import com.manojkhannakm.prosolver.swing.tree.TreeNode;
import com.manojkhannakm.prosolver.util.SwingUtils;
import org.intellij.lang.regexp.RegExpFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Manoj Khanna
 */

public class ToolWindowPanel extends JPanel {

    private final Context context;

    private final JPanel filterPanel;
    private final EditorTextField filterTextField;
    private final Tree fileTree;

    private RefreshWorker refreshWorker;

    public ToolWindowPanel(Context context) {
        super(new BorderLayout());

        this.context = context;

        filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        filterPanel.setVisible(false);
        add(filterPanel, BorderLayout.NORTH);

        filterPanel.add(new JLabel("Filter"));

        filterPanel.add(Box.createHorizontalStrut(10));

        Project project = context.getProject();
        filterTextField = new EditorTextField("", project, RegExpFileType.INSTANCE);
        //background
        filterTextField.addDocumentListener(new DocumentAdapter() {

            @Override
            public void documentChanged(DocumentEvent e) {
                fileTree.getModel().reload();

                fileTree.expandTree();
            }

        });
        filterPanel.add(filterTextField);

        fileTree = new Tree(new TreeModel(null));
        fileTree.setCellRenderer(new ColoredTreeCellRenderer() {

            @Override
            public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected,
                                              boolean expanded, boolean leaf, int row, boolean hasFocus) {
                FileNode fileNode = (FileNode) value;
                JsonFile file = fileNode.getFile();
                Icon icon;
                if (file instanceof PlatformFile) {
                    icon = Constants.Icons.Files.PLATFORM;
                } else if (file instanceof ContestFile) {
                    icon = Constants.Icons.Files.CONTEST;
                } else {
                    icon = Constants.Icons.Files.PROBLEM;
                }

                setIcon(icon);

                String name = fileNode.getName();
                if (name != null) {
                    append(name);
                }

                setToolTipText(fileNode.getUrl());
            }

        });
        fileTree.setRootVisible(false);

//        DefaultActionGroup actionGroup = new DefaultActionGroup();
//        actionGroup.add(new Action(context, null, "Go To Class") {
//
//            @Override
//            public void actionPerformed(AnActionEvent e) {
//
//            }
//
//        });
//        actionGroup.add(new Action(context, null, "Go To Url") {
//
//            @Override
//            public void actionPerformed(AnActionEvent e) {
//
//            }
//
//        });
//
//        JPopupMenu popupMenu = ActionManager.getInstance()
//                .createActionPopupMenu(ActionPlaces.UNKNOWN, actionGroup).getComponent();
//
//        fileTree.addMouseListener(new MouseAdapter() {
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                if (e.isPopupTrigger() && fileTree.getSelectionCount() == 1
//                        && ((FileNode) fileTree.getLastSelectedPathComponent()).getFile() instanceof ProblemFile) {
//                    popupMenu.show(fileTree, e.getX(), e.getY());
//                }
//            }
//
//        });

        JBScrollPane fileScrollPane = new JBScrollPane(fileTree);
        fileScrollPane.setBorder(null);
        add(fileScrollPane, BorderLayout.CENTER);

        refresh(true);

        ProjectManager.getInstance().addProjectManagerListener(project, new ProjectManagerListener() {

            @Override
            public void projectClosing(Project project) {
                refresh(false);
            }

        });
    }

    public void add(ContentFile contentFile) {
        TreeModel fileModel = fileTree.getModel();

        FileNode contentNode = (FileNode) fileModel.getRoot();
        if (contentNode == null) {
            contentNode = new FileNode(contentFile);

            fileModel.setRoot(contentNode);
        }

        for (ProblemFile problemFile : contentFile.getProblemFileList()) {
            contentNode.add(new FileNode(problemFile));

            problemFile.set(null);
        }

        for (PlatformFile platformFile : contentFile.getPlatformFileList()) {
            FileNode platformNode = (FileNode) contentNode.getChildWith(platformFile);
            if (platformNode == null) {
                platformNode = new FileNode(platformFile);

                contentNode.insert(platformNode, contentNode.getBranchCount());
            }

            for (ProblemFile problemFile : platformFile.getProblemFileList()) {
                platformNode.add(new FileNode(problemFile));

                problemFile.set(null);
            }

            for (ContestFile contestFile : platformFile.getContestFileList()) {
                FileNode contestNode = (FileNode) platformNode.getChildWith(contestFile);
                if (contestNode == null) {
                    contestNode = new FileNode(contestFile);

                    platformNode.insert(contestNode, platformNode.getBranchCount());
                }

                for (ProblemFile problemFile : contestFile.getProblemFileList()) {
                    contestNode.add(new FileNode(problemFile));

                    problemFile.set(null);
                }

                contestFile.set(null);
                contestFile.getProblemFileList().clear();
            }

            platformFile.set(null);
            platformFile.getProblemFileList().clear();
            platformFile.getContestFileList().clear();
        }

        contentFile.set(null);
        contentFile.getProblemFileList().clear();
        contentFile.getPlatformFileList().clear();

        fileModel.reload();

        fileTree.expandTree();
    }

//    public void add(ContentFile getContentFile) {
//        TreeModel fileModel = fileTree.getModel();
//
//        FileNode contentNode = (FileNode) fileModel.getRoot();
//        if (contentNode == null) {
//            contentNode = new FileNode(getContentFile);
//
//            fileModel.setRoot(contentNode);
//        }
//
//
//    }

    public void add(ContentFile contentFile,
                    ArrayList<ProblemFile> problemFileList) {

    }

    public void add(ContentFile contentFile,
                    PlatformFile platformFile,
                    ArrayList<ProblemFile> problemFileList) {

    }

    public void add(ContentFile contentFile,
                    PlatformFile platformFile,
                    ContestFile contestFile,
                    ArrayList<ProblemFile> problemFileList) {

    }

    public void refresh(boolean refresh) {
        if (refresh) {
            if (refreshWorker != null && !refreshWorker.isDone()) {
                return;
            }

            fileTree.setPaintBusy(true);

            refreshWorker = new RefreshWorker();
            refreshWorker.execute();
        } else {
            if (refreshWorker == null || refreshWorker.isDone()) {
                return;
            }

            refreshWorker.cancel(true);

            fileTree.setPaintBusy(false);
        }
    }

    public void sort(boolean sort) {
        TreeModel fileModel = fileTree.getModel();
        if (sort) {
            fileModel.setTreeSorter((treeNode1, treeNode2) -> {
                FileNode fileNode1 = (FileNode) treeNode1,
                        fileNode2 = (FileNode) treeNode2;
                if (fileNode1.getFile().getClass().equals(fileNode2.getFile().getClass())) {
                    return fileNode1.getName().compareTo(fileNode2.getName());
                }

                return fileNode2.getFile() instanceof ProblemFile ? -1 : 1;
            });
        } else {
            fileModel.setTreeSorter(null);
        }

        fileModel.reload();

        fileTree.expandTree();
    }

    public void filter(boolean filter) {
        filterPanel.setVisible(filter);

        filterTextField.requestFocus();

        repaint();

        TreeModel fileModel = fileTree.getModel();
        if (filter) {
            fileModel.setTreeFilter(treeNode -> {
                String name = ((FileNode) treeNode).getName();
                if (name == null) {
                    return false;
                }

                try {
                    return Pattern.compile(filterTextField.getText()).matcher(name).find();
                } catch (PatternSyntaxException ignored) {
                }

                return true;
            });
        } else {
            fileModel.setTreeFilter(null);
        }

        fileModel.reload();

        fileTree.expandTree();
    }

    public void expandAll() {
        fileTree.expandTree();
    }

    public void collapseAll() {
        Enumeration enumeration = ((FileNode) fileTree.getModel().getRoot()).children();
        while (enumeration.hasMoreElements()) {
            fileTree.collapseNode(((FileNode) enumeration.nextElement()));
        }
    }

    private class FileNode extends TreeNode {

        private final JsonFile file;

        private String name, url;

        public FileNode(JsonFile file) {
            this.file = file;

            if (file instanceof PlatformFile) {
                Platform platform = ((PlatformFile) file).get();
                name = platform.getName();
                url = platform.getUrl();
            } else if (file instanceof ContestFile) {
                Contest contest = ((ContestFile) file).get();
                name = contest.getName();
                url = contest.getUrl();
            } else if (file instanceof ProblemFile) {
                Problem problem = ((ProblemFile) file).get();
                name = problem.getName();
                url = problem.getUrl();
            }

            setUserObject(file);
        }

        public JsonFile getFile() {
            return file;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

    }

    private class RefreshWorker extends SwingWorker<Boolean, Object> {

        private ContentFile contentFile;

        @Override
        protected Boolean doInBackground() throws Exception {
            contentFile = context.getContentFile();
            if (!contentFile.read()) {
                return false;
            }

            for (ProblemFile problemFile : contentFile.getProblemFileList()) {
                if (!problemFile.read()) {
                    return false;
                }
            }

            for (PlatformFile platformFile : contentFile.getPlatformFileList()) {
                if (!platformFile.read()) {
                    return false;
                }

                for (ProblemFile problemFile : platformFile.getProblemFileList()) {
                    if (!problemFile.read()) {
                        return false;
                    }
                }

                for (ContestFile contestFile : platformFile.getContestFileList()) {
                    if (!contestFile.read()) {
                        return false;
                    }

                    for (ProblemFile problemFile : contestFile.getProblemFileList()) {
                        if (!problemFile.read()) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        @Override
        protected void done() {
            super.done();

            if (isCancelled()) {
                return;
            }

            try {
                if (get()) {
                    fileTree.getModel().setRoot(null);

                    add(contentFile);
                } else {
                    SwingUtils.showError(context.getProject(), "Loading problems failed!");
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();

                SwingUtils.showError(context.getProject(), "Loading problems failed!");
            }

            fileTree.setPaintBusy(false);
        }

    }

}
