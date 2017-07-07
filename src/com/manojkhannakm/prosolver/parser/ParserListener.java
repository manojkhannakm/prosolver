package com.manojkhannakm.prosolver.parser;

import com.manojkhannakm.prosolver.model.Contest;
import com.manojkhannakm.prosolver.model.Problem;

/**
 * @author Manoj Khanna
 */

public interface ParserListener {

    void problemParsed(Problem problem);

    void contestParsed(Contest contest);

}
