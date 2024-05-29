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

    private final DefaultMenuBar defaultMenuBar;

    private JButton recieveButton;
    private JTextField connectionTextField;
    private JButton connectButton;
    private JLabel serverIPLabel;
    private JLabel serverStatusLabel;
    private JPanel contentPane;
    private JLabel enterIPLabel;
    private JLabel recieveTitleLabel;
    private JLabel connectTitleLabel;

    public MainFrame(Instance inst) {
        instance = inst;

        setContentPane(contentPane);

        defaultMenuBar = new DefaultMenuBar(instance);
        setJMenuBar(defaultMenuBar);

        Dimension size_dim = new Dimension(500, 180);

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

    public void recieveButton_click() {
        if (instance.server == null) {
            instance.server = new Server(instance);
            serverStatusLabel.setText(instance.bundle.getString("mf.status") + ": " + (instance.server == null ? instance.bundle.getString("mf.status.disabled") : instance.bundle.getString("mf.status.ready")));
            serverIPLabel.setText(instance.bundle.getString("mf.your_ip") + ": " + (instance.server == null ? "0.0.0.0" : instance.server.getIPAddress()));
            recieveButton.setText(instance.server == null ? instance.bundle.getString("mf.receive.enable") : instance.bundle.getString("mf.receive.disable"));
            //connectButton.setEnabled(false);
        }
        else {
            instance.server.close();
            instance.server = null;
            serverStatusLabel.setText(instance.bundle.getString("mf.status") + ": " + (instance.server == null ? instance.bundle.getString("mf.status.disabled") : instance.bundle.getString("mf.status.ready")));
            serverIPLabel.setText(instance.bundle.getString("mf.your_ip") + ": " + (instance.server == null ? "0.0.0.0" : instance.server.getIPAddress()));
            recieveButton.setText(instance.server == null ? instance.bundle.getString("mf.receive.enable") : instance.bundle.getString("mf.receive.disable"));
            //connectButton.setEnabled(true);
        }
        recieveButton.setEnabled(false);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.schedule(() -> recieveButton.setEnabled(true), 3, TimeUnit.SECONDS);
    }

    private void connectButton_click() {
        if (instance.client == null) {
            try {
                instance.client = new Client(instance, connectionTextField.getText());
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

    public void updateLanguage() {
        setTitle(instance.bundle.getString("mf.title"));
        recieveTitleLabel.setText(instance.bundle.getString("mf.receive_connection"));
        connectTitleLabel.setText(instance.bundle.getString("mf.connect_to_other"));
        serverStatusLabel.setText(instance.bundle.getString("mf.status") + ": " + (instance.server == null ? instance.bundle.getString("mf.status.disabled") : instance.bundle.getString("mf.status.ready")));
        serverIPLabel.setText(instance.bundle.getString("mf.your_ip") + ": " + (instance.server == null ? "0.0.0.0" : instance.server.getIPAddress()));
        recieveButton.setText(instance.server == null ? instance.bundle.getString("mf.receive.enable") : instance.bundle.getString("mf.receive.disable"));
        enterIPLabel.setText(instance.bundle.getString("mf.enter_ip_address") + ": ");
        connectButton.setText(instance.bundle.getString("mf.connect"));
        defaultMenuBar.updateLanguage();
    }
}
