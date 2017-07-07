package com.manojkhannakm.prosolver.model;

import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.util.StringUtils;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author Manoj Khanna
 */

public class Setting {

    private final Context context;

    private String sourceDirectory = "",
            templateClass = "",
            packagePattern = "",
            classPattern = "";
    private String[] classArguments = new String[0];

    public Setting(Context context) {
        this.context = context;
    }

    public File getSourceDirectoryFile() {
        if (!sourceDirectory.isEmpty()) {
            File sourceDirectoryFile = context.getFile(sourceDirectory);
            if (sourceDirectoryFile.isDirectory()) {
                return sourceDirectoryFile;
            }
        }

        return context.getFile("src/");
    }

    public File getTemplateClassFile() {
        if (!templateClass.isEmpty()) {
            File templateClassFile = context.getFile(templateClass);
            if (templateClassFile.isFile()) {
                return templateClassFile;
            }
        }

        try {
            //noinspection ConstantConditions
            return new File(getClass().getClassLoader()
                    .getResource(Constants.Strings.Files.TEMPLATE_CLASS).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getPackageName(Platform platform, Contest contest, Problem problem) {
        String packageName = StringUtils.getName(packagePattern, platform, contest, problem);
        if (packageName == null || !StringUtils.isPackageNameValid(packageName)) {
            return null;
        }

        return packageName;
    }

    public String getClassName(Platform platform, Contest contest, Problem problem) {
        String className = StringUtils.getName(classPattern, platform, contest, problem);
        if (className == null || !StringUtils.isClassNameValid(className)) {
            return null;
        }

        return className;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public String getTemplateClass() {
        return templateClass;
    }

    public void setTemplateClass(String templateClass) {
        this.templateClass = templateClass;
    }

    public String getPackagePattern() {
        return packagePattern;
    }

    public void setPackagePattern(String packagePattern) {
        this.packagePattern = packagePattern;
    }

    public String getClassPattern() {
        return classPattern;
    }

    public void setClassPattern(String classPattern) {
        this.classPattern = classPattern;
    }

    public String[] getClassArguments() {
        return classArguments;
    }

    public void setClassArguments(String[] classArguments) {
        this.classArguments = classArguments;
    }

}
