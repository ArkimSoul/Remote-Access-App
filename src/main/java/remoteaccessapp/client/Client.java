package remoteaccessapp.client;

import remoteaccessapp.Instance;
import remoteaccessapp.RemoteAccessApp;
import remoteaccessapp.client.messages.KeyboardMessage;
import remoteaccessapp.client.messages.MouseMessage;
import remoteaccessapp.server.AESHelper;
import remoteaccessapp.server.messages.FrameMessage;
import remoteaccessapp.server.messages.AESKeyMessage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class Client {
    private Instance instance;

    private AESHelper aesHelper;

    private String serverIP = "localhost";
    private int port = 4389;

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

        try {
            AESKeyMessage aesKeyMessage = (AESKeyMessage) in.readObject();
            aesHelper = new AESHelper(aesKeyMessage.getKey());
        }
        catch (Exception _) {

        }

        client_lifecycle();
    }

    private void client_lifecycle() {
        instance.executor.submit(() -> {
            try {
                while (true) {
                    if (in.readObject() instanceof FrameMessage frameMessage) {
                        byte[] message = frameMessage.getImage();
                        message = aesHelper.decrypt(message);
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message);
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

    public void sendKeyMessage(KeyboardMessage keyboardMessage) {
        try {
            out.writeObject(keyboardMessage);
        } catch (IOException _) {

        }
    }

    public boolean isConnected() {
        return socket.isConnected();
    }
}
