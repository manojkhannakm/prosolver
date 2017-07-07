package com.manojkhannakm.prosolver;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.intellij.util.ImageLoader;

import javax.swing.*;
import java.awt.*;

/**
 * @author Manoj Khanna
 */

public class Constants {

    public static class Strings {

        public static final String PROSOLVER = "ProSolver";

        public static class Files {

            public static final String FILES = "files/";
            public static final String TEMPLATE_CLASS = FILES + "Template.java";

            public static final String PROSOLVER = "prosolver/";
            public static final String CONTENTS = PROSOLVER + "contents.json";
            public static final String SETTINGS = PROSOLVER + "settings.json";

        }

    }

    public static class Icons {

        public static final Icon ICON = icon("icon");

        private static Icon icon(String name) {
            return IconLoader.getIcon("/icons/" + name + ".png");
        }

        public static class Actions {

            public static final Icon ADD = icon("add");
            public static final Icon REMOVE = icon("remove");
            public static final Icon EDIT = icon("edit");
            public static final Icon MOVE = icon("move");
            public static final Icon RUN = icon("run");
            public static final Icon REFRESH = icon("refresh");
            public static final Icon SORT = icon("sort");
            public static final Icon FILTER = icon("filter");
            public static final Icon EXPAND_ALL = icon("expand_all");
            public static final Icon COLLAPSE_ALL = icon("collapse_all");
            public static final Icon SETTING = icon("setting");
            public static final Icon ABOUT = icon("about");
            public static final Icon TOGGLE = icon("toggle");

        }

        public static class Files {                                                                                     //TODO

            public static final Icon PLATFORM = AllIcons.Nodes.Folder;
            public static final Icon CONTEST = AllIcons.Nodes.Folder;
            public static final Icon PROBLEM = AllIcons.FileTypes.Any_type;

        }

    }

    public static class Images {                                                                                        //TODO

        public static final Image ICON = image("icon");

        private static Image image(String name) {
            return ImageLoader.loadFromResource("/images/" + name + ".png");
        }

    }

}
