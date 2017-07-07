package com.manojkhannakm.prosolver.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.manojkhannakm.prosolver.model.Context;

import javax.swing.*;

/**
 * @author Manoj Khanna
 */

public abstract class ToggleAction extends com.intellij.openapi.actionSystem.ToggleAction implements DumbAware {

    protected final Context context;

    private boolean selected;

    public ToggleAction(Context context, Icon icon, String text) {
        super(text, null, icon);

        this.context = context;
    }

    public abstract void actionPerformed(AnActionEvent e, boolean selected);

    @Override
    public boolean isSelected(AnActionEvent e) {
        return selected;
    }

    @Override
    public void setSelected(AnActionEvent e, boolean selected) {
        this.selected = selected;

        actionPerformed(e, selected);
    }

}
