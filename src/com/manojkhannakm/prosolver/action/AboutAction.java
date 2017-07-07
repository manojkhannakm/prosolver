package com.manojkhannakm.prosolver.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.model.Context;

/**
 * @author Manoj Khanna
 */

@SuppressWarnings("ComponentNotRegistered")
public class AboutAction extends Action {

    public AboutAction(Context context) {
        super(context, Constants.Icons.Actions.ABOUT, "About");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        //TODO
    }

}
