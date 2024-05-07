package remoteaccessapp.dialogs;

import remoteaccessapp.Instance;
import remoteaccessapp.client.Client;
import remoteaccessapp.server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainFrame extends JFrame {
    private static Instance instance;

    private JButton recieveButton;
    private JPanel rootPanel;
    private JTextField connectionTextField;
    private JButton connectButton;
    private JLabel serverIPLabel;
    private JLabel serverStatusLabel;

    public MainFrame(Instance inst) {
        instance = inst;

        setContentPane(rootPanel);

        Dimension size_dim = new Dimension(400, 150);

        setPreferredSize(size_dim);
        setSize(size_dim);
        setResizable(false);
        setLocationRelativeTo(null);
        setType(Type.NORMAL);
        setTitle("Remote Access App");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        recieveButton.addActionListener(e -> recieveButton_click());
        connectButton.addActionListener(e -> connectButton_click());
    }

    private void recieveButton_click() {
        if (instance.server == null) {
            instance.server = new Server(instance);
            serverStatusLabel.setText("Status: Ready");
            serverIPLabel.setText("Your IP: 127.0.0.1");
            recieveButton.setText("Disable");
        }
        else {
            instance.server.close();
            instance.server = null;
            serverStatusLabel.setText("Status: Disabled");
            serverIPLabel.setText("Your IP: 0.0.0.0");
            recieveButton.setText("Enable");
        }
        recieveButton.setEnabled(false);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.schedule(() -> recieveButton.setEnabled(true), 3, TimeUnit.SECONDS);
    }

    private void connectButton_click() {
        if (instance.client == null) {
            try {
                instance.client = new Client(instance, connectionTextField.getText(), 4389);
            } catch (Exception e) {
                instance.client = null;
                JOptionPane.showMessageDialog(this, "Connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (instance.client != null) {
                setVisible(false);
                instance.clientFrame.startSession();
            }
        }
    }

}
