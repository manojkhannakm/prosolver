package com.manojkhannakm.prosolver.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.model.Context;
import com.manojkhannakm.prosolver.swing.dialog.AddDialog;

/**
 * @author Manoj Khanna
 */

@SuppressWarnings("ComponentNotRegistered")
public class AddAction extends Action {

    public AddAction(Context context) {
        super(context, Constants.Icons.Actions.ADD, "Add");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        new AddDialog(context).show();
    }

}
