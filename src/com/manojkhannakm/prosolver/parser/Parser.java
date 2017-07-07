package com.manojkhannakm.prosolver.parser;

import com.manojkhannakm.prosolver.model.Category;
import com.manojkhannakm.prosolver.model.Contest;
import com.manojkhannakm.prosolver.model.Platform;
import com.manojkhannakm.prosolver.model.Problem;

/**
 * @author Manoj Khanna
 */

public abstract class Parser {

    public abstract boolean parseProblem(Problem problem);

    public abstract boolean parseContest(Contest contest);

    public abstract boolean parseProblems(Platform platform, Category category, ParserListener listener);

    public abstract boolean parseContests(Platform platform, Category category, ParserListener listener);

}
