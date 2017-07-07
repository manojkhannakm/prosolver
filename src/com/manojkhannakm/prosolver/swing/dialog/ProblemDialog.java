package com.manojkhannakm.prosolver.swing.dialog;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.action.Action;
import com.manojkhannakm.prosolver.model.*;
import com.manojkhannakm.prosolver.swing.action.ActionButton;
import com.manojkhannakm.prosolver.swing.tab.Tab;
import com.manojkhannakm.prosolver.swing.tab.TabPane;
import com.manojkhannakm.prosolver.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * @author Manoj Khanna
 */

public class ProblemDialog extends Dialog {

    protected Content content;
    protected Platform platform;
    protected Contest contest;
    protected ArrayList<Problem> problemList;

    private JTextField contestCodeTextField, contestNameTextField;
    private TabPane<ProblemTab> problemTabPane;

    private Timer repaintTimer;

    protected ProblemDialog(Context context) {
        super(context);
    }

    protected ProblemDialog(Context context, Content content) {
        super(context);

        this.content = content;

        ArrayList<Platform> platformList = content.getPlatformList();
        if (platformList == null) {
            problemList = content.getProblemList();
        } else {
            platform = platformList.get(0);

            ArrayList<Contest> contestList = platform.getContestList();
            if (contestList == null) {
                problemList = platform.getProblemList();
            } else {
                contest = contestList.get(0);

                problemList = contest.getProblemList();
            }
        }
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayoutManager(contest == null ? 2 : 3, 4));
        if (contest != null) {
            GridConstraints constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(0);
            constraints.setAnchor(GridConstraints.ANCHOR_WEST);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            panel.add(new JLabel("Contest Code"), constraints);

            contestCodeTextField = new JTextField(1);
            contestCodeTextField.setEnabled(false);
            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(1);
            constraints.setFill(GridConstraints.FILL_HORIZONTAL);
            panel.add(contestCodeTextField, constraints);

            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(2);
            constraints.setAnchor(GridConstraints.ANCHOR_WEST);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            panel.add(new JLabel("Contest Name"), constraints);

            contestNameTextField = new JTextField(1);
            contestNameTextField.getDocument().addDocumentListener(new DocumentAdapter() {

                @Override
                protected void textChanged(DocumentEvent e) {
                    String contestName = contestNameTextField.getText().trim();
                    if (contestName.isEmpty()) {
                        contestName = null;
                    }

                    contest.setName(contestName);

                    for (ProblemTab problemTab : problemTabPane.getAllTabs()) {
                        problemTab.update();
                    }
                }

            });
            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(3);
            constraints.setFill(GridConstraints.FILL_HORIZONTAL);
            panel.add(contestNameTextField, constraints);
        }

        GridConstraints constraints = new GridConstraints();
        constraints.setRow(contest == null ? 0 : 1);
        constraints.setColumn(0);
        constraints.setAnchor(GridConstraints.ANCHOR_WEST);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        panel.add(new JLabel("Problems"), constraints);

        JPanel problemActionPanel = new JPanel(new GridLayoutManager(1, 3));
        constraints = new GridConstraints();
        constraints.setRow(contest == null ? 0 : 1);
        constraints.setColumn(1);
        constraints.setColSpan(3);
        constraints.setAnchor(GridConstraints.ANCHOR_EAST);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        panel.add(problemActionPanel, constraints);

        ActionButton addProblemButton = new ActionButton(new Action(context,
                Constants.Icons.Actions.ADD, "Add Problem") {

            @Override
            public void actionPerformed(AnActionEvent e) {
                addProblem(null, true);
            }

        });

        if (platform != null || problemList != null && isProblemParsed(problemList.get(0))) {
            addProblemButton.setEnabled(false);
        }

        constraints = new GridConstraints();
        constraints.setRow(0);
        constraints.setColumn(0);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        problemActionPanel.add(addProblemButton, constraints);

        ActionButton removeProblemButton = new ActionButton(new Action(context,
                Constants.Icons.Actions.REMOVE, "Remove Problem") {

            @Override
            public void actionPerformed(AnActionEvent e) {
                removeProblem();
            }

        });

        if (problemList != null && isProblemParsed(problemList.get(0))) {
            removeProblemButton.setEnabled(false);
        }

