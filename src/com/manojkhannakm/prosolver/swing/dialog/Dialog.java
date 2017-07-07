package com.manojkhannakm.prosolver.swing.dialog;

import com.intellij.openapi.ui.DialogEarthquakeShaker;
import com.intellij.openapi.ui.DialogWrapper;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.model.Context;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Manoj Khanna
 */

public abstract class Dialog extends DialogWrapper {

    protected final Context context;

    private String error;

    protected Dialog(Context context) {
        super(context.getProject());

        this.context = context;

        getWindow().setIconImage(Constants.Images.ICON);
    }

    @Override
    protected void setErrorText(@Nullable String text) {
        super.setErrorText(text);

        error = text;
    }

    public void showError(String error) {
        if (this.error == null) {
            DialogEarthquakeShaker.shake(getWindow());
        }

        setValidationDelay(3000);

        setErrorText(error);

        Timer timer = new Timer(2500, e -> {
            setErrorText(null);

            setValidationDelay(300);
        });
        timer.setRepeats(false);
        timer.start();
    }

}
