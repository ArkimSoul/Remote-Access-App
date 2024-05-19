package remoteaccessapp.dialogs;

import remoteaccessapp.Instance;
import remoteaccessapp.enums.Language;

import javax.swing.*;
import java.awt.*;

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
    private JTextField a60TextField;
    private JLabel aesRenewalLabel;

    public SettingsFrame(Instance inst) {
        instance = inst;

        setContentPane(contentPane);

        Dimension size_dim = new Dimension(550, 250);

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

        enableAESEncryptionCheckBox.addChangeListener(e -> a60TextField.setEnabled(enableAESEncryptionCheckBox.isSelected()));

        a60TextField.setEnabled(enableAESEncryptionCheckBox.isSelected());
        a60TextField.setText(Integer.toString(instance.settings.getAESKeyRenewalPeriod()));
        deviceNameTextField.setText(instance.settings.getDeviceName());
        languageComboBox.setSelectedItem(instance.settings.getLanguage());
        serverPortTextField.setText(Integer.toString(instance.settings.getServerPort()));
        enableAESEncryptionCheckBox.setSelected(instance.settings.isAESEnabled());
        enableRSAEncryptionCheckBox.setSelected(instance.settings.isRSAEnabled());
    }

    private void saveButton_click() {
        instance.settings.setDeviceName(deviceNameTextField.getText());
        instance.settings.setLanguage((Language) languageComboBox.getSelectedItem());
        instance.settings.setServerPort(Integer.parseInt(serverPortTextField.getText()));
        instance.settings.setAESEnabled(enableAESEncryptionCheckBox.isSelected());
        instance.settings.setRSAEnabled(enableRSAEncryptionCheckBox.isSelected());
        instance.settings.setAESKeyRenewalPeriod(Integer.parseInt(a60TextField.getText()));
    }

    public void updateLanguage() {
        setTitle(instance.bundle.getString("sf.title"));
        deviceNameLabel.setText(instance.bundle.getString("sf.device_name"));
        languageLabel.setText(instance.bundle.getString("sf.language"));
        serverPortLabel.setText(instance.bundle.getString("sf.server_port"));
        enableAESEncryptionCheckBox.setText(instance.bundle.getString("sf.enable_aes"));
        enableRSAEncryptionCheckBox.setText(instance.bundle.getString("sf.enable_rsa"));
        aesRenewalLabel.setText(instance.bundle.getString("sf.aes_renewal"));
        saveButton.setText(instance.bundle.getString("sf.save"));
    }
}
