package remoteaccessapp;

import remoteaccessapp.dialogs.ClientDialog;
import remoteaccessapp.dialogs.MainDialog;
import remoteaccessapp.client.Client;
import remoteaccessapp.server.Server;

public class Instance {
    public MainDialog mainDialog = new MainDialog(this);
    public ClientDialog clientDialog = new ClientDialog(this);
    public Server server;
    public Client client;

    public Instance() {
        mainDialog.setVisible(true);
    }

}
