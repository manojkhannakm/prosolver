package com.manojkhannakm.prosolver.platform.codechef;

import com.manojkhannakm.prosolver.model.Category;
import com.manojkhannakm.prosolver.model.Contest;
import com.manojkhannakm.prosolver.model.Platform;
import com.manojkhannakm.prosolver.model.Problem;
import com.manojkhannakm.prosolver.parser.Parser;

/**
 * @author Manoj Khanna
 */

public class CodeChefPlatform extends Platform {

    public CodeChefPlatform() {
        super("https://www.codechef.com", "codechef", "CodeChef");
    }

    @Override
    public Category[] getProblemCategories() {
        return new Category[]{
                new Category("https://www.codechef.com/problems/school", "school", "Beginner"),
                new Category("https://www.codechef.com/problems/easy", "easy", "Easy"),
                new Category("https://www.codechef.com/problems/medium", "medium", "Medium"),
                new Category("https://www.codechef.com/problems/hard", "hard", "Hard"),
                new Category("https://www.codechef.com/problems/challenge", "challenge", "Challenge"),
                new Category("https://www.codechef.com/problems/extcontest", "extcontest", "Peer")
        };
    }

    @Override
    public Category[] getContestCategories() {
        return new Category[]{
                new Category("https://www.codechef.com/contests", "present", "Present"),
                new Category("https://www.codechef.com/contests", "past", "Past")
        };
    }

    @Override
    public Parser getParser() {
        return new CodeChefParser();
    }

    @Override
    public String getContestUrl(Problem problem) {
        String problemUrl = problem.getUrl();
        return problemUrl.substring(0, problemUrl.indexOf("/problems/"));
    }

    @Override
    public String getPackageName(Contest contest, Problem problem) {
        if (contest == null) {
            return getCode();
        } else {
            String contestCode = contest.getCode();
            if (contestCode == null) {
                return null;
            }

            return getCode() + "." + contestCode.toLowerCase();
        }
    }

    @Override
    public String getClassName(Contest contest, Problem problem) {
        String problemCode = problem.getCode();
        if (problemCode == null) {
            return null;
        }

        return problemCode;
    }

}
