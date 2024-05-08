package remoteaccessapp.server;

import remoteaccessapp.Instance;
import remoteaccessapp.client.KeyMessage;
import remoteaccessapp.client.MouseMessage;
import remoteaccessapp.utils.ScreenRecorder;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private Instance instance;

    private int port = 4389;
    private ServerSocket serverSocket;

    private Socket client_socket;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private boolean isClientConnected = false;

    private Robot robot;

    public Server(Instance inst) {
        instance = inst;

        instance.executor.submit(() -> {
            try {
                serverSocket = new ServerSocket(port);

                client_socket = serverSocket.accept();
                out = new ObjectOutputStream(client_socket.getOutputStream());
                in = new ObjectInputStream(client_socket.getInputStream());

                isClientConnected = true;
                robot = new Robot();

                server_lifecycle();
            }
            catch (Exception e) {

            }
        });
    }

    private void server_lifecycle() {
        /* OUTPUT THREAD */
        instance.executor.submit(() -> {
            try {
                while (isClientConnected) {
                    out.writeObject(new FrameMessage(ScreenRecorder.getByteFrame()));
                    out.flush();
                }
            }
            catch (Exception e) {

            }
        });

        /* INPUT THREAD */
        instance.executor.submit(() -> {
            try {
                while (isClientConnected) {
                    Object message = in.readObject();
                    if (message instanceof MouseMessage mouseMessage) {
                        robot.mouseMove(mouseMessage.getX(), mouseMessage.getY());
                        instance.executor.submit(() -> {
                            if (mouseMessage.isWheel()) {
                                robot.mouseWheel(mouseMessage.getWheelRotation());
                            }
                            else if (mouseMessage.isPressed()) {
                                robot.mousePress(mouseMessage.getMouseButton());
                            }
                            else {
                                robot.mouseRelease(mouseMessage.getMouseButton());
                            }
                        });
                    } else if (message instanceof KeyMessage keyMessage) {
                        instance.executor.submit(() -> {
                           if (keyMessage.isPressed()) {
                               robot.keyPress(keyMessage.getKeyCode());
                           }
                           else {
                               robot.keyRelease(keyMessage.getKeyCode());
                           }
                        });
                    }
                }
            }
            catch (Exception e) {

            }
        });
    }

    public void close() {
        try {
            isClientConnected = false;
            serverSocket.close();
            client_socket.close();
        }
        catch (Exception e) {

        }
    }
}
