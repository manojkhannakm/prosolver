package com.manojkhannakm.prosolver.model;

import com.manojkhannakm.prosolver.parser.Parser;
import com.manojkhannakm.prosolver.platform.codechef.CodeChefPlatform;
import com.manojkhannakm.prosolver.platform.codeforces.CodeforcesPlatform;

import java.util.ArrayList;

/**
 * @author Manoj Khanna
 */

public abstract class Platform {

    private final String url, code, name;

    private ArrayList<Problem> problemList;
    private ArrayList<Contest> contestList;

    protected Platform(String url, String code, String name) {
        this.url = url;
        this.code = code;
        this.name = name;
    }

    public static Platform[] getPlatforms() {
        return new Platform[]{
                new CodeChefPlatform(),
                new CodeforcesPlatform()
        };
    }

    public static Platform getPlatform(String code) {
        for (Platform platform : getPlatforms()) {
            if (platform.getCode().equals(code)) {
                return platform;
            }
        }

        return null;
    }

    public abstract Category[] getProblemCategories();

    public abstract Category[] getContestCategories();

    public abstract Parser getParser();

    public abstract String getContestUrl(Problem problem);

    public abstract String getPackageName(Contest contest, Problem problem);

    public abstract String getClassName(Contest contest, Problem problem);

    @Override
    public String toString() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Problem> getProblemList() {
        return problemList;
    }

    public void setProblemList(ArrayList<Problem> problemList) {
        this.problemList = problemList;
    }

    public ArrayList<Contest> getContestList() {
        return contestList;
    }

    public void setContestList(ArrayList<Contest> contestList) {
        this.contestList = contestList;
    }

}
