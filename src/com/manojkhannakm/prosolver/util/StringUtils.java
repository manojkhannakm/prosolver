package com.manojkhannakm.prosolver.util;

import com.manojkhannakm.prosolver.model.Contest;
import com.manojkhannakm.prosolver.model.Platform;
import com.manojkhannakm.prosolver.model.Problem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Manoj Khanna
 */

public class StringUtils {

    private static final String NAME_REGEX = "[a-zA-Z_][0-9a-zA-Z_]*";
    private static final String PATTERN_REGEX = "([a-zA-Z_]|(lower|upper|snake|camel|pascal)\\((platform|problem|contest)_(code|name)\\))" +
            "([0-9a-zA-Z_]|(lower|upper|snake|camel|pascal)\\((platform|problem|contest)_(code|name)\\))*";

    public static String getLowerCase(String string) {
        return string.replaceAll(" +", "").toLowerCase();
    }

    public static String getUpperCase(String string) {
        return string.replaceAll(" +", "").toUpperCase();
    }

    public static String getSnakeCase(String string) {
        return string.replaceAll(" +", "_").toLowerCase();
    }

    public static String getCamelCase(String string) {
        Matcher matcher = Pattern.compile(" +([a-z])").matcher(string.toLowerCase());
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, matcher.group(1).toUpperCase());
        }

        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }

    public static String getPascalCase(String string) {
        Matcher matcher = Pattern.compile("^([a-z])").matcher(getCamelCase(string));
        StringBuffer stringBuffer = new StringBuffer();
        if (matcher.find()) {
            matcher.appendReplacement(stringBuffer, matcher.group(1).toUpperCase());
        }

        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }

    public static String getName(String pattern, Platform platform, Contest contest, Problem problem) {
        if (pattern.isEmpty()) {
            return null;
        }

        String[][] strings = new String[][]{
                new String[]{"platform_code", platform != null ? platform.getCode() : null},
                new String[]{"platform_name", platform != null ? platform.getName() : null},
                new String[]{"contest_code", contest != null ? contest.getCode() : null},
                new String[]{"contest_name", contest != null ? contest.getName() : null},
                new String[]{"problem_code", problem != null ? problem.getCode() : null},
                new String[]{"problem_name", problem != null ? problem.getName() : null}
        };
        for (String[] string : strings) {
            String string0 = string[0],
                    string1 = string[1];
            if (pattern.contains(string0)) {
                if (string1 == null) {
                    return null;
                }

                pattern = pattern.replace(string0, string1);
            }
        }

        Matcher matcher = Pattern.compile("(?:lower|upper|snake|camel|pascal)\\((.*?)\\)").matcher(pattern);
        while (matcher.find()) {
            String match0 = matcher.group(0),
                    match1 = matcher.group(1);
            if (match0.startsWith("lower")) {
                pattern = pattern.replace(match0, getLowerCase(match1));
            } else if (match0.startsWith("upper")) {
                pattern = pattern.replace(match0, getUpperCase(match1));
            } else if (match0.startsWith("snake")) {
                pattern = pattern.replace(match0, getSnakeCase(match1));
            } else if (match0.startsWith("camel")) {
                pattern = pattern.replace(match0, getCamelCase(match1));
            } else if (match0.startsWith("pascal")) {
                pattern = pattern.replace(match0, getPascalCase(match1));
            }
        }

        return pattern;
    }

    public static boolean isPackageNameValid(String packageName) {
        return packageName.matches(NAME_REGEX + "(\\." + NAME_REGEX + ")*");
    }

    public static boolean isClassNameValid(String className) {
        return className.matches(NAME_REGEX);
    }

    public static boolean isPackagePatternValid(String packagePattern) {
        return packagePattern.matches(PATTERN_REGEX + "(\\." + PATTERN_REGEX + ")*");
    }

    public static boolean isClassPatternValid(String classPattern) {
        return classPattern.matches(PATTERN_REGEX);
    }

}
