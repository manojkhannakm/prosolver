package com.manojkhannakm.prosolver.swing.action;

import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.impl.PresentationFactory;

import java.awt.*;

/**
 * @author Manoj Khanna
 */

public class ActionButton extends com.intellij.openapi.actionSystem.impl.ActionButton {

    public ActionButton(AnAction action) {
        super(action, new PresentationFactory().getPresentation(action),
                ActionPlaces.TOOLBAR, new Dimension(25, 25));
    }

}
