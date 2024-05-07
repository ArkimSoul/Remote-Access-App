package remoteaccessapp.client;

import remoteaccessapp.Instance;
import remoteaccessapp.server.FrameMessage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class Client {
    private Instance instance;

    private String serverIP = "localhost";
    private int port = 4389;

    private static Socket socket;

    private static ObjectInputStream in;
    private static ObjectOutputStream out;

    public static BufferedImage frame;

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
            while (true) {
                try {
                    FrameMessage frameMessage = (FrameMessage) in.readObject();
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(frameMessage.getImage());
                    frame = ImageIO.read(byteArrayInputStream);
                } catch (Exception e) {

                }
            }
        });
    }

    public void sendMouseMessage(MouseMessage mouseMessage) {
        try {
            out.writeObject(mouseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendKeyMessage(KeyMessage keyMessage) {
        try {
            out.writeObject(keyMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socket.isConnected();
    }
}
