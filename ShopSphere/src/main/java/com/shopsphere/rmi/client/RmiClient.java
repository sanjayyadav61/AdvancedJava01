package com.shopsphere.rmi.client;

import com.shopsphere.model.Product;
import com.shopsphere.rmi.remote.InventoryService;
import com.shopsphere.rmi.server.RmiServer;
import com.shopsphere.util.CurrencyFormatter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;

/**
 * Interactive Client for Java RMI Remote Inventory Service.
 * Demonstrates: java.rmi.Naming.lookup(), Remote method invocations, Exception handling.
 */
public class RmiClient extends JFrame {

    private final String rmiUrl;
    private InventoryService remoteService;

    private DefaultTableModel tableModel;
    private JTable productTable;
    private JTextArea logArea;
    private JTextField urlField;
    private JTextField productIdField;
    private JSpinner deltaSpinner;
    private JLabel statusLabel;

    public RmiClient(String rmiUrl) {
        this.rmiUrl = rmiUrl;

        setTitle("ShopSphere Java RMI Client (Remote Method Invocation Demonstration)");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        lookupService();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Registry Lookup Bar
        JPanel lookupBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        lookupBar.setBorder(BorderFactory.createTitledBorder("RMI Registry Lookup (Naming.lookup)"));

        urlField = new JTextField(rmiUrl, 25);
        JButton connectBtn = new JButton("Lookup Service");
        statusLabel = new JLabel("Status: Disconnected");
        statusLabel.setForeground(Color.RED);

        connectBtn.addActionListener(e -> lookupService());

        lookupBar.add(new JLabel("RMI URL:"));
        lookupBar.add(urlField);
        lookupBar.add(connectBtn);
        lookupBar.add(Box.createHorizontalStrut(15));
        lookupBar.add(statusLabel);

        // Remote Invocations Toolbar
        JPanel cmdBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        cmdBar.setBorder(BorderFactory.createTitledBorder("Remote Method Invocations"));

        JButton getAllBtn = new JButton("getAllProducts()");
        getAllBtn.addActionListener(e -> callGetAll());

        productIdField = new JTextField("1", 4);
        JButton getProductBtn = new JButton("getProduct(id)");
        getProductBtn.addActionListener(e -> callGetProduct());

        JButton getStockBtn = new JButton("getStock(id)");
        getStockBtn.addActionListener(e -> callGetStock());

        deltaSpinner = new JSpinner(new SpinnerNumberModel(5, -100, 100, 1));
        JButton updateStockBtn = new JButton("updateStock(id, qty)");
        updateStockBtn.addActionListener(e -> callUpdateStock());

        JButton statusBtn = new JButton("getServerStatus()");
        statusBtn.addActionListener(e -> callGetStatus());

        cmdBar.add(getAllBtn);
        cmdBar.add(new JSeparator(SwingConstants.VERTICAL));
        cmdBar.add(new JLabel("Product ID:"));
        cmdBar.add(productIdField);
        cmdBar.add(getProductBtn);
        cmdBar.add(getStockBtn);
        cmdBar.add(new JSeparator(SwingConstants.VERTICAL));
        cmdBar.add(new JLabel("Qty Delta:"));
        cmdBar.add(deltaSpinner);
        cmdBar.add(updateStockBtn);
        cmdBar.add(statusBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(lookupBar, BorderLayout.NORTH);
        topPanel.add(cmdBar, BorderLayout.SOUTH);

        // Split view: Table on top, invocation log on bottom
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

    private void lookupService() {
        String url = urlField.getText().trim();
        try {
            log("Looking up Remote Service via: " + url + "...");
            remoteService = (InventoryService) Naming.lookup(url);
            statusLabel.setText("Status: CONNECTED (Remote Stub Acquired)");
            statusLabel.setForeground(new Color(40, 160, 80));
            log("Success! Remote reference obtained: " + remoteService.getClass().getName());
        } catch (Exception ex) {
            statusLabel.setText("Status: Lookup Failed");
            statusLabel.setForeground(Color.RED);
            log("Lookup Error: " + ex.getMessage());
        }
    }

    private void callGetStatus() {
        if (!ensureConnected()) return;
        try {
            log("--> Calling remote method: getServerStatus()...");
            String status = remoteService.getServerStatus();
            log("<-- Remote Response: " + status);
            JOptionPane.showMessageDialog(this, status, "Remote Server Status", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            log("Remote Call Error: " + ex.getMessage());
        }
    }

    private void callGetAll() {
        if (!ensureConnected()) return;
        try {
            log("--> Calling remote method: getAllProducts()...");
            List<Product> products = remoteService.getAllProducts();
            log("<-- Remote Response: Retrieved " + products.size() + " serialized products.");
            tableModel.setRowCount(0);
            for (Product p : products) {
                tableModel.addRow(new Object[]{
                        p.getProductId(), p.getName(), p.getBrand(),
                        CurrencyFormatter.formatINR(p.getPrice()),
                        p.getDiscount() + "%", p.getStock(),
                        p.isStatus() ? "Active" : "Inactive"
                });
            }
        } catch (Exception ex) {
            log("Remote Call Error: " + ex.getMessage());
        }
    }

    private void callGetProduct() {
        if (!ensureConnected()) return;
        try {
            int id = Integer.parseInt(productIdField.getText().trim());
            log("--> Calling remote method: getProduct(" + id + ")...");
            Product p = remoteService.getProduct(id);
            if (p != null) {
                log("<-- Remote Response: Received product " + p.getName());
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                        p.getProductId(), p.getName(), p.getBrand(),
                        CurrencyFormatter.formatINR(p.getPrice()),
                        p.getDiscount() + "%", p.getStock(),
                        p.isStatus() ? "Active" : "Inactive"
                });
            } else {
                log("<-- Remote Response: Product ID " + id + " not found.");
                JOptionPane.showMessageDialog(this, "Product not found.");
            }
        } catch (Exception ex) {
            log("Remote Call Error: " + ex.getMessage());
        }
    }

    private void callGetStock() {
        if (!ensureConnected()) return;
        try {
            int id = Integer.parseInt(productIdField.getText().trim());
            log("--> Calling remote method: getStock(" + id + ")...");
            int stock = remoteService.getStock(id);
            log("<-- Remote Response: Current Stock = " + stock);
            JOptionPane.showMessageDialog(this, "Current Stock for Product #" + id + ": " + stock + " units",
                    "RMI getStock() Result", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            log("Remote Call Error: " + ex.getMessage());
        }
    }

    private void callUpdateStock() {
        if (!ensureConnected()) return;
        try {
            int id = Integer.parseInt(productIdField.getText().trim());
            int delta = (Integer) deltaSpinner.getValue();
            log("--> Calling remote method: updateStock(" + id + ", " + delta + ")...");
            boolean ok = remoteService.updateStock(id, delta);
            log("<-- Remote Response: updateStock returned " + ok);
            JOptionPane.showMessageDialog(this, "updateStock() returned: " + ok, "Result", JOptionPane.INFORMATION_MESSAGE);
            callGetAll();
        } catch (Exception ex) {
            log("Remote Call Error: " + ex.getMessage());
        }
    }

    private boolean ensureConnected() {
        if (remoteService == null) {
            lookupService();
            if (remoteService == null) {
                JOptionPane.showMessageDialog(this, "Not connected to RMI Service.\nPlease start RmiServer first.", "RMI Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        String defaultUrl = "rmi://localhost:" + RmiServer.RMI_PORT + "/" + RmiServer.SERVICE_NAME;
        SwingUtilities.invokeLater(() -> {
            RmiClient client = new RmiClient(defaultUrl);
            client.setVisible(true);
        });
    }
}
