package remoteaccessapp.dialogs;

import remoteaccessapp.Instance;
import remoteaccessapp.client.messages.KeyboardMessage;
import remoteaccessapp.client.messages.MouseMessage;
import remoteaccessapp.enums.ConnectionStatus;
import remoteaccessapp.enums.MouseAction;
import remoteaccessapp.utils.Converter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;

public class ClientFrame extends JFrame {
    private static Instance instance;

    private final DefaultMenuBar defaultMenuBar;

    private JPanel contentPane;
    private JLabel screenLabel;
    private JPanel screenPanel;
    private JLabel connectionStatusLabel;

    private float x_mul = 0.0f;
    private float y_mul = 0.0f;

    private ConnectionStatus connectionStatus = ConnectionStatus.DISCONNECTED;
    private int statusTick = 0;

    public ClientFrame(Instance inst) {
        instance = inst;

        setContentPane(contentPane);

        defaultMenuBar = new DefaultMenuBar(instance);
        setJMenuBar(defaultMenuBar);

        Dimension size_dim = new Dimension(640, 480);

        setPreferredSize(size_dim);
        setSize(size_dim);
        setResizable(true);
        setLocationRelativeTo(null);
        setTitle("Remote Access App");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void startSession() {
        setVisible(true);
        setConnectionStatus(ConnectionStatus.CONNECTED);
        instance.executor.submit(() -> {
            screenLabel.addMouseListener(screenMouseListener);
            screenLabel.addMouseMotionListener(screenMotionListener);
            screenLabel.addMouseWheelListener(e -> instance.client.sendMouseMessage(new MouseMessage((int) (e.getX() * x_mul), (int) (e.getY() * y_mul), 0, e.getWheelRotation(), MouseAction.SCROLL)));
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
            while (true) {
                updateScreen();
            }
        });
    }

    private void updateScreen() {
        try {
            ImageIcon scaledFrame = scaleFrame(instance.client.frameBuffer);
            screenLabel.setIcon(scaledFrame);
        } catch (NullPointerException _) {

        }
    }

    private ImageIcon scaleFrame(BufferedImage image) {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();
        double widthRatio = (double) screenPanel.getWidth() / originalWidth;
        double heightRatio = (double) screenPanel.getHeight() / originalHeight;
        double scaleRatio = Math.min(widthRatio, heightRatio);
        int newWidth = (int) (originalWidth * scaleRatio);
        int newHeight = (int) (originalHeight * scaleRatio);

        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        x_mul = originalWidth / (float) newWidth;
        y_mul = originalHeight / (float) newHeight;

        return new ImageIcon(scaledImage);
    }

    private final MouseMotionListener screenMotionListener = new MouseMotionAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            instance.client.sendMouseMessage(new MouseMessage((int) (e.getX() * x_mul), (int) (e.getY() * y_mul), 0, 0, MouseAction.MOVE));
        }
    };

    private final MouseListener screenMouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            instance.client.sendMouseMessage(new MouseMessage((int) (e.getX() * x_mul), (int) (e.getY() * y_mul), Converter.mouseKeyToInputKey(e.getButton()), 0, MouseAction.PRESS));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            instance.client.sendMouseMessage(new MouseMessage((int) (e.getX() * x_mul), (int) (e.getY() * y_mul), Converter.mouseKeyToInputKey(e.getButton()), 0, MouseAction.RELEASE));
        }
    };

    private final KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            instance.client.sendKeyboardMessage(new KeyboardMessage(e.getKeyCode(), e.getID() == KeyEvent.KEY_PRESSED));
            return false;
        }
    };

    public void setConnectionStatus(ConnectionStatus cs) {
        boolean update = true;
        if (connectionStatus == ConnectionStatus.AES_KEY_UPDATING) {
            if (statusTick < 99999999) {
                update = false;
                statusTick++;
            }
            else {
                statusTick = 0;
            }
        }
        if (update) {
            connectionStatus = cs;
            switch (connectionStatus) {
                case CONNECTED -> connectionStatusLabel.setText(instance.bundle.getString("cf.connection_status.connected"));
                case DISCONNECTED -> connectionStatusLabel.setText(instance.bundle.getString("cf.connection_status.disconnected"));
                case AES_KEY_UPDATING -> {
                    connectionStatusLabel.setText(instance.bundle.getString("cf.connection_status.aes_update"));
                }
            }
        }
    }

    public void updateLanguage() {
        setTitle(instance.bundle.getString("cf.title"));
        setConnectionStatus(connectionStatus);
        defaultMenuBar.updateLanguage();
    }
}
