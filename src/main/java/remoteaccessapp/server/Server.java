package remoteaccessapp.server;

import remoteaccessapp.client.MouseMessage;
import remoteaccessapp.utils.ScreenRecorder;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port = 4389;
    private ServerSocket serverSocket;

    private Socket frame_socket;
    private Socket sound_socket;
    private Socket mouse_socket;
    private Socket keyboard_socket;

    private ObjectOutputStream out_frame;
    private ObjectOutputStream out_sound;
    private ObjectInputStream in_mouse;
    private ObjectInputStream in_keyboard;

    private boolean isClientConnected = false;

    public Server() {
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                frame_socket = serverSocket.accept();
                out_frame = new ObjectOutputStream(frame_socket.getOutputStream());

                sound_socket = serverSocket.accept();
                out_sound = new ObjectOutputStream(sound_socket.getOutputStream());

                mouse_socket = serverSocket.accept();
                in_mouse = new ObjectInputStream(mouse_socket.getInputStream());

                keyboard_socket = serverSocket.accept();
                in_keyboard = new ObjectInputStream(keyboard_socket.getInputStream());

                isClientConnected = true;
                server_lifecycle();
            }
            catch (Exception e) {

            }
        }).start();
    }

    private void server_lifecycle() {
        /* FRAME THREAD */
        new Thread(() -> {
            try {
                while (isClientConnected) {
                    out_frame.writeObject(new FrameMessage(ScreenRecorder.getByteFrame()));
                    out_frame.flush();
                }
            }
            catch (Exception e) {
            }
        }).start();

        /* SOUND THREAD */
        new Thread(() -> {
            try {
                while (isClientConnected) {
                }
            }
            catch (Exception e) {
            }
        }).start();

        /* MOUSE THREAD */
        new Thread(() -> {
            try {
                Robot robot = new Robot();
                while (isClientConnected) {
                    MouseMessage mouseMessage = (MouseMessage) in_mouse.readObject();
                    robot.mouseMove(mouseMessage.getX(), mouseMessage.getY());
                    if (mouseMessage.isPressed()) {
                        robot.mousePress(mouseMessage.getMouseButton());
                    }
                    else {
                        robot.mouseRelease(mouseMessage.getMouseButton());
                    }
                }
            }
            catch (Exception e) {
            }
        }).start();

        /* KEYBOARD THREAD */
        new Thread(() -> {
            try {
                while (isClientConnected) {
                }
            }
            catch (Exception e) {
            }
        }).start();
    }

    public void close() {
        try {
            isClientConnected = false;
            serverSocket.close();
            frame_socket.close();
            sound_socket.close();
            mouse_socket.close();
            keyboard_socket.close();
        }
        catch (Exception e) {

        }
    }
}
