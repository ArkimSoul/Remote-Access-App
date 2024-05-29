package remoteaccessapp.dialogs;

import remoteaccessapp.Instance;
import remoteaccessapp.enums.Language;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SettingsFrame extends JFrame {
    private Instance instance;
    private JPanel contentPane;
    private JTextField deviceNameTextField;
    private JTextField serverPortTextField;
    private JCheckBox enableAESEncryptionCheckBox;
    private JCheckBox enableRSAEncryptionCheckBox;
    private JComboBox languageComboBox;
    private JButton saveButton;
    private JLabel deviceNameLabel;
    private JLabel languageLabel;
    private JLabel serverPortLabel;
    private JTextField aesRenewalTextField;
    private JLabel aesRenewalLabel;
    private JButton restoreButton;

    public SettingsFrame(Instance inst) {
        instance = inst;

        setContentPane(contentPane);

        Dimension size_dim = new Dimension(590, 260);

        setPreferredSize(size_dim);
        setSize(size_dim);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(false);
        setTitle("Settings");

        for (Language language : Language.values()) {
            languageComboBox.addItem(language);
        }

        saveButton.addActionListener(e -> saveButton_click());
        restoreButton.addActionListener(e -> restoreButton_click());

        enableAESEncryptionCheckBox.addChangeListener(e -> {
            aesRenewalTextField.setEnabled(enableAESEncryptionCheckBox.isSelected());
            enableRSAEncryptionCheckBox.setEnabled(enableAESEncryptionCheckBox.isSelected());
            if (!enableAESEncryptionCheckBox.isSelected()) {
                enableRSAEncryptionCheckBox.setSelected(false);
            }
        });

        aesRenewalTextField.setEnabled(enableAESEncryptionCheckBox.isSelected());
        aesRenewalTextField.setText(Integer.toString(instance.settings.getAESKeyRenewalPeriod()));
        deviceNameTextField.setText(instance.settings.getDeviceName());
        languageComboBox.setSelectedItem(instance.settings.getLanguage());
        serverPortTextField.setText(Integer.toString(instance.settings.getServerPort()));
        enableAESEncryptionCheckBox.setSelected(instance.settings.isAESEnabled());
        enableRSAEncryptionCheckBox.setSelected(instance.settings.isRSAEnabled());
    }

    private void restoreButton_click() {
        instance.settings.restoreDefaults();
        deviceNameTextField.setText(instance.settings.getDeviceName());
        languageComboBox.setSelectedItem(instance.settings.getLanguage());
        serverPortTextField.setText(Integer.toString(instance.settings.getServerPort()));
        enableAESEncryptionCheckBox.setSelected(instance.settings.isAESEnabled());
        enableRSAEncryptionCheckBox.setSelected(instance.settings.isRSAEnabled());
        aesRenewalTextField.setText(Integer.toString(instance.settings.getAESKeyRenewalPeriod()));
        instance.updateLanguage();
    }

    private void saveButton_click() {
        instance.settings.setDeviceName(deviceNameTextField.getText());
        instance.settings.setLanguage((Language) Objects.requireNonNull(languageComboBox.getSelectedItem()));
        instance.settings.setServerPort(Integer.parseInt(serverPortTextField.getText()));
        instance.settings.setAESEnabled(enableAESEncryptionCheckBox.isSelected());
        instance.settings.setRSAEnabled(enableRSAEncryptionCheckBox.isSelected());
        instance.settings.setAESKeyRenewalPeriod(Integer.parseInt(aesRenewalTextField.getText()));
    }

    public void updateLanguage() {
        setTitle(instance.bundle.getString("sf.title"));
        deviceNameLabel.setText(instance.bundle.getString("sf.device_name"));
        languageLabel.setText(instance.bundle.getString("sf.language"));
        serverPortLabel.setText(instance.bundle.getString("sf.server_port"));
        serverPortTextField.setToolTipText(instance.bundle.getString("sf.server_port.tooltip"));
        enableAESEncryptionCheckBox.setText(instance.bundle.getString("sf.enable_aes"));
        enableAESEncryptionCheckBox.setToolTipText(instance.bundle.getString("sf.enable_aes.tooltip"));
        enableRSAEncryptionCheckBox.setText(instance.bundle.getString("sf.enable_rsa"));
        enableRSAEncryptionCheckBox.setToolTipText(instance.bundle.getString("sf.enable_rsa.tooltip"));
        aesRenewalLabel.setText(instance.bundle.getString("sf.aes_renewal"));
        aesRenewalTextField.setToolTipText(instance.bundle.getString("sf.aes_renewal.tooltip"));
        saveButton.setText(instance.bundle.getString("sf.save"));
    }
}
