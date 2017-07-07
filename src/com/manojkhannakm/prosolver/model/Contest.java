package com.manojkhannakm.prosolver.model;

import java.util.ArrayList;

/**
 * @author Manoj Khanna
 */

public class Contest {

    private String url, code, name;
    private ArrayList<Problem> problemList;

    public Contest() {
    }

    public Contest(String url) {
        this.url = url;
    }

    public Contest(String url, String code, String name) {
        this.url = url;
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Problem> getProblemList() {
        return problemList;
    }

    public void setProblemList(ArrayList<Problem> problemList) {
        this.problemList = problemList;
    }

}
