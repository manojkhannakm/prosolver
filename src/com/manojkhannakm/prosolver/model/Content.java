package com.manojkhannakm.prosolver.model;

import java.util.ArrayList;

/**
 * @author Manoj Khanna
 */

public class Content {

    private ArrayList<Problem> problemList;
    private ArrayList<Platform> platformList;

    public ArrayList<Problem> getProblemList() {
        return problemList;
    }

    public void setProblemList(ArrayList<Problem> problemList) {
        this.problemList = problemList;
    }

    public ArrayList<Platform> getPlatformList() {
        return platformList;
    }

    public void setPlatformList(ArrayList<Platform> platformList) {
        this.platformList = platformList;
    }

}
