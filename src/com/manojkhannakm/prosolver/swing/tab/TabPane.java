package com.manojkhannakm.prosolver.swing.tab;

import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.intellij.util.ui.AsyncProcessIcon;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author Manoj Khanna
 */

public class TabPane<T extends Tab> extends JBTabsImpl {

    private final LinkedHashMap<TabInfo, T> tabMap = new LinkedHashMap<>();

    public TabPane(Project project) {
        super(project);
    }

    @Override
    protected TabLabel createTabLabel(TabInfo info) {
        return new TabLabel(info);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);

        if (evt.getPropertyName().equals(Tab.BUSY_PROPERTY)) {
            //noinspection unchecked,SuspiciousMethodCalls
            TabLabel tabLabel = (TabLabel) myInfo2Label.get(evt.getSource());
            if (tabLabel != null) {
                tabLabel.setBusy((Boolean) evt.getNewValue());
            }
        }
    }

    public void addTab(T tab) {
        TabInfo tabInfo = tab.getTabInfo();
        addTab(tabInfo);

        tabMap.put(tabInfo, tab);
    }

    public void removeTab(T tab) {
        TabInfo tabInfo = tab.getTabInfo();
        removeTab(tabInfo);

        tabMap.remove(tabInfo);
    }

    public T getTab(int index) {
        return tabMap.get(getTabAt(index));
    }

    public ArrayList<T> getAllTabs() {
        return new ArrayList<>(tabMap.values());
    }

    public T getSelectedTab() {
        return tabMap.get(getSelectedInfo());
    }

    public void setSelectedTab(T tab) {
        select(tab.getTabInfo(), true);
    }

    private class TabLabel extends com.intellij.ui.tabs.impl.TabLabel {

        private final AsyncProcessIcon busyIcon;

        public TabLabel(TabInfo tabInfo) {
            super(TabPane.this, tabInfo);

            busyIcon = new AsyncProcessIcon(toString());
            busyIcon.setOpaque(false);
            busyIcon.setUseMask(false);
            busyIcon.setVisible(false);
            busyIcon.suspend();
            add(busyIcon, BorderLayout.EAST);
        }

        public void setBusy(boolean busy) {
            if (busy == busyIcon.isRunning()) {
                return;
            }

            if (busy) {
                busyIcon.setVisible(true);
                busyIcon.resume();
            } else {
                busyIcon.suspend();
                busyIcon.setVisible(false);
            }

            myTabs.repaint();
        }

    }

}
