package com.manojkhannakm.prosolver.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.model.Context;
import com.manojkhannakm.prosolver.swing.dialog.SettingDialog;

/**
 * @author Manoj Khanna
 */

@SuppressWarnings("ComponentNotRegistered")
public class SettingAction extends Action {

    public SettingAction(Context context) {
        super(context, Constants.Icons.Actions.SETTING, "Settings");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        new SettingDialog(context).show();
    }

}
