package remoteaccessapp.server;

import remoteaccessapp.Instance;
import remoteaccessapp.client.messages.KeyboardMessage;
import remoteaccessapp.client.messages.MouseMessage;
import remoteaccessapp.enums.MouseAction;
import remoteaccessapp.server.messages.FrameMessage;
import remoteaccessapp.server.messages.AESKeyMessage;
import remoteaccessapp.server.messages.RSAKeyMessage;
import remoteaccessapp.utils.Converter;
import remoteaccessapp.utils.ScreenRecorder;

import javax.crypto.NoSuchPaddingException;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Server {
    private final Instance instance;

    private boolean isRSAEnabled;
    private boolean isAESEnabled;
    private int aesKeyRenewalPeriod;

    private RSAHelper rsaHelper;
    private AESHelper aesHelper;

    private volatile ServerSocket serverSocket;
    private Socket client_socket;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private boolean isClientConnected = false;

    private Robot robot;

    public Server(Instance inst) {
        instance = inst;

        isRSAEnabled = instance.settings.isRSAEnabled();
        isAESEnabled = instance.settings.isAESEnabled();
        aesKeyRenewalPeriod = instance.settings.getAESKeyRenewalPeriod();

        instance.executor.submit(() -> {
            try {
                serverSocket = new ServerSocket(instance.settings.getServerPort());

                client_socket = serverSocket.accept();
                out = new ObjectOutputStream(client_socket.getOutputStream());
                in = new ObjectInputStream(client_socket.getInputStream());

                isClientConnected = true;
                robot = new Robot();

                if (isRSAEnabled) {
                    rsaHelper = new RSAHelper();
                    out.writeObject(new RSAKeyMessage(rsaHelper.encodePublicKey(), rsaHelper.encodePrivateKey()));
                    out.flush();

                    out.writeObject(updateAESKey(true));
                    out.flush();
                    isAESEnabled = true;
                }

                if (isAESEnabled && !isRSAEnabled) {
                    out.writeObject(updateAESKey(false));
                    out.flush();
                }

                server_lifecycle();
            }
            catch (Exception _) {

            }
        });
    }

    private AESKeyMessage updateAESKey(boolean returnRSAEncodedKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        aesHelper = new AESHelper();
        if (returnRSAEncodedKey) {
            byte[] key = aesHelper.encodeKey();
            return new AESKeyMessage(rsaHelper.encrypt(key));
        }
        return new AESKeyMessage(aesHelper.encodeKey());
    }

    private void server_lifecycle() {
        /* OUTPUT THREAD */
        instance.executor.submit(() -> {
            try {
                while (isClientConnected) {
                    byte[] message = (isAESEnabled ? aesHelper.encrypt(ScreenRecorder.getByteFrame()) : ScreenRecorder.getByteFrame());
                    out.writeObject(new FrameMessage(message));
                    out.flush();
                }
            }
            catch (Exception _) {

            }
        });

        /* INPUT THREAD */
        instance.executor.submit(() -> {
            try {
                while (isClientConnected) {
                    Object message = in.readObject();
                    if (message instanceof MouseMessage mouseMessage) {
                        robot.mouseMove(mouseMessage.x(), mouseMessage.y());
                        instance.executor.submit(() -> {
                            switch (mouseMessage.mouseAction()) {
                                case PRESS -> robot.mousePress(mouseMessage.mouseButton());
                                case RELEASE -> robot.mouseRelease(mouseMessage.mouseButton());
                                case SCROLL -> robot.mouseWheel(mouseMessage.wheelRotation());
                            }
                        });
                    } else if (message instanceof KeyboardMessage keyMessage) {
                        instance.executor.submit(() -> {
                           if (keyMessage.isPressed()) {
                               robot.keyPress(keyMessage.keyCode());
                           }
                           else {
                               robot.keyRelease(keyMessage.keyCode());
                           }
                        });
                    }
                }
            }
            catch (Exception _) {

            }
        });
    }

    public String getIPAddress() {
        return "localhost";
    }

    public void close() {
        try {
            isClientConnected = false;
            serverSocket.close();
            client_socket.close();
        }
        catch (Exception _) {

        }
    }

}
