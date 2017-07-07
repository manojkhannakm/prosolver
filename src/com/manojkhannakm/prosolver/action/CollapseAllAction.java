package com.manojkhannakm.prosolver.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.model.Context;

/**
 * @author Manoj Khanna
 */

@SuppressWarnings("ComponentNotRegistered")
public class CollapseAllAction extends Action {

    public CollapseAllAction(Context context) {
        super(context, Constants.Icons.Actions.COLLAPSE_ALL, "Collapse All");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        context.getToolWindowPanel().collapseAll();
    }

}
