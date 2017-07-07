package com.manojkhannakm.prosolver.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.model.Content;
import com.manojkhannakm.prosolver.model.Context;
import com.manojkhannakm.prosolver.model.Platform;
import com.manojkhannakm.prosolver.model.Problem;
import com.manojkhannakm.prosolver.platform.codechef.CodeChefPlatform;
import com.manojkhannakm.prosolver.swing.dialog.AddDialog;

import java.util.ArrayList;

/**
 * @author Manoj Khanna
 */

@SuppressWarnings("ComponentNotRegistered")
public class AddProblemAction extends Action {

    public AddProblemAction(Context context) {
        super(context, Constants.Icons.Actions.ADD, "Add Problem");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {                                                                      //TODO
        Content content = new Content();

        ArrayList<Platform> platformList = new ArrayList<>();
        content.setPlatformList(platformList);

        CodeChefPlatform platform = new CodeChefPlatform();
        platformList.add(platform);

        ArrayList<Problem> problemList = new ArrayList<>();
        platform.setProblemList(problemList);

        problemList.add(new Problem("https://www.codechef.com/problems/CHEFKEY"));
        problemList.add(new Problem("https://www.codechef.com/problems/TALAZY", "TALAZY", "Lazy Jem"));

        new AddDialog(context, content).show();
    }

}
