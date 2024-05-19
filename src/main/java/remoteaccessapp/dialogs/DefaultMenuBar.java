package remoteaccessapp.dialogs;

import remoteaccessapp.Instance;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class DefaultMenuBar extends JMenuBar {
    private Instance instance;

    JMenu appMenu = new JMenu();
    JMenuItem settingsMenuItem = new JMenuItem();
    JMenuItem exitMenuItem = new JMenuItem();

    JMenu helpMenu = new JMenu();
    JMenuItem aboutMenuItem = new JMenuItem();

    public DefaultMenuBar(Instance inst) {
        instance = inst;

        add(appMenu);
        appMenu.add(settingsMenuItem);
        appMenu.add(exitMenuItem);

        add(helpMenu);
        helpMenu.add(aboutMenuItem);

        updateLanguage();

        settingsMenuItem.addActionListener(e -> {
            instance.settingsFrame.setVisible(true);
        });

        exitMenuItem.addActionListener(e -> {
            System.exit(0);
        });

        aboutMenuItem.addActionListener(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/ArkimSoul/Remote-Access-App/"));
                } catch (Exception _) {

                }
            }
        });
    }

    public void updateLanguage() {
        appMenu.setText(instance.bundle.getString("dmb.app"));
        settingsMenuItem.setText(instance.bundle.getString("dmb.app.settings"));
        exitMenuItem.setText(instance.bundle.getString("dmb.app.exit"));
        helpMenu.setText(instance.bundle.getString("dmb.help"));
        aboutMenuItem.setText(instance.bundle.getString("dmb.help.about"));
    }
}
