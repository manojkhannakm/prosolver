package com.manojkhannakm.prosolver.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.BalloonImpl;
import com.manojkhannakm.prosolver.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Manoj Khanna
 */

public class SwingUtils {

    public static <T extends JComponent> ArrayList<T> getComponents(JComponent component, Class<T> componentClass) {
        ArrayList<T> componentList = new ArrayList<>();
        for (Component childComponent : component.getComponents()) {
            if (!(childComponent instanceof JComponent)) {
                continue;
            }

            if (componentClass.isAssignableFrom(childComponent.getClass())) {
                //noinspection unchecked
                componentList.add((T) childComponent);
            }

            componentList.addAll(getComponents((JComponent) childComponent, componentClass));
        }

        return componentList;
    }

    public static void showError(Project project, String error) {
        Notification notification = new Notification(Constants.Strings.PROSOLVER,
                Constants.Strings.PROSOLVER, error, NotificationType.ERROR);
        notification.notify(project);

        //noinspection ConstantConditions
        ((BalloonImpl) notification.getBalloon()).startSmartFadeoutTimer(2500);
    }

}
