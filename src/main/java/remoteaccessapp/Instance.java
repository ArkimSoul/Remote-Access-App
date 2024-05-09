package remoteaccessapp;

import com.dosse.upnp.UPnP;
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

        UPnP.openPortTCP(RemoteAccessApp.PORT);
        UPnP.openPortUDP(RemoteAccessApp.PORT);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                UPnP.closePortTCP(RemoteAccessApp.PORT);
                UPnP.closePortUDP(RemoteAccessApp.PORT);
            }
        });
    }

}
