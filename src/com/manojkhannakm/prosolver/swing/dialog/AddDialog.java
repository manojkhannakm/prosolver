package com.manojkhannakm.prosolver.swing.dialog;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.refactoring.rename.RenameProcessor;
import com.intellij.usageView.UsageInfo;
import com.manojkhannakm.prosolver.file.ContentFile;
import com.manojkhannakm.prosolver.file.ContestFile;
import com.manojkhannakm.prosolver.file.PlatformFile;
import com.manojkhannakm.prosolver.file.ProblemFile;
import com.manojkhannakm.prosolver.model.Content;
import com.manojkhannakm.prosolver.model.Context;
import com.manojkhannakm.prosolver.model.Problem;
import com.manojkhannakm.prosolver.model.Setting;
import com.manojkhannakm.prosolver.util.SwingUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Manoj Khanna
 */

public class AddDialog extends ProblemDialog {

    public AddDialog(Context context) {
        super(context);

        init();
    }

    public AddDialog(Context context, Content content) {
        super(context, content);

        init();
    }

    @Override
    protected void init() {
        setTitle("Add");
        setResizable(false);
        setOKButtonText("Add");

        super.init();
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();

        if (!getOKAction().isEnabled()) {
            return;
        }

        for (Problem problem : problemList) {
            problem.setClassPath(problem.getClassName() + ".java");
        }

        ContentFile contentFile = context.getContentFile();
        if (!contentFile.read()) {
            showError("Adding problems failed!");
            return;
        }

        ArrayList<ProblemFile> contentProblemFileList = contentFile.getProblemFileList();
        ArrayList<PlatformFile> contentPlatformFileList = contentFile.getPlatformFileList();
        if (platform == null) {
            ArrayList<ProblemFile> problemFileList = new ArrayList<>();
            for (Problem problem : problemList) {
                ProblemFile problemFile = contentFile.getProblemFile(
                        getProblemFileName("problem", problemFileList, contentProblemFileList));
                problemFile.set(problem);
                if (!problemFile.write()) {
                    showError("Adding problems failed!");
                    return;
                }

                problemFileList.add(problemFile);
            }

            contentProblemFileList.addAll(problemFileList);

            context.getToolWindowPanel().add(contentFile, problemFileList);
        } else {
            PlatformFile platformFile = contentFile.getPlatformFile(platform.getCode().toLowerCase());
            if (!contentPlatformFileList.contains(platformFile)) {
                platformFile.set(platform);

                contentPlatformFileList.add(platformFile);
            } else {
                if (!platformFile.read()) {
                    showError("Adding problems failed!");
                    return;
                }
            }

            ArrayList<ProblemFile> platformProblemFileList = platformFile.getProblemFileList();
            ArrayList<ContestFile> platformContestFileList = platformFile.getContestFileList();
            if (contest == null) {
                ArrayList<ProblemFile> problemFileList = new ArrayList<>();
                for (Problem problem : problemList) {
                    ProblemFile problemFile = platformFile.getProblemFile(
                            getProblemFileName(problem.getCode(), problemFileList, platformProblemFileList));
                    problemFile.set(problem);
                    if (!problemFile.write()) {
                        showError("Adding problems failed!");
                        return;
                    }

                    problemFileList.add(problemFile);
                }

                platformProblemFileList.addAll(problemFileList);

                context.getToolWindowPanel().add(contentFile, platformFile, problemFileList);
            } else {
                ContestFile contestFile = platformFile.getContestFile(contest.getCode().toLowerCase());
                if (!platformContestFileList.contains(contestFile)) {
                    contestFile.set(contest);

                    platformContestFileList.add(contestFile);
                } else {
                    if (!contestFile.read()) {
                        showError("Adding problems failed!");
                        return;
                    }
                }

                ArrayList<ProblemFile> contestProblemFileList = contestFile.getProblemFileList();

                ArrayList<ProblemFile> problemFileList = new ArrayList<>();
                for (Problem problem : problemList) {
                    ProblemFile problemFile = contestFile.getProblemFile(
                            getProblemFileName(problem.getCode(), problemFileList, contestProblemFileList));
                    problemFile.set(problem);
                    if (!problemFile.write()) {
                        showError("Adding problems failed!");
                        return;
                    }

                    problemFileList.add(problemFile);
                }

                contestProblemFileList.addAll(problemFileList);

                context.getToolWindowPanel().add(contentFile, platformFile, contestFile, problemFileList);

                if (!contestFile.write()) {
                    showError("Adding problems failed!");
                    return;
                }
            }

            if (!platformFile.write()) {
                showError("Adding problems failed!");
                return;
            }
        }

        if (!contentFile.write()) {
            showError("Adding problems failed!");
            return;
        }

        close(DialogWrapper.OK_EXIT_CODE);

        Project project = context.getProject();
        Setting setting = context.getSetting();
        LocalFileSystem localFileSystem = LocalFileSystem.getInstance();
        PsiManager psiManager = PsiManager.getInstance(project);
        VirtualFile sourceDirectoryFile = localFileSystem.findFileByIoFile(setting.getSourceDirectoryFile());
        if (sourceDirectoryFile == null) {
            SwingUtils.showError(project, "Adding classes failed!");
            return;
        }

        PsiDirectory sourceDirectoryPsi = psiManager.findDirectory(sourceDirectoryFile);
        if (sourceDirectoryPsi == null) {
            SwingUtils.showError(project, "Adding classes failed!");
            return;
        }

        VirtualFile templateClassFile = localFileSystem.findFileByIoFile(setting.getTemplateClassFile());
        if (templateClassFile == null) {
            SwingUtils.showError(project, "Adding classes failed!");
            return;
        }

        PsiJavaFile templateClassPsi = (PsiJavaFile) psiManager.findFile(templateClassFile);
        if (templateClassPsi == null) {
            SwingUtils.showError(project, "Adding classes failed!");
            return;
        }

        if (templateClassPsi.getClasses().length != 1) {
            SwingUtils.showError(project, "Adding classes failed!");
            return;
        }

        Application application = ApplicationManager.getApplication();
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        for (Problem problem : problemList) {
            application.runWriteAction(() -> {
                PsiJavaFile problemClassPsi = (PsiJavaFile) sourceDirectoryPsi
                        .copyFileFrom(problem.getClassPath(), templateClassPsi);

                application.invokeLater(() -> {
                    new RenameProcessor(project, problemClassPsi.getClasses()[0], problem.getClassName(), false, false) {

                        @NotNull
                        @Override
                        public UsageInfo[] findUsages() {
                            ArrayList<UsageInfo> usageInfoList = new ArrayList<>();
                            for (UsageInfo usageInfo : super.findUsages()) {
                                PsiFile psi = usageInfo.getFile();
                                if (psi != null && psi.equals(problemClassPsi)) {
                                    usageInfoList.add(usageInfo);
                                }
                            }

                            return usageInfoList.toArray(new UsageInfo[usageInfoList.size()]);
                        }

                    }.run();

                    fileEditorManager.openFile(problemClassPsi.getVirtualFile(), true);
                });
            });
        }
    }

    private String getProblemFileName(String problemCode,
                                      ArrayList<ProblemFile> problemFileList1,
                                      ArrayList<ProblemFile> problemFileList2) {
        ArrayList<ProblemFile> fileList = new ArrayList<>();
        fileList.addAll(problemFileList1);
        fileList.addAll(problemFileList2);

        problemCode = problemCode.toLowerCase();

        ArrayList<Integer> numberList = new ArrayList<>();
        for (ProblemFile file : fileList) {
            String fileName = file.getFile().getName();
            if (fileName.startsWith(problemCode)) {
                String number = fileName.substring(problemCode.length(), fileName.lastIndexOf('.'));
                numberList.add(number.isEmpty() ? 0 : Integer.parseInt(number));
            }
        }

        numberList.sort(Comparator.naturalOrder());

        if (numberList.isEmpty() || numberList.get(0) != 0) {
            return problemCode;
        }

        for (int i = 1; i < numberList.size(); i++) {
            if (numberList.get(i) != i) {
                return problemCode + i;
            }
        }

        return problemCode + numberList.size();
    }

}
