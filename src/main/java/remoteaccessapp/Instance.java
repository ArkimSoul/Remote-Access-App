package remoteaccessapp;

import remoteaccessapp.dialogs.ClientDialog;
import remoteaccessapp.dialogs.MainDialog;
import remoteaccessapp.client.Client;
import remoteaccessapp.server.Server;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Instance {
    public ExecutorService executor = Executors.newCachedThreadPool();

    public MainDialog mainDialog = new MainDialog(this);
    public ClientDialog clientDialog = new ClientDialog(this);

    public Server server;
    public Client client;

    public Instance() {
        mainDialog.setVisible(true);
    }

}
