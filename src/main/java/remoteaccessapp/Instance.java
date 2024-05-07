package remoteaccessapp;

import remoteaccessapp.dialogs.ClientFrame;
import remoteaccessapp.dialogs.MainFrame;
import remoteaccessapp.client.Client;
import remoteaccessapp.server.Server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Instance {
    public ExecutorService executor = Executors.newCachedThreadPool();

    public MainFrame mainFrame = new MainFrame(this);
    public ClientFrame clientFrame = new ClientFrame(this);

    public Server server;
    public Client client;

    public Instance() {
        mainFrame.setVisible(true);
    }

}
