package com.manojkhannakm.prosolver.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.model.Context;

/**
 * @author Manoj Khanna
 */

@SuppressWarnings("ComponentNotRegistered")
public class FilterAction extends ToggleAction {

    public FilterAction(Context context) {
        super(context, Constants.Icons.Actions.FILTER, "Filter");
    }

    @Override
    public void actionPerformed(AnActionEvent e, boolean selected) {
        context.getToolWindowPanel().filter(selected);
    }

}
