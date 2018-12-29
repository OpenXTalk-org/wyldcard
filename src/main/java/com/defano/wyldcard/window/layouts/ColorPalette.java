package com.defano.wyldcard.window.layouts;

import com.defano.wyldcard.WyldCard;
import com.defano.wyldcard.runtime.context.DefaultToolsManager;
import com.defano.wyldcard.window.WyldCardWindow;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;

public class ColorPalette extends WyldCardWindow<Object> {

    private JPanel panel;
    private JTabbedPane tabs;
    private JPanel foregroundChooserPanel;
    private JPanel backgroundChooserPanel;
    private JPanel lineChooserPanel;
    private final JColorChooser foregroundChooser = new JColorChooser();
    private final JColorChooser backgroundChooser = new JColorChooser();
    private final JColorChooser lineChooser = new JColorChooser();

    public ColorPalette() {
        foregroundChooser.setPreviewPanel(new JPanel());
        backgroundChooser.setPreviewPanel(new JPanel());
        lineChooser.setPreviewPanel(new JPanel());

        for (AbstractColorChooserPanel accp : foregroundChooser.getChooserPanels()) {
            if (!accp.getDisplayName().equals("RGB")) {
                foregroundChooser.removeChooserPanel(accp);
            }
        }

        for (AbstractColorChooserPanel accp : backgroundChooser.getChooserPanels()) {
            if (!accp.getDisplayName().equals("RGB")) {
                backgroundChooser.removeChooserPanel(accp);
            }
        }

        for (AbstractColorChooserPanel accp : lineChooser.getChooserPanels()) {
            if (!accp.getDisplayName().equals("RGB")) {
                lineChooser.removeChooserPanel(accp);
            }
        }

        foregroundChooserPanel.add(foregroundChooser);
        backgroundChooserPanel.add(backgroundChooser);
        lineChooserPanel.add(lineChooser);

        WyldCard.getInstance().getToolsManager().getForegroundColorProvider().subscribe(foregroundChooser::setColor);
        WyldCard.getInstance().getToolsManager().getBackgroundColorProvider().subscribe(backgroundChooser::setColor);
        WyldCard.getInstance().getToolsManager().getLinePaintProvider().subscribe(paint -> lineChooser.setColor((Color) paint));

        foregroundChooser.getSelectionModel().addChangeListener(e -> WyldCard.getInstance().getToolsManager().setForegroundColor(foregroundChooser.getColor()));
        backgroundChooser.getSelectionModel().addChangeListener(e -> WyldCard.getInstance().getToolsManager().setBackgroundColor(backgroundChooser.getColor()));
        lineChooser.getSelectionModel().addChangeListener(e -> WyldCard.getInstance().getToolsManager().setLinePaint(lineChooser.getColor()));
    }

    @Override
    public JPanel getWindowPanel() {
        return panel;
    }

    @Override
    public void bindModel(Object data) {

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabs = new JTabbedPane();
        panel.add(tabs, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        foregroundChooserPanel = new JPanel();
        foregroundChooserPanel.setLayout(new BorderLayout(0, 0));
        tabs.addTab("Foreground Color", foregroundChooserPanel);
        backgroundChooserPanel = new JPanel();
        backgroundChooserPanel.setLayout(new BorderLayout(0, 0));
        tabs.addTab("Background Color", backgroundChooserPanel);
        lineChooserPanel = new JPanel();
        lineChooserPanel.setLayout(new BorderLayout(0, 0));
        tabs.addTab("Line Color", lineChooserPanel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
