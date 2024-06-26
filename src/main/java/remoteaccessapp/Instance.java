package remoteaccessapp;

import remoteaccessapp.dialogs.ClientFrame;
import remoteaccessapp.dialogs.MainFrame;
import remoteaccessapp.client.Client;
import remoteaccessapp.dialogs.SettingsFrame;
import remoteaccessapp.server.Server;
import remoteaccessapp.utils.Settings;

import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Instance {
    public ResourceBundle bundle = ResourceBundle.getBundle("language.en_US");

    public Settings settings = new Settings(this);
    public ScheduledExecutorService executor = Executors.newScheduledThreadPool(6);

    public MainFrame mainFrame = new MainFrame(this);
    public ClientFrame clientFrame = new ClientFrame(this);
    public SettingsFrame settingsFrame = new SettingsFrame(this);

    public Server server;
    public Client client;

    public Instance() {
        mainFrame.setVisible(true);
        updateLanguage();
    }

    public void updateLanguage() {
        bundle = ResourceBundle.getBundle("language." + settings.getLanguage().getCode());
        mainFrame.updateLanguage();
        clientFrame.updateLanguage();
        settingsFrame.updateLanguage();
    }

}
