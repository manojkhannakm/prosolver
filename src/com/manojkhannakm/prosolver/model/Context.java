package com.manojkhannakm.prosolver.model;

import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.file.ContentFile;
import com.manojkhannakm.prosolver.file.SettingFile;
import com.manojkhannakm.prosolver.swing.toolwindow.ToolWindowPanel;
import com.manojkhannakm.prosolver.util.SwingUtils;

import javax.swing.*;
import java.io.File;

/**
 * @author Manoj Khanna
 */

public class Context {

    private final Project project;

    private Setting setting;
    private SettingFile settingFile;

    private ActionButton removeButton, editButton, moveButton, runButton;
    private ToolWindowPanel toolWindowPanel;

    public Context(Project project) {
        this.project = project;
    }

    public void create() {
        setting = new Setting(this);

        settingFile = new SettingFile(getFile(Constants.Strings.Files.SETTINGS));
        settingFile.set(setting);
        if (!settingFile.read()) {
            SwingUtils.showError(project, "Loading settings failed!");
        }

        JComponent component = ToolWindowManager.getInstance(project)
                .getToolWindow(Constants.Strings.PROSOLVER).getComponent();
        for (ActionButton actionButton : SwingUtils.getComponents(component, ActionButton.class)) {
            String actionText = actionButton.getAction().getTemplatePresentation().getText();
            if (actionText != null) {
                //noinspection IfCanBeSwitch
                if (actionText.equals("Remove")) {
                    removeButton = actionButton;
                } else if (actionText.equals("Edit")) {
                    editButton = actionButton;
                } else if (actionText.equals("Move")) {
                    moveButton = actionButton;
                } else if (actionText.equals("Run")) {
                    runButton = actionButton;
                }
            }
        }

        toolWindowPanel = SwingUtils.getComponents(component, ToolWindowPanel.class).get(0);
    }

    public File getFile(String fileName) {
        return new File(project.getBasePath(), fileName);
    }

    public ContentFile getContentFile() {
        return new ContentFile(getFile(Constants.Strings.Files.CONTENTS));
    }

    public Project getProject() {
        return project;
    }

    public Setting getSetting() {
        return setting;
    }

    public SettingFile getSettingFile() {
        return settingFile;
    }

    public ActionButton getRemoveButton() {
        return removeButton;
    }

    public ActionButton getEditButton() {
        return editButton;
    }

    public ActionButton getRunButton() {
        return runButton;
    }

    public ActionButton getMoveButton() {
        return moveButton;
    }

    public ToolWindowPanel getToolWindowPanel() {
        return toolWindowPanel;
    }

}
