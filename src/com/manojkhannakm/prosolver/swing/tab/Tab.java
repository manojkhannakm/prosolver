package com.manojkhannakm.prosolver.swing.tab;

import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.tabs.TabInfo;

import javax.swing.*;

/**
 * @author Manoj Khanna
 */

public class Tab {

    public static final String BUSY_PROPERTY = "busy";

    private final TabInfo tabInfo;

    public Tab() {
        tabInfo = new TabInfo(null);
        tabInfo.setText(toString());
    }

    public void setComponent(JComponent component) {
        tabInfo.setComponent(component);
    }

    public void setText(String text) {
        SimpleTextAttributes attributes = tabInfo.getColoredText().getAttributes().get(0);

        tabInfo.clearText(false);

        tabInfo.append(text, attributes);
    }

    public void setStyle(int style) {
        String text = tabInfo.getColoredText().getTexts().get(0);

        tabInfo.clearText(false);

        tabInfo.append(text, new SimpleTextAttributes(style, null));
    }

    public void setBusy(boolean busy) {
        tabInfo.getChangeSupport().firePropertyChange(BUSY_PROPERTY, null, busy);
    }

    public TabInfo getTabInfo() {
        return tabInfo;
    }

}
