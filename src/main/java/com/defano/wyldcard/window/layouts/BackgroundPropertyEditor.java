package com.defano.wyldcard.window.layouts;

import com.defano.wyldcard.aspect.RunOnDispatch;
import com.defano.wyldcard.runtime.context.ExecutionContext;
import com.defano.wyldcard.util.StringUtils;
import com.defano.wyldcard.window.WyldCardDialog;
import com.defano.wyldcard.parts.card.CardPart;
import com.defano.wyldcard.parts.bkgnd.BackgroundModel;
import com.defano.wyldcard.parts.model.PartModel;
import com.defano.hypertalk.ast.model.Owner;
import com.defano.hypertalk.ast.model.PartType;
import com.defano.hypertalk.ast.model.Value;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;

public class BackgroundPropertyEditor extends WyldCardDialog<CardPart> {
    private CardPart cardPart;
    private BackgroundModel backgroundModel;

    private JPanel propertiesPanel;
    private JTextField backgroundName;
    private JLabel fieldCountLabel;
    private JLabel buttonCountLabel;
    private JCheckBox cantDeleteBkgndCheckBox;
    private JButton saveButton;
    private JLabel cardCountLabel;
    private JLabel backgroundIdLabel;
    private JButton scriptButton;
    private JButton contentsButton;
    private JCheckBox dontSearchCheckBox;

    public BackgroundPropertyEditor() {
        saveButton.addActionListener(e -> {
            updateProperties();
            dispose();
        });

        contentsButton.addActionListener(e -> {
            dispose();
            showContentsEditor();
        });

        scriptButton.addActionListener(e -> {
            dispose();
            backgroundModel.editScript(new ExecutionContext());
        });
    }

    @Override
    public JButton getDefaultButton() {
        return saveButton;
    }

    @Override
    public JPanel getWindowPanel() {
        return propertiesPanel;
    }

    @Override
    @RunOnDispatch
    public void bindModel(CardPart cardPart) {
        ExecutionContext context = new ExecutionContext();
        this.cardPart = cardPart;
        backgroundModel = this.cardPart.getCardModel().getBackgroundModel();

        int backgroundId = this.cardPart.getCardModel().getBackgroundId();
        backgroundIdLabel.setText("Background ID: " + backgroundId);
        cantDeleteBkgndCheckBox.setSelected(backgroundModel.getKnownProperty(context, BackgroundModel.PROP_CANTDELETE).booleanValue());
        dontSearchCheckBox.setSelected(backgroundModel.getKnownProperty(context, BackgroundModel.PROP_DONTSEARCH).booleanValue());

        // Don't display "default" name ('background id xxx')
        Value bkgndNameValue = backgroundModel.getRawProperty(BackgroundModel.PROP_NAME);
        if (bkgndNameValue != null && !bkgndNameValue.isEmpty()) {
            backgroundName.setText(backgroundModel.getKnownProperty(context, BackgroundModel.PROP_NAME).toString());
        }

        long cardCount = this.cardPart.getCardModel().getStackModel().getCardsInBackground(backgroundId).size();
        long fieldCount = this.cardPart.getCardModel().getPartCount(context, PartType.FIELD, Owner.BACKGROUND);
        long buttonCount = this.cardPart.getCardModel().getPartCount(context, PartType.BUTTON, Owner.BACKGROUND);

        cardCountLabel.setText(StringUtils.pluralize(cardCount, "Background shared by %d card.", "Background shared by %d cards."));
        buttonCountLabel.setText(StringUtils.pluralize(buttonCount, "Contains %d background button.", "Contains %d background buttons."));
        fieldCountLabel.setText(StringUtils.pluralize(fieldCount, "Contains %d background field.", "Contains %d background fields."));
    }

    private void updateProperties() {
        ExecutionContext context = new ExecutionContext();
        cardPart.getCardModel().getBackgroundModel().setKnownProperty(context, BackgroundModel.PROP_NAME, new Value(backgroundName.getText()));
        cardPart.getCardModel().getBackgroundModel().setKnownProperty(context, BackgroundModel.PROP_CANTDELETE, new Value(cantDeleteBkgndCheckBox.isSelected()));
        cardPart.getCardModel().getBackgroundModel().setKnownProperty(context, BackgroundModel.PROP_DONTSEARCH, new Value(dontSearchCheckBox.isSelected()));
    }

    private void showContentsEditor() {
        ExecutionContext context = new ExecutionContext();
        String contents = PartContentsEditor.editContents(backgroundModel.getKnownProperty(context, PartModel.PROP_CONTENTS).toString(), getWindowPanel());
        if (contents != null) {
            backgroundModel.setKnownProperty(context, PartModel.PROP_CONTENTS, new Value(contents));
        }
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        propertiesPanel = new JPanel();
        propertiesPanel.setLayout(new GridLayoutManager(13, 5, new Insets(10, 10, 10, 10), -1, -1));
        panel1.add(propertiesPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Background Name:");
        propertiesPanel.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backgroundName = new JTextField();
        propertiesPanel.add(backgroundName, new GridConstraints(0, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        backgroundIdLabel = new JLabel();
        backgroundIdLabel.setText("Background ID:");
        propertiesPanel.add(backgroundIdLabel, new GridConstraints(2, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldCountLabel = new JLabel();
        fieldCountLabel.setText("Contains 2 background fields.");
        propertiesPanel.add(fieldCountLabel, new GridConstraints(5, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCountLabel = new JLabel();
        buttonCountLabel.setText("Contains 2 background buttons.");
        propertiesPanel.add(buttonCountLabel, new GridConstraints(6, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cantDeleteBkgndCheckBox = new JCheckBox();
        cantDeleteBkgndCheckBox.setText("Can't Delete Background");
        propertiesPanel.add(cantDeleteBkgndCheckBox, new GridConstraints(8, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("");
        propertiesPanel.add(label2, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("");
        propertiesPanel.add(label3, new GridConstraints(7, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cardCountLabel = new JLabel();
        cardCountLabel.setText("Background shared by 9 cards.");
        propertiesPanel.add(cardCountLabel, new GridConstraints(3, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("");
        propertiesPanel.add(label4, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scriptButton = new JButton();
        scriptButton.setEnabled(true);
        scriptButton.setText("Edit Script...");
        scriptButton.setToolTipText("Not implemented");
        propertiesPanel.add(scriptButton, new GridConstraints(12, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        propertiesPanel.add(spacer1, new GridConstraints(12, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("OK");
        propertiesPanel.add(saveButton, new GridConstraints(12, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contentsButton = new JButton();
        contentsButton.setEnabled(true);
        contentsButton.setText("Contents...");
        contentsButton.setToolTipText("Not implemented");
        propertiesPanel.add(contentsButton, new GridConstraints(11, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        propertiesPanel.add(spacer2, new GridConstraints(12, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("");
        propertiesPanel.add(label5, new GridConstraints(10, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dontSearchCheckBox = new JCheckBox();
        dontSearchCheckBox.setText("Don't Search Background");
        propertiesPanel.add(dontSearchCheckBox, new GridConstraints(9, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }
}
