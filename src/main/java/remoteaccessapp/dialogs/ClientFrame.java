package remoteaccessapp.dialogs;

import remoteaccessapp.Instance;
import remoteaccessapp.client.messages.KeyboardMessage;
import remoteaccessapp.client.messages.MouseMessage;
import remoteaccessapp.utils.Converter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ClientFrame extends JFrame {
    private static Instance instance;

    private final DefaultMenuBar defaultMenuBar;

    private JPanel contentPane;
    private JLabel screenLabel;
    private JPanel screenPanel;
    private JLabel connectionStatusLabel;

    private float x_mul = 0.0f;
    private float y_mul = 0.0f;

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
        new Thread(() -> {
            screenLabel.addMouseListener(screenMouseListener);
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
            while (instance.client.isConnected()) {
                updateScreen();
            }
        }).start();
    }

    private void updateScreen() {
        try {
            ImageIcon scaledFrame = scaleFrame(instance.client.frameBuffer);
            screenLabel.setIcon(scaledFrame);
        } catch (NullPointerException e) {

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

    private MouseListener screenMouseListener = new MouseAdapter() {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            instance.client.sendMouseMessage(new MouseMessage(e.getWheelRotation(), true));
        }
        @Override
        public void mousePressed(MouseEvent e) {
            instance.client.sendMouseMessage(new MouseMessage((int) (e.getX() * x_mul), (int) (e.getY() * y_mul), Converter.mouseKeyToInputKey(e.getButton()), true));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            instance.client.sendMouseMessage(new MouseMessage((int) (e.getX() * x_mul), (int) (e.getY() * y_mul), Converter.mouseKeyToInputKey(e.getButton()), false));
        }
    };

    private KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            instance.client.sendKeyMessage(new KeyboardMessage(e.getKeyCode(), e.getID() == KeyEvent.KEY_PRESSED));
            return false;
        }
    };

    public void updateLanguage() {
        setTitle(instance.bundle.getString("cf.title"));
        connectionStatusLabel.setText(instance.bundle.getString("cf.connected"));
        defaultMenuBar.updateLanguage();
    }
}
