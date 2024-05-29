package remoteaccessapp.client;

import remoteaccessapp.Instance;
import remoteaccessapp.client.messages.ClientInfoMessage;
import remoteaccessapp.client.messages.KeyboardMessage;
import remoteaccessapp.client.messages.MouseMessage;
import remoteaccessapp.enums.ConnectionStatus;
import remoteaccessapp.server.AESHelper;
import remoteaccessapp.server.RSAHelper;
import remoteaccessapp.server.messages.ConfirmMessage;
import remoteaccessapp.server.messages.FrameMessage;
import remoteaccessapp.server.messages.AESKeyMessage;
import remoteaccessapp.server.messages.RSAKeyMessage;
import remoteaccessapp.utils.Converter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class Client {
    private final Instance instance;

    private boolean isCycleEnabled = true;

    private boolean isRSAEnabled = false;
    private boolean isAESEnabled = false;

    private AESHelper aesHelper;
    private RSAHelper rsaHelper;

    private final String serverIP;
    private final int port;

    private static Socket socket;

    private static ObjectInputStream in;
    private static ObjectOutputStream out;

    public BufferedImage frameBuffer;
    private Instant lastRequest;

    public Client(Instance inst, String connection_ip) throws IOException {
        instance = inst;

        String[] ip_parts = connection_ip.split(":");

        serverIP = ip_parts[0];
        port = (ip_parts.length == 2 ? Integer.parseInt(ip_parts[1]) : 4389);

        socket = new Socket(serverIP, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        instance.executor.submit(() -> {
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

                byte[] deviceName = Converter.stringToBytes(inst.settings.getDeviceName());
                if (isAESEnabled)
                    deviceName = aesHelper.encrypt(deviceName);
                ClientInfoMessage clientInfoMessage = new ClientInfoMessage(deviceName);
                out.writeObject(clientInfoMessage);

                ConfirmMessage confirmMessage = (ConfirmMessage) in.readObject();

                if (!confirmMessage.confirm()) {
                    closeCycle();
                }

                client_lifecycle();
            }
            catch (Exception _) {

            }
        });
    }

    private void client_lifecycle() {
        instance.executor.submit(() -> {
            try {
                while (isCycleEnabled) {
                    lastRequest = Instant.now();
                    Object object = in.readObject();
                    if (object instanceof FrameMessage frameMessage) {
                        byte[] message = (isAESEnabled ? aesHelper.decrypt(frameMessage.image()) : frameMessage.image());
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message);
                        frameBuffer = ImageIO.read(byteArrayInputStream);
                    }
                    else if (object instanceof AESKeyMessage aesKeyMessage) {
                        instance.clientFrame.setConnectionStatus(ConnectionStatus.AES_KEY_UPDATING);
                        aesHelper = new AESHelper(isRSAEnabled ? Converter.bytesToString(rsaHelper.decrypt(aesKeyMessage.key())) : Converter.bytesToString(aesKeyMessage.key()));
                    }
                }
            } catch (Exception _) {

            }
        });
        instance.executor.submit(() -> {
            while (isCycleEnabled) {
                if (isConnected()) {
                    instance.clientFrame.setConnectionStatus(ConnectionStatus.CONNECTED);
                }
                else {
                    instance.clientFrame.setConnectionStatus(ConnectionStatus.DISCONNECTED);

                    if (Duration.between(lastRequest, Instant.now()).getSeconds() > 15) {
                        int result = JOptionPane.showConfirmDialog(new JFrame(), "There is no signal from the server. Continue waiting for connection?", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            lastRequest = Instant.now();
                        } else {
                            closeCycle();
                        }
                    }
                }
            }
        });
    }

    private void closeCycle() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        instance.mainFrame.setVisible(true);
        instance.clientFrame.setVisible(false);
        isCycleEnabled = false;
        instance.client = null;
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
        return Duration.between(lastRequest, Instant.now()).getSeconds() <= 5;
    }
}
