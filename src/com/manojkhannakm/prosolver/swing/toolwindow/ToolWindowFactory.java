package com.manojkhannakm.prosolver.swing.toolwindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.ContentManager;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.action.*;
import com.manojkhannakm.prosolver.model.Context;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Manoj Khanna
 */

public class ToolWindowFactory implements com.intellij.openapi.wm.ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        toolWindow.setIcon(Constants.Icons.ICON);

        Context context = new Context(project);

        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(true);

        DefaultActionGroup actionGroup = new DefaultActionGroup();

        DefaultActionGroup addActionGroup = new DefaultActionGroup("Add", true);
        addActionGroup.getTemplatePresentation().setIcon(Constants.Icons.Actions.ADD);
        addActionGroup.add(new AddAction(context));
        addActionGroup.add(new AddProblemAction(context));
        addActionGroup.add(new AddContestAction(context));
        actionGroup.add(addActionGroup);

        actionGroup.add(new RemoveAction(context));
        actionGroup.add(new EditAction(context));
        actionGroup.add(new MoveAction(context));
        actionGroup.add(new RunAction(context));
        actionGroup.addSeparator();
        actionGroup.add(new RefreshAction(context));
        actionGroup.add(new SortAction(context));
        actionGroup.add(new FilterAction(context));
        actionGroup.add(new ExpandAllAction(context));
        actionGroup.add(new CollapseAllAction(context));
        actionGroup.addSeparator();
        actionGroup.add(new SettingAction(context));
        actionGroup.add(new AboutAction(context));

        panel.setToolbar(ActionManager.getInstance()
                .createActionToolbar(ActionPlaces.TOOLBAR, actionGroup, true).getComponent());

        panel.setContent(new ToolWindowPanel(context));

        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.addContent(contentManager.getFactory().createContent(panel, null, true));

        SwingUtilities.invokeLater(context::create);
    }

}
