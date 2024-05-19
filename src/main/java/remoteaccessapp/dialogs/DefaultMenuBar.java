package remoteaccessapp.dialogs;

import remoteaccessapp.Instance;

import javax.swing.*;

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
    }

    public void updateLanguage() {
        appMenu.setText(instance.bundle.getString("dmb.app"));
        settingsMenuItem.setText(instance.bundle.getString("dmb.app.settings"));
        exitMenuItem.setText(instance.bundle.getString("dmb.app.exit"));
        helpMenu.setText(instance.bundle.getString("dmb.help"));
        aboutMenuItem.setText(instance.bundle.getString("dmb.help.about"));
    }
}
