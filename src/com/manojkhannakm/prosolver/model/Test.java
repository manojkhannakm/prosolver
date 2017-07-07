package com.manojkhannakm.prosolver.model;

/**
 * @author Manoj Khanna
 */

public class Test {

    private boolean enabled;
    private String input, output;

    public Test() {
    }

    public Test(boolean enabled, String input, String output) {
        this.input = input;
        this.output = output;
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

}
