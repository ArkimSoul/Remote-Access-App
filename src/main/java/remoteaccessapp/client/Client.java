package remoteaccessapp.client;

import remoteaccessapp.server.FrameMessage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class Client {
    private String serverIP = "localhost";
    private int port = 4389;

    private static Socket frame_socket;
    private static Socket sound_socket;
    private static Socket mouse_socket;
    private static Socket keyboard_socket;

    private static ObjectInputStream in_frame;
    private static ObjectInputStream in_sound;
    private static ObjectOutputStream out_mouse;
    private static ObjectOutputStream out_keyboard;

    public static BufferedImage frame;

    public Client(String connection_ip, int connection_port) throws IOException {
        serverIP = connection_ip;
        port = connection_port;

        frame_socket = new Socket(serverIP, port);
        in_frame = new ObjectInputStream(frame_socket.getInputStream());

        sound_socket = new Socket(serverIP, port);
        in_sound = new ObjectInputStream(sound_socket.getInputStream());

        mouse_socket = new Socket(serverIP, port);
        out_mouse = new ObjectOutputStream(mouse_socket.getOutputStream());

        keyboard_socket = new Socket(serverIP, port);
        out_keyboard = new ObjectOutputStream(keyboard_socket.getOutputStream());

        client_lifecycle();
    }

    private void client_lifecycle() {
        /* FRAME THREAD */
        new Thread(() -> {
            while (true) {
                try {
                    FrameMessage frameMessage = (FrameMessage) in_frame.readObject();
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(frameMessage.getImage());
                    frame = ImageIO.read(byteArrayInputStream);
                } catch (Exception e) {
                }
            }
        }).start();

        /* SOUND THREAD */
        new Thread(() -> {
            while (true) {
                try {
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public void sendMouseMessage(MouseMessage mouseMessage) {
        try {
            out_mouse.writeObject(mouseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return frame_socket.isConnected();
    }
}
