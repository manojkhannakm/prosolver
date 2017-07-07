package com.manojkhannakm.prosolver.swing.dialog;

import com.intellij.ide.IdeTooltipManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.BalloonImpl;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.manojkhannakm.prosolver.Constants;
import com.manojkhannakm.prosolver.model.Context;
import com.manojkhannakm.prosolver.model.Setting;
import com.manojkhannakm.prosolver.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

/**
 * @author Manoj Khanna
 */

public class SettingDialog extends Dialog {

    private final JWindow hintWindow;

    private JTextField sourceDirectoryTextField, templateClassTextField,
            packagePatternTextField, classPatternTextField,
            classArgumentsTextField;

    public SettingDialog(Context context) {
        super(context);

        Window window = getWindow();

        hintWindow = new JWindow(window);
        //noinspection UseJBColor
        hintWindow.setBackground(new Color(0, 0, 0, 0));
        hintWindow.setPreferredSize(new Dimension(500, 500));
        hintWindow.pack();
        hintWindow.setVisible(true);

        setTitle("Settings");
        setResizable(false);
        setOKButtonText("Save");

        init();

        window.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                Point point = window.getLocationOnScreen();
                point.x += window.getWidth() / 2;
                point.y -= window.getHeight() / 2;
                hintWindow.setLocation(point);
            }

        });
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayoutManager(6, 3));
        panel.setPreferredSize(new Dimension(320, -1));

        JLabel settingsLabel = new JLabel("Settings");
        settingsLabel.setFont(settingsLabel.getFont().deriveFont(Font.BOLD));
        GridConstraints constraints = new GridConstraints();
        constraints.setRow(0);
        constraints.setColumn(0);
        constraints.setColSpan(3);
        constraints.setAnchor(GridConstraints.ANCHOR_WEST);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        panel.add(settingsLabel, constraints);

        constraints = new GridConstraints();
        constraints.setRow(1);
        constraints.setColumn(0);
        constraints.setAnchor(GridConstraints.ANCHOR_WEST);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        panel.add(new JLabel("Source Directory"), constraints);

        sourceDirectoryTextField = new JTextField(1);
        constraints = new GridConstraints();
        constraints.setRow(1);
        constraints.setColumn(1);
        constraints.setFill(GridConstraints.FILL_HORIZONTAL);
        panel.add(sourceDirectoryTextField, constraints);

        HintIcon sourceDirectoryHintIcon = new HintIcon("<html>" +
                "<b>Example:</b><br>" +
                "src/main/java/" +
                "</html>");
        constraints = new GridConstraints();
        constraints.setRow(1);
        constraints.setColumn(2);
        constraints.setAnchor(GridConstraints.ANCHOR_EAST);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        panel.add(sourceDirectoryHintIcon, constraints);

        constraints = new GridConstraints();
        constraints.setRow(2);
        constraints.setColumn(0);
        constraints.setAnchor(GridConstraints.ANCHOR_WEST);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        panel.add(new JLabel("Template Class"), constraints);

        templateClassTextField = new JTextField(1);
        constraints = new GridConstraints();
        constraints.setRow(2);
        constraints.setColumn(1);
        constraints.setFill(GridConstraints.FILL_HORIZONTAL);
        panel.add(templateClassTextField, constraints);

        HintIcon templateClassHintIcon = new HintIcon("<html>" +
                "<b>Example:</b><br>" +
                "src/Template.java" +
                "</html>");
        constraints = new GridConstraints();
        constraints.setRow(2);
        constraints.setColumn(2);
        constraints.setAnchor(GridConstraints.ANCHOR_EAST);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        panel.add(templateClassHintIcon, constraints);

        constraints = new GridConstraints();
        constraints.setRow(3);
        constraints.setColumn(0);
        constraints.setAnchor(GridConstraints.ANCHOR_WEST);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        panel.add(new JLabel("Package Pattern"), constraints);

        packagePatternTextField = new JTextField(1);
        constraints = new GridConstraints();
        constraints.setRow(3);
        constraints.setColumn(1);
        constraints.setFill(GridConstraints.FILL_HORIZONTAL);
        panel.add(packagePatternTextField, constraints);

        HintIcon patternHintIcon = new HintIcon("<html>" +
                "<b>Characters:</b><br>" +
                "0-9, a-z, A-Z, _ and .<br><br>" +
                "<b>Variables:</b><br>" +
                "platform_code<br>" +
                "platform_name<br>" +
                "problem_code<br>" +
                "problem_name<br>" +
                "contest_code<br>" +
                "contest_name<br><br>" +
                "<b>Cases:</b><br>" +
                "lower() - lowercase<br>" +
                "upper() - UPPERCASE<br>" +
                "snake() - snake_case<br>" +
                "camel() - camelCase<br>" +
                "pascal() - PascalCase<br><br>" +
                "<b>Example:</b><br>" +
                "lower(contest_code).problem_code" +
                "</html>");
        constraints = new GridConstraints();
        constraints.setRow(3);
        constraints.setColumn(2);
        constraints.setRowSpan(2);
        constraints.setAnchor(GridConstraints.ANCHOR_EAST);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        panel.add(patternHintIcon, constraints);

        constraints = new GridConstraints();
        constraints.setRow(4);
        constraints.setColumn(0);
        constraints.setAnchor(GridConstraints.ANCHOR_WEST);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        panel.add(new JLabel("Class Pattern"), constraints);

        classPatternTextField = new JTextField(1);
        constraints = new GridConstraints();
        constraints.setRow(4);
        constraints.setColumn(1);
        constraints.setFill(GridConstraints.FILL_HORIZONTAL);
        panel.add(classPatternTextField, constraints);

        constraints = new GridConstraints();
        constraints.setRow(5);
        constraints.setColumn(0);
        constraints.setAnchor(GridConstraints.ANCHOR_WEST);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        panel.add(new JLabel("Class Arguments"), constraints);

        classArgumentsTextField = new JTextField(1);
        constraints = new GridConstraints();
        constraints.setRow(5);
        constraints.setColumn(1);
        constraints.setFill(GridConstraints.FILL_HORIZONTAL);
        panel.add(classArgumentsTextField, constraints);

        HintIcon classArgumentsHintIcon = new HintIcon("<html>" +
                "<b>Example:</b><br>" +
                "-in, in.txt, -out, out.txt" +
                "</html>");
        constraints = new GridConstraints();
        constraints.setRow(5);
        constraints.setColumn(2);
        constraints.setAnchor(GridConstraints.ANCHOR_EAST);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        panel.add(classArgumentsHintIcon, constraints);

        SwingUtilities.invokeLater(() -> {
            if (!context.getSettingFile().read()) {
                showError("Loading settings failed!");
                return;
            }

            Setting setting = context.getSetting();
            sourceDirectoryTextField.setText(setting.getSourceDirectory());
            templateClassTextField.setText(setting.getTemplateClass());
            packagePatternTextField.setText(setting.getPackagePattern());
            classPatternTextField.setText(setting.getClassPattern());

            String classArguments = Arrays.toString(setting.getClassArguments());
            classArgumentsTextField.setText(classArguments.substring(1, classArguments.length() - 1));
        });

        return panel;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return sourceDirectoryTextField;
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        String sourceDirectory = sourceDirectoryTextField.getText();
        if (!sourceDirectory.isEmpty() && !context.getFile(sourceDirectory).isDirectory()) {
            return new ValidationInfo("Invalid source directory!", sourceDirectoryTextField);
        }

        String templateClass = templateClassTextField.getText();
        if (!templateClass.isEmpty() && !context.getFile(templateClass).isFile()) {
            return new ValidationInfo("Invalid template class!", templateClassTextField);
        }

        String packagePattern = packagePatternTextField.getText();
        if (!packagePattern.isEmpty() && !StringUtils.isPackagePatternValid(packagePattern)) {
            return new ValidationInfo("Invalid package pattern!", packagePatternTextField);
        }

        String classPattern = classPatternTextField.getText();
        if (!classPattern.isEmpty() && !StringUtils.isClassPatternValid(classPattern)) {
            return new ValidationInfo("Invalid class pattern!", classPatternTextField);
        }

        return null;
    }

    @Override
    protected void doOKAction() {
        if (!getOKAction().isEnabled()) {
            return;
        }

        Setting setting = context.getSetting();
        setting.setSourceDirectory(sourceDirectoryTextField.getText());
        setting.setTemplateClass(templateClassTextField.getText());
        setting.setPackagePattern(packagePatternTextField.getText());
        setting.setClassPattern(classPatternTextField.getText());
        setting.setClassArguments(classArgumentsTextField.getText().split(", "));

        if (!context.getSettingFile().write()) {
            showError("Saving settings failed!");
            return;
        }

        close(DialogWrapper.OK_EXIT_CODE);
    }

    private class HintIcon extends JLabel implements MouseMotionListener {

        private final String hint;

        private Balloon balloon;

        public HintIcon(String hint) {
            super(Constants.Icons.Actions.ABOUT);

            this.hint = hint;

            setMinimumSize(new Dimension(25, 25));

            getContentPane().addMouseMotionListener(this);

            hintWindow.addMouseMotionListener(this);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Rectangle rectangle = getBounds();
            rectangle.setLocation(getLocationOnScreen());
            if (rectangle.contains(e.getLocationOnScreen())) {
                showHint();
            } else {
                hideHint();
            }
        }

        private void showHint() {
            if (balloon != null && !balloon.isDisposed()) {
                return;
            }

            IdeTooltipManager ideTooltipManager = IdeTooltipManager.getInstance();

            JLabel label = new JLabel(hint);
            label.setBorder(BorderFactory.createEmptyBorder(1, 3, 2, 3));
            label.setForeground(ideTooltipManager.getTextForeground(true));
            label.setFont(ideTooltipManager.getTextFont(true));

            balloon = JBPopupFactory.getInstance()
                    .createBalloonBuilder(label)
                    .setFillColor(ideTooltipManager.getTextBackground(true))
                    .setAnimationCycle(250)
                    .setHideOnKeyOutside(false)
                    .setHideOnClickOutside(false)
                    .createBalloon();

            Point point = getLocationOnScreen(),
                    offsetPoint = hintWindow.getLocationOnScreen();
            point.x = point.x - offsetPoint.x + getWidth() / 2;
            point.y = point.y - offsetPoint.y + getHeight() / 2;

            balloon.show(new RelativePoint(hintWindow, point), Balloon.Position.atRight);

            ((BalloonImpl) balloon).getComponent().addMouseMotionListener(this);
        }

        private void hideHint() {
            if (balloon == null || balloon.isDisposed()) {
                return;
            }

            balloon.hide(true);
        }

    }

}
