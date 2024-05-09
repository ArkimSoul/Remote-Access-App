package remoteaccessapp.client;

import remoteaccessapp.Instance;
import remoteaccessapp.RemoteAccessApp;
import remoteaccessapp.server.FrameMessage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class Client {
    private Instance instance;

    private String serverIP = "localhost";
    private int port = RemoteAccessApp.PORT;

    private static Socket socket;

    private static ObjectInputStream in;
    private static ObjectOutputStream out;

    public static BufferedImage frameBuffer;

    public Client(Instance inst, String connection_ip, int connection_port) throws IOException {
        instance = inst;

        serverIP = connection_ip;
        port = connection_port;

        socket = new Socket(serverIP, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        client_lifecycle();
    }

    private void client_lifecycle() {
        instance.executor.submit(() -> {
            try {
                while (true) {
                    Object message = in.readObject();
                    if (message instanceof FrameMessage frameMessage) {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(frameMessage.getImage());
                        frameBuffer = ImageIO.read(byteArrayInputStream);
                    }
                }
            } catch (Exception _) {

            }
        });
    }

    public void sendMouseMessage(MouseMessage mouseMessage) {
        try {
            out.writeObject(mouseMessage);
        } catch (IOException _) {

        }
    }

    public void sendKeyMessage(KeyMessage keyMessage) {
        try {
            out.writeObject(keyMessage);
        } catch (IOException _) {

        }
    }

    public boolean isConnected() {
        return socket.isConnected();
    }
}
