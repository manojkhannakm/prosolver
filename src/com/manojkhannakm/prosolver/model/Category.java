package com.manojkhannakm.prosolver.model;

/**
 * @author Manoj Khanna
 */

public class Category {

    private final String url, code, name;

    public Category(String url, String code, String name) {
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

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
