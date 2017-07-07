package com.manojkhannakm.prosolver.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.model.Context;

/**
 * @author Manoj Khanna
 */

@SuppressWarnings("ComponentNotRegistered")
public class SortAction extends ToggleAction {

    public SortAction(Context context) {
        super(context, Constants.Icons.Actions.SORT, "Sort");
    }

    @Override
    public void actionPerformed(AnActionEvent e, boolean selected) {
        context.getToolWindowPanel().sort(selected);
    }

}
