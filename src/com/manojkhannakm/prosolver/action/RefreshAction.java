package com.manojkhannakm.prosolver.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.model.Context;

/**
 * @author Manoj Khanna
 */

@SuppressWarnings("ComponentNotRegistered")
public class RefreshAction extends Action {

    public RefreshAction(Context context) {
        super(context, Constants.Icons.Actions.REFRESH, "Refresh");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        context.getToolWindowPanel().refresh(true);
    }

}
