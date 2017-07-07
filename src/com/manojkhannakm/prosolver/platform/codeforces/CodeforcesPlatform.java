package com.manojkhannakm.prosolver.platform.codeforces;

import com.manojkhannakm.prosolver.model.Category;
import com.manojkhannakm.prosolver.model.Contest;
import com.manojkhannakm.prosolver.model.Platform;
import com.manojkhannakm.prosolver.model.Problem;
import com.manojkhannakm.prosolver.parser.Parser;

/**
 * @author Manoj Khanna
 */

public class CodeforcesPlatform extends Platform {                                                                      //TODO

    public CodeforcesPlatform() {
        super("http://codeforces.com", "codeforces", "Codeforces");
    }

    @Override
    public Category[] getProblemCategories() {
        return new Category[]{
                new Category("http://codeforces.com/problemset", "all", "All")
        };
    }

    @Override
    public Category[] getContestCategories() {
        return new Category[]{
                new Category("http://codeforces.com/contests", "current", "Current"),
                new Category("http://codeforces.com/contests", "past", "Past")
        };
    }

    @Override
    public Parser getParser() {
        return null;
    }

    @Override
    public String getContestUrl(Problem problem) {
        return null;
    }

    @Override
    public String getPackageName(Contest contest, Problem problem) {
        return null;
    }

    @Override
    public String getClassName(Contest contest, Problem problem) {
        return null;
    }

}
