package com.manojkhannakm.prosolver.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.model.*;
import com.manojkhannakm.prosolver.platform.codechef.CodeChefPlatform;
import com.manojkhannakm.prosolver.swing.dialog.AddDialog;

import java.util.ArrayList;

/**
 * @author Manoj Khanna
 */

@SuppressWarnings("ComponentNotRegistered")
public class AddContestAction extends Action {

    public AddContestAction(Context context) {
        super(context, Constants.Icons.Actions.ADD, "Add Contest");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {                                                                      //TODO
        Content content = new Content();

        ArrayList<Platform> platformList = new ArrayList<>();
        content.setPlatformList(platformList);

        CodeChefPlatform platform = new CodeChefPlatform();
        platformList.add(platform);

        ArrayList<Contest> contestList = new ArrayList<>();
        platform.setContestList(contestList);

        Contest contest = new Contest("https://www.codechef.com/NOV16", "NOV16", "November Challenge 2016");
        contestList.add(contest);

        ArrayList<Problem> problemList = new ArrayList<>();
        contest.setProblemList(problemList);

        problemList.add(new Problem("https://www.codechef.com/NOV16/problems/ALEXTASK", "ALEXTASK", "Task for Alexey"));

        new AddDialog(context, content).show();
    }

}
