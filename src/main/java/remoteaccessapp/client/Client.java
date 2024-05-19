package remoteaccessapp.client;

import remoteaccessapp.Instance;
import remoteaccessapp.client.messages.KeyboardMessage;
import remoteaccessapp.client.messages.MouseMessage;
import remoteaccessapp.server.AESHelper;
import remoteaccessapp.server.RSAHelper;
import remoteaccessapp.server.messages.FrameMessage;
import remoteaccessapp.server.messages.AESKeyMessage;
import remoteaccessapp.server.messages.RSAKeyMessage;
import remoteaccessapp.utils.Converter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class Client {
    private final Instance instance;

    private boolean isRSAEnabled = false;
    private boolean isAESEnabled = false;

    private AESHelper aesHelper;
    private RSAHelper rsaHelper;

    private String serverIP;
    private int port;

    private static Socket socket;

    private static ObjectInputStream in;
    private static ObjectOutputStream out;

    public BufferedImage frameBuffer;

    public Client(Instance inst, String connection_ip, int connection_port) throws IOException {
        instance = inst;

        serverIP = connection_ip;
        port = connection_port;

        socket = new Socket(serverIP, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        try {
            Object object = in.readObject();
            if (object instanceof RSAKeyMessage rsaKeyMessage) {
                rsaHelper = new RSAHelper(rsaKeyMessage.publicKey(), rsaKeyMessage.privateKey());
                isRSAEnabled = true;

                AESKeyMessage aesKeyMessage = (AESKeyMessage) in.readObject();
                aesHelper = new AESHelper(Converter.bytesToString(rsaHelper.decrypt(aesKeyMessage.key())));
                isAESEnabled = true;
            }
            else if (object instanceof AESKeyMessage aesKeyMessage) {
                aesHelper = new AESHelper(Converter.bytesToString(aesKeyMessage.key()));
                isAESEnabled = true;
            }
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
                        byte[] message = (isAESEnabled ? aesHelper.decrypt(frameMessage.image()) : frameMessage.image());
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