        constraints = new GridConstraints();
        constraints.setRow(0);
        constraints.setColumn(1);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        problemActionPanel.add(removeProblemButton, constraints);

        ActionButton refreshProblemButton = new ActionButton(new Action(context,
                Constants.Icons.Actions.REFRESH, "Refresh Problem") {

            @Override
            public void actionPerformed(AnActionEvent e) {
                refreshProblem();
            }

        });

        if (platform == null) {
            refreshProblemButton.setEnabled(false);
        }

        constraints = new GridConstraints();
        constraints.setRow(0);
        constraints.setColumn(2);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        problemActionPanel.add(refreshProblemButton, constraints);

        problemTabPane = new TabPane<>(context.getProject());
        constraints = new GridConstraints();
        constraints.setRow(contest == null ? 1 : 2);
        constraints.setColumn(0);
        constraints.setColSpan(4);
        constraints.setFill(GridConstraints.FILL_BOTH);
        panel.add(problemTabPane, constraints);

        repaintTimer = new Timer(100, e -> getRootPane().getGlassPane().repaint());
        repaintTimer.start();

        if (content == null) {
            addProblem(null, false);
        } else {
            if (contest != null) {
                contestCodeTextField.setText(contest.getCode());
                contestNameTextField.setText(contest.getName());
            }

            for (Problem problem : problemList) {
                addProblem(problem, false);
            }
        }

        return panel;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        if (contestNameTextField != null) {
            return contestNameTextField;
        }

        return problemTabPane.getSelectedTab().problemNameTextField;
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (contestNameTextField != null && contestNameTextField.getText().isEmpty()) {
            return new ValidationInfo("Invalid contest name!", contestNameTextField);
        }

        ProblemTab selectedTab = problemTabPane.getSelectedTab();
        ValidationInfo validationInfo = selectedTab.validate();
        if (validationInfo != null) {
            return validationInfo;
        }

        ArrayList<ProblemTab> problemTabList = problemTabPane.getAllTabs();
        problemTabList.remove(selectedTab);

        for (ProblemTab problemTab : problemTabList) {
            validationInfo = problemTab.validate();
            if (validationInfo != null) {
                return new ValidationInfo(validationInfo.message,
                        problemTabPane.getTabLabel(problemTab.getTabInfo()));
            }
        }

