package com.manojkhannakm.prosolver.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.model.Context;

/**
 * @author Manoj Khanna
 */

@SuppressWarnings("ComponentNotRegistered")
public class ExpandAllAction extends Action {

    public ExpandAllAction(Context context) {
        super(context, Constants.Icons.Actions.EXPAND_ALL, "Expand All");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        context.getToolWindowPanel().expandAll();
    }

}
