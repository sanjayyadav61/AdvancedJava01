package com.shopsphere.network.client;

import com.shopsphere.model.Product;
import com.shopsphere.network.protocol.NetworkMessage;
import com.shopsphere.network.server.InventoryServer;
import com.shopsphere.util.CurrencyFormatter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Interactive Client for java.net Socket Server.
 * Demonstrates: Socket, ObjectOutputStream, ObjectInputStream, Serialization.
 */
public class InventoryClient extends JFrame {

    private final String host;
    private final int port;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private DefaultTableModel tableModel;
    private JTable productTable;
    private JTextArea logArea;
    private JTextField hostField;
    private JTextField portField;
    private JTextField searchField;
    private JTextField productIdField;
    private JSpinner stockDeltaSpinner;
    private JLabel statusLabel;

    public InventoryClient(String host, int port) {
        this.host = host;
        this.port = port;

        setTitle("ShopSphere Socket Inventory Client (java.net Demonstration)");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Connection Bar
        JPanel connBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        connBar.setBorder(BorderFactory.createTitledBorder("Server Connection (java.net.Socket)"));

        hostField = new JTextField(host, 12);
        portField = new JTextField(String.valueOf(port), 5);
        JButton connectBtn = new JButton("Connect");
        JButton disconnectBtn = new JButton("Disconnect");
        statusLabel = new JLabel("Status: Disconnected");
        statusLabel.setForeground(Color.RED);

        connectBtn.addActionListener(e -> connectToServer());
        disconnectBtn.addActionListener(e -> disconnectFromServer());

        connBar.add(new JLabel("Host:"));
        connBar.add(hostField);
        connBar.add(new JLabel("Port:"));
        connBar.add(portField);
        connBar.add(connectBtn);
        connBar.add(disconnectBtn);
        connBar.add(Box.createHorizontalStrut(15));
        connBar.add(statusLabel);

        // Commands Toolbar
        JPanel cmdBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        cmdBar.setBorder(BorderFactory.createTitledBorder("Socket Commands (Object Streams)"));

        JButton getAllBtn = new JButton("Fetch All Products (GET_ALL)");
        getAllBtn.addActionListener(e -> sendGetAll());

        searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search (SEARCH)");
        searchBtn.addActionListener(e -> sendSearch());

        productIdField = new JTextField("1", 4);
        JButton getProductBtn = new JButton("Get Product (GET_PRODUCT)");
        getProductBtn.addActionListener(e -> sendGetProduct());

        stockDeltaSpinner = new JSpinner(new SpinnerNumberModel(5, -100, 100, 1));
        JButton updateStockBtn = new JButton("Update Stock (UPDATE_STOCK)");
        updateStockBtn.addActionListener(e -> sendUpdateStock());

        JButton pingBtn = new JButton("Ping Server");
        pingBtn.addActionListener(e -> sendPing());

        cmdBar.add(getAllBtn);
        cmdBar.add(new JSeparator(SwingConstants.VERTICAL));
        cmdBar.add(new JLabel("Keyword:"));
        cmdBar.add(searchField);
        cmdBar.add(searchBtn);
        cmdBar.add(new JSeparator(SwingConstants.VERTICAL));
        cmdBar.add(new JLabel("Product ID:"));
        cmdBar.add(productIdField);
        cmdBar.add(getProductBtn);
        cmdBar.add(new JLabel("Delta:"));
        cmdBar.add(stockDeltaSpinner);
        cmdBar.add(updateStockBtn);
        cmdBar.add(pingBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(connBar, BorderLayout.NORTH);
        topPanel.add(cmdBar, BorderLayout.SOUTH);

        // Center Split Pane: Table on top, Log stream on bottom
        String[] cols = {"Product ID", "Name", "Brand", "Price", "Discount", "Stock", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        productTable = new JTable(tableModel);
        productTable.setRowHeight(22);

        logArea = new JTextArea(8, 60);
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setBackground(new Color(245, 247, 250));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(productTable),
                new JScrollPane(logArea));
        splitPane.setResizeWeight(0.65);

        main.add(topPanel, BorderLayout.NORTH);
        main.add(splitPane, BorderLayout.CENTER);

        getContentPane().add(main);
    }

    private synchronized void connectToServer() {
        try {
            String h = hostField.getText().trim();
            int p = Integer.parseInt(portField.getText().trim());

            log("Opening java.net.Socket to " + h + ":" + p + "...");
            socket = new Socket(h, p);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            statusLabel.setText("Status: CONNECTED to " + h + ":" + p);
            statusLabel.setForeground(new Color(40, 160, 80));
            log("Connected successfully! Ready to serialize objects over network.");
        } catch (Exception ex) {
            statusLabel.setText("Status: Connection Failed");
            statusLabel.setForeground(Color.RED);
            log("Connection Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Could not connect to Inventory Server: " + ex.getMessage() +
                    "\nPlease ensure InventoryServer is running on port " + portField.getText(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private synchronized void disconnectFromServer() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            statusLabel.setText("Status: Disconnected");
            statusLabel.setForeground(Color.RED);
            log("Disconnected from server.");
        } catch (Exception ex) {
            log("Error closing socket: " + ex.getMessage());
        }
    }

    private synchronized NetworkMessage sendCommand(NetworkMessage cmd) {
        if (socket == null || socket.isClosed()) {
            connectToServer();
            if (socket == null || socket.isClosed()) {
                return null;
            }
        }
        try {
            log("--> Sending Command: " + cmd.getType() + " via ObjectOutputStream");
            out.writeObject(cmd);
            out.flush();

            Object resp = in.readObject();
            if (resp instanceof NetworkMessage) {
                NetworkMessage response = (NetworkMessage) resp;
                log("<-- Received Response: " + response.getType() + " | " + response.getMessage());
                return response;
            }
        } catch (Exception ex) {
            log("Communication Error: " + ex.getMessage());
            disconnectFromServer();
        }
        return null;
    }

    private void sendPing() {
        NetworkMessage req = new NetworkMessage(NetworkMessage.Type.PING);
        NetworkMessage resp = sendCommand(req);
        if (resp != null) {
            JOptionPane.showMessageDialog(this, resp.getMessage(), "Ping Response", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void sendGetAll() {
        NetworkMessage req = new NetworkMessage(NetworkMessage.Type.GET_ALL);
        NetworkMessage resp = sendCommand(req);
        if (resp != null && resp.getPayload() instanceof List) {
            populateTable((List<Product>) resp.getPayload());
        }
    }

    @SuppressWarnings("unchecked")
    private void sendSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            sendGetAll();
            return;
        }
        NetworkMessage req = new NetworkMessage(NetworkMessage.Type.SEARCH);
        req.setQuery(query);
        NetworkMessage resp = sendCommand(req);
        if (resp != null && resp.getPayload() instanceof List) {
            populateTable((List<Product>) resp.getPayload());
        }
    }

    private void sendGetProduct() {
        try {
            int id = Integer.parseInt(productIdField.getText().trim());
            NetworkMessage req = new NetworkMessage(NetworkMessage.Type.GET_PRODUCT);
            req.setProductId(id);
            NetworkMessage resp = sendCommand(req);
            if (resp != null && resp.getPayload() instanceof Product) {
                Product p = (Product) resp.getPayload();
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                        p.getProductId(), p.getName(), p.getBrand(),
                        CurrencyFormatter.formatINR(p.getPrice()),
                        p.getDiscount() + "%", p.getStock(),
                        p.isStatus() ? "Active" : "Inactive"
                });
            } else if (resp != null) {
                JOptionPane.showMessageDialog(this, resp.getMessage(), "Result", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric Product ID.");
        }
    }

    private void sendUpdateStock() {
        try {
            int id = Integer.parseInt(productIdField.getText().trim());
            int delta = (Integer) stockDeltaSpinner.getValue();
            NetworkMessage req = new NetworkMessage(NetworkMessage.Type.UPDATE_STOCK);
            req.setProductId(id);
            req.setQuantity(delta);
            NetworkMessage resp = sendCommand(req);
            if (resp != null) {
                JOptionPane.showMessageDialog(this, resp.getMessage(), "Stock Update Result", JOptionPane.INFORMATION_MESSAGE);
                sendGetAll();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric Product ID.");
        }
    }

    private void populateTable(List<Product> list) {
        tableModel.setRowCount(0);
        for (Product p : list) {
            tableModel.addRow(new Object[]{
                    p.getProductId(),
                    p.getName(),
                    p.getBrand(),
                    CurrencyFormatter.formatINR(p.getPrice()),
                    p.getDiscount() + "%",
                    p.getStock(),
                    p.isStatus() ? "Active" : "Inactive"
            });
        }
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InventoryClient client = new InventoryClient("127.0.0.1", InventoryServer.PORT);
            client.setVisible(true);
        });
    }
}
