package com.manojkhannakm.prosolver.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.DumbAware;
import com.manojkhannakm.prosolver.model.Context;

import javax.swing.*;

/**
 * @author Manoj Khanna
 */

public abstract class Action extends AnAction implements DumbAware {

    protected final Context context;

    public Action(Context context, Icon icon, String text) {
        super(text, null, icon);

        this.context = context;
    }

}