        return null;
    }

    @Override
    protected void doOKAction() {
        if (!getOKAction().isEnabled()) {
            return;
        }

        for (ProblemTab problemTab : problemTabPane.getAllTabs()) {
            problemTab.refresh(false);
        }

        problemList = new ArrayList<>();
        for (int i = 0; i < problemTabPane.getTabCount(); i++) {
            problemList.add(problemTabPane.getTab(i).getProblem());
        }

        if (content == null) {
            content = new Content();
            content.setProblemList(problemList);
        } else {
            if (platform == null) {
                content.setProblemList(problemList);
            } else {
                if (contest == null) {
                    platform.setProblemList(problemList);
                } else {
                    contest.setProblemList(problemList);
                }
            }
        }
    }

    @Override
    public void doCancelAction() {
        if (!getCancelAction().isEnabled()) {
            return;
        }

        for (ProblemTab problemTab : problemTabPane.getAllTabs()) {
            problemTab.refresh(false);
        }

        close(DialogWrapper.CANCEL_EXIT_CODE);
    }

    @Override
    protected void dispose() {
        repaintTimer.stop();

        super.dispose();
    }

    private void addProblem(Problem problem, boolean select) {
        if (problem == null) {
            problem = new Problem();
            problem.setUrl("");
            problem.setCode("");
        }

        ProblemTab problemTab = new ProblemTab(problem);
        problemTabPane.addTab(problemTab);

        if (select) {
            problemTabPane.setSelectedTab(problemTab);
        }
    }

    private void removeProblem() {
        if (problemTabPane.getTabCount() == 1) {
            close(DialogWrapper.CLOSE_EXIT_CODE);
            return;
        }

        problemTabPane.removeTab(problemTabPane.getSelectedTab());

        for (int i = 0; i < problemTabPane.getTabCount(); i++) {
            problemTabPane.getTab(i).setText("Problem " + (i + 1));
        }
    }

    private void refreshProblem() {
        ProblemTab problemTab = problemTabPane.getSelectedTab();
        problemTab.refresh(false);
        problemTab.refresh(true);
    }

    private boolean isProblemLocal(Problem problem) {
        return problem.getUrl().isEmpty();
    }

    private boolean isProblemParsed(Problem problem) {
        return problem.getPackageName() != null;
    }

    private class ProblemTab extends Tab {

        private final Problem problem;

        private final JTextField problemCodeTextField, problemNameTextField,
                packageNameTextField, classNameTextField;
        private final JSpinner sizeLimitSpinner, timeLimitSpinner, memoryLimitSpinner;
        private final TabPane<TestTab> testTabPane;

        private RefreshWorker refreshWorker;

        private ProblemTab(Problem problem) {
            this.problem = problem;

            setText("Problem " + (problemTabPane.getTabCount() + 1));

            JPanel panel = new JPanel(new GridLayoutManager(5, 4));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setComponent(panel);

            GridConstraints constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(0);
            constraints.setAnchor(GridConstraints.ANCHOR_WEST);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            panel.add(new JLabel("Problem Code"), constraints);

            problemCodeTextField = new JTextField(1);
            problemCodeTextField.setEnabled(false);
            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(1);
            constraints.setFill(GridConstraints.FILL_HORIZONTAL);
            panel.add(problemCodeTextField, constraints);

            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(2);
            constraints.setAnchor(GridConstraints.ANCHOR_WEST);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            panel.add(new JLabel("Problem Name"), constraints);

            problemNameTextField = new JTextField(1);
            problemNameTextField.getDocument().addDocumentListener(new DocumentAdapter() {

                @Override
                protected void textChanged(DocumentEvent e) {
                    String problemName = problemNameTextField.getText().trim();
                    if (problemName.isEmpty()) {
                        problemName = null;
                    }

                    problem.setName(problemName);

                    update();
                }

            });
            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(3);
            constraints.setFill(GridConstraints.FILL_HORIZONTAL);
            panel.add(problemNameTextField, constraints);

            constraints = new GridConstraints();
            constraints.setRow(1);
            constraints.setColumn(0);
            constraints.setAnchor(GridConstraints.ANCHOR_WEST);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            panel.add(new JLabel("Package Name"), constraints);

            packageNameTextField = new JTextField(1);
            constraints = new GridConstraints();
            constraints.setRow(1);
            constraints.setColumn(1);
            constraints.setFill(GridConstraints.FILL_HORIZONTAL);
            panel.add(packageNameTextField, constraints);

            constraints = new GridConstraints();
            constraints.setRow(1);
            constraints.setColumn(2);
            constraints.setAnchor(GridConstraints.ANCHOR_WEST);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            panel.add(new JLabel("Class Name"), constraints);

            classNameTextField = new JTextField(1);
            constraints = new GridConstraints();
            constraints.setRow(1);
            constraints.setColumn(3);
            constraints.setFill(GridConstraints.FILL_HORIZONTAL);
            panel.add(classNameTextField, constraints);

            constraints = new GridConstraints();
            constraints.setRow(2);
            constraints.setColumn(0);
            constraints.setAnchor(GridConstraints.ANCHOR_WEST);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            panel.add(new JLabel("Limits"), constraints);

            JPanel limitPanel = new JPanel(new GridLayoutManager(1, 9));
            constraints = new GridConstraints();
            constraints.setRow(2);
            constraints.setColumn(1);
            constraints.setColSpan(3);
            constraints.setFill(GridConstraints.FILL_HORIZONTAL);
            panel.add(limitPanel, constraints);

            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(0);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            limitPanel.add(new JLabel("Size"), constraints);

            sizeLimitSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
            JSpinner.NumberEditor editor = new JSpinner.NumberEditor(sizeLimitSpinner, "#");
            editor.getTextField().setColumns(1);
            sizeLimitSpinner.setEditor(editor);
            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(1);
            constraints.setFill(GridConstraints.FILL_HORIZONTAL);
            limitPanel.add(sizeLimitSpinner, constraints);

            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(2);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            limitPanel.add(new JLabel("kb"), constraints);

            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(3);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            limitPanel.add(new JLabel("Time"), constraints);

            timeLimitSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 100));
            editor = new JSpinner.NumberEditor(timeLimitSpinner, "#");
            editor.getTextField().setColumns(1);
            timeLimitSpinner.setEditor(editor);
            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(4);
            constraints.setFill(GridConstraints.FILL_HORIZONTAL);
            limitPanel.add(timeLimitSpinner, constraints);

            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(5);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            limitPanel.add(new JLabel("ms"), constraints);

            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(6);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            limitPanel.add(new JLabel("Memory"), constraints);

            memoryLimitSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
            editor = new JSpinner.NumberEditor(memoryLimitSpinner, "#");
            editor.getTextField().setColumns(1);
            memoryLimitSpinner.setEditor(editor);
            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(7);
            constraints.setFill(GridConstraints.FILL_HORIZONTAL);
            limitPanel.add(memoryLimitSpinner, constraints);

            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(8);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            limitPanel.add(new JLabel("mb"), constraints);

            constraints = new GridConstraints();
            constraints.setRow(3);
            constraints.setColumn(0);
            constraints.setAnchor(GridConstraints.ANCHOR_WEST);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            panel.add(new JLabel("Tests"), constraints);

            JPanel testActionPanel = new JPanel(new GridLayoutManager(1, 3));
            constraints = new GridConstraints();
            constraints.setRow(3);
            constraints.setColumn(1);
            constraints.setColSpan(3);
            constraints.setAnchor(GridConstraints.ANCHOR_EAST);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            panel.add(testActionPanel, constraints);

            DefaultActionGroup addTestActionGroup = new DefaultActionGroup("Add Test", true);
            addTestActionGroup.getTemplatePresentation().setIcon(Constants.Icons.Actions.ADD);
            addTestActionGroup.add(new Action(context, Constants.Icons.Actions.ADD, "Add Test") {

                @Override
                public void actionPerformed(AnActionEvent e) {
                    addTest(null, true);
                }

            });
            addTestActionGroup.add(new Action(context, Constants.Icons.Actions.ADD, "Add Test Files") {

                @Override
                public void actionPerformed(AnActionEvent e) {
                    VirtualFile[] inputFiles = FileChooser.chooseFiles(
                            new FileChooserDescriptor(true, false, false, false, false, true)
                                    .withTitle("Select Input Files"), context.getProject(), null);
                    if (inputFiles.length == 0) {
                        return;
                    }

                    VirtualFile[] outputFiles = FileChooser.chooseFiles(
                            new FileChooserDescriptor(true, false, false, false, false, true)
                                    .withTitle("Select Output Files"), context.getProject(), null);
                    if (inputFiles.length != outputFiles.length) {
                        showError("Invalid test files!");
                        return;
                    }

                    for (int i = 0; i < inputFiles.length; i++) {
                        try {
                            Test test = new Test();
                            test.setEnabled(true);
                            test.setInput(new String(inputFiles[i].contentsToByteArray(false)));
                            test.setOutput(new String(outputFiles[i].contentsToByteArray(false)));

                            addTest(test, true);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

            });

            ActionButton addTestButton = new ActionButton(addTestActionGroup);
            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(0);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            testActionPanel.add(addTestButton, constraints);

            ActionButton removeTestButton = new ActionButton(new Action(context,
                    Constants.Icons.Actions.REMOVE, "Remove Test") {

                @Override
                public void actionPerformed(AnActionEvent e) {
                    removeTest();
                }

            });
            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(1);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            testActionPanel.add(removeTestButton, constraints);

            ActionButton toggleTestButton = new ActionButton(new Action(context,
                    Constants.Icons.Actions.TOGGLE, "Toggle Test") {

                @Override
                public void actionPerformed(AnActionEvent e) {
                    toggleTest();
                }

            });
            constraints = new GridConstraints();
            constraints.setRow(0);
            constraints.setColumn(2);
            constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
            testActionPanel.add(toggleTestButton, constraints);

            testTabPane = new TabPane<>(context.getProject());
            constraints = new GridConstraints();
            constraints.setRow(4);
            constraints.setColumn(0);
            constraints.setColSpan(4);
            constraints.setFill(GridConstraints.FILL_BOTH);
            panel.add(testTabPane, constraints);

            if (!isProblemParsed(problem)) {
                if (isProblemLocal(problem)) {
                    sizeLimitSpinner.setValue(1024);
                    timeLimitSpinner.setValue(1000);
                    memoryLimitSpinner.setValue(1024);

                    addTest(null, false);
                } else {
                    problemCodeTextField.setText(problem.getCode());
                    problemNameTextField.setText(problem.getName());

                    addTest(null, false);

                    SwingUtilities.invokeLater(() -> refresh(true));
                }
            } else {
                problemCodeTextField.setText(problem.getCode());
                problemNameTextField.setText(problem.getName());
                packageNameTextField.setText(problem.getPackageName());
                classNameTextField.setText(problem.getClassName());
                sizeLimitSpinner.setValue(problem.getSizeLimit());
                timeLimitSpinner.setValue(problem.getTimeLimit());
                memoryLimitSpinner.setValue(problem.getMemoryLimit());

                for (Test test : problem.getTestList()) {
                    addTest(test, false);
                }
            }
        }

        private void addTest(Test test, boolean select) {
            if (test == null) {
                test = new Test();
                test.setEnabled(true);
            }

            TestTab testTab = new TestTab(test);
            testTabPane.addTab(testTab);

            if (select) {
                testTabPane.setSelectedTab(testTab);
            }
        }

        private void removeTest() {
            if (testTabPane.getTabCount() == 1) {
                return;
            }

            testTabPane.removeTab(testTabPane.getSelectedTab());

            for (int i = 0; i < testTabPane.getTabCount(); i++) {
                testTabPane.getTab(i).setText("Test " + (i + 1));
            }
        }

        private void toggleTest() {
            TestTab testTab = testTabPane.getSelectedTab();

            Test test = testTab.test;
            test.setEnabled(!test.isEnabled());

            if (test.isEnabled()) {
                testTab.setStyle(SimpleTextAttributes.STYLE_PLAIN);
            } else {
                testTab.setStyle(SimpleTextAttributes.STYLE_STRIKEOUT);
            }
        }

        private void update() {
            Setting setting = context.getSetting();
            String packageName = setting.getPackageName(platform, contest, problem);
            if (packageName == null) {
                if (platform != null) {
                    packageName = platform.getPackageName(contest, problem);
                }
            }

            packageNameTextField.setText(packageName);

            String className = setting.getClassName(platform, contest, problem);
            if (className == null) {
                if (platform != null) {
                    className = platform.getClassName(contest, problem);
                } else {
                    className = StringUtils.getPascalCase(problemNameTextField.getText().trim());
                    if (!StringUtils.isClassNameValid(className)) {
                        className = null;
                    }
                }
            }

            classNameTextField.setText(className);
        }

        private void refresh(boolean refresh) {
            if (refresh) {
                if (refreshWorker != null && !refreshWorker.isDone()) {
                    return;
                }

                setBusy(true);

                refreshWorker = new RefreshWorker();
                refreshWorker.execute();
            } else {
                if (refreshWorker == null || refreshWorker.isDone()) {
                    return;
                }

                refreshWorker.cancel(true);

                setBusy(false);
            }
        }

        private ValidationInfo validate() {
            if (problemNameTextField.getText().isEmpty()) {
                return new ValidationInfo("Invalid problem name!", problemNameTextField);
            }

            String packageName = packageNameTextField.getText();
            if (!packageName.isEmpty() && !StringUtils.isPackageNameValid(packageName)) {
                return new ValidationInfo("Invalid package name!", packageNameTextField);
            }

            String className = classNameTextField.getText();
            if (className.isEmpty() || !StringUtils.isClassNameValid(className)) {
                return new ValidationInfo("Invalid class name!", classNameTextField);
            }

            if (new File(context.getSetting().getSourceDirectoryFile(), className + ".java").exists()) {
                return new ValidationInfo("Class exist already!", classNameTextField);
            }

            return null;
        }

        private Problem getProblem() {
            problem.setName(problemNameTextField.getText());
            problem.setPackageName(packageNameTextField.getText());
            problem.setClassName(classNameTextField.getText());
            problem.setSizeLimit((Integer) sizeLimitSpinner.getValue());
            problem.setTimeLimit((Integer) timeLimitSpinner.getValue());
            problem.setMemoryLimit((Integer) memoryLimitSpinner.getValue());

            ArrayList<Test> testList = new ArrayList<>();
            for (int i = 0; i < testTabPane.getTabCount(); i++) {
                testList.add(testTabPane.getTab(i).getTest());
            }

            problem.setTestList(testList);

            return problem;
        }

        private class TestTab extends Tab {

            private final Test test;

            private final EditorTextField inputTextField, outputTextField;

            private TestTab(Test test) {
                this.test = test;

                setText("Test " + (testTabPane.getTabCount() + 1));

                if (test.isEnabled()) {
                    setStyle(SimpleTextAttributes.STYLE_PLAIN);
                } else {
                    setStyle(SimpleTextAttributes.STYLE_STRIKEOUT);
                }

                JPanel panel = new JPanel(new GridLayoutManager(2, 2));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                setComponent(panel);

                GridConstraints constraints = new GridConstraints();
                constraints.setRow(0);
                constraints.setColumn(0);
                constraints.setAnchor(GridConstraints.ANCHOR_WEST);
                panel.add(new JLabel("Input"), constraints);

                constraints = new GridConstraints();
                constraints.setRow(0);
                constraints.setColumn(1);
                constraints.setAnchor(GridConstraints.ANCHOR_WEST);
                panel.add(new JLabel("Output"), constraints);

                inputTextField = new EditorTextField();
                inputTextField.setPreferredSize(new Dimension(239, 100));
                inputTextField.setFont(new Font("Consolas", Font.PLAIN, 12));
                inputTextField.setOneLineMode(false);
                inputTextField.addSettingsProvider(editor -> {
                    editor.setHorizontalScrollbarVisible(true);
                    editor.setVerticalScrollbarVisible(true);

                    EditorSettings settings = editor.getSettings();
                    settings.setLineCursorWidth(2);
                    settings.setLineNumbersShown(true);
                    settings.setLineMarkerAreaShown(true);
                    settings.setGutterIconsShown(false);
                });
                constraints = new GridConstraints();
                constraints.setRow(1);
                constraints.setColumn(0);
                constraints.setFill(GridConstraints.FILL_BOTH);
                panel.add(inputTextField, constraints);

                outputTextField = new EditorTextField();
                outputTextField.setPreferredSize(new Dimension(239, 100));
                outputTextField.setFont(new Font("Consolas", Font.PLAIN, 12));
                outputTextField.setOneLineMode(false);
                outputTextField.addSettingsProvider(editor -> {
                    editor.setHorizontalScrollbarVisible(true);
                    editor.setVerticalScrollbarVisible(true);

                    EditorSettings settings = editor.getSettings();
                    settings.setLineCursorWidth(2);
                    settings.setLineNumbersShown(true);
                    settings.setLineMarkerAreaShown(true);
                    settings.setGutterIconsShown(false);
                });
                constraints = new GridConstraints();
                constraints.setRow(1);
                constraints.setColumn(1);
                constraints.setFill(GridConstraints.FILL_BOTH);
                panel.add(outputTextField, constraints);

                inputTextField.setText(test.getInput());
                outputTextField.setText(test.getOutput());
            }

            private Test getTest() {
                test.setInput(inputTextField.getText());
                test.setOutput(outputTextField.getText());

                return test;
            }

        }

        private class RefreshWorker extends SwingWorker<Boolean, Object> {

            @Override
            protected Boolean doInBackground() throws Exception {
                return platform.getParser().parseProblem(problem);
            }

            @Override
            protected void done() {
                super.done();

                if (isCancelled()) {
                    return;
                }

                try {
                    if (get()) {
                        problemCodeTextField.setText(problem.getCode());
                        problemNameTextField.setText(problem.getName());
                        sizeLimitSpinner.setValue(problem.getSizeLimit());
                        timeLimitSpinner.setValue(problem.getTimeLimit());
                        memoryLimitSpinner.setValue(problem.getMemoryLimit());

                        testTabPane.removeAllTabs();

                        for (Test test : problem.getTestList()) {
                            addTest(test, false);
                        }
                    } else {
                        showError("Parsing problem failed!");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();

                    showError("Parsing problem failed!");
                }

                setBusy(false);
            }

        }

    }

}
