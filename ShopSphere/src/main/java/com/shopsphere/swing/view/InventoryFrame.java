package com.shopsphere.swing.view;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * RTU Swing MVC View: InventoryFrame
 * Standalone Inventory & Stock Management Frame fulfilling Section 21 of the project guide.
 * Demonstrates: JFrame, JTable, TableCellRenderer for low-stock alerts, JSpinner, JButton.
 */
public class InventoryFrame extends JFrame {

    private final ProductDAO productDAO = new ProductDAO();
    private DefaultTableModel tableModel;
    private JTable inventoryTable;
    private JLabel lowStockCountLabel;

    public InventoryFrame() {
        setTitle("ShopSphere - Inventory & Low-Stock Monitor (Swing MVC View)");
        setSize(880, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadInventory();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Header with metrics
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Warehouse Inventory Control & Low-Stock Alerts", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        lowStockCountLabel = new JLabel("Low Stock Items: 0", SwingConstants.RIGHT);
        lowStockCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lowStockCountLabel.setForeground(new Color(200, 50, 50));

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(lowStockCountLabel, BorderLayout.EAST);
        main.add(headerPanel, BorderLayout.NORTH);

        // Inventory Table
        String[] columns = {"Product ID", "Product Name", "Brand", "Category", "Current Stock", "Status", "Low Stock Alert"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        inventoryTable = new JTable(tableModel);
        inventoryTable.setRowHeight(26);

        // Highlight low stock rows in red
        inventoryTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (value instanceof Integer) {
                    int stock = (int) value;
                    if (stock <= 5) {
                        c.setForeground(Color.RED);
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        });

        main.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        // Bottom Actions
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton adjustStockBtn = new JButton("Quick Stock Adjustment (+/-)");
        JButton refreshBtn = new JButton("Refresh Inventory");
        JButton closeBtn = new JButton("Close");

        bottomBar.add(adjustStockBtn);
        bottomBar.add(refreshBtn);
        bottomBar.add(closeBtn);
        main.add(bottomBar, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadInventory());
        closeBtn.addActionListener(e -> dispose());

        adjustStockBtn.addActionListener(e -> {
            int row = inventoryTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a product row to adjust stock.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int productId = (int) tableModel.getValueAt(row, 0);
            String name = (String) tableModel.getValueAt(row, 1);
            int currentStock = (int) tableModel.getValueAt(row, 4);

            String deltaStr = JOptionPane.showInputDialog(this,
                    "Adjust stock for '" + name + "' (Current: " + currentStock + ")\nEnter units to add (or negative to deduct):",
                    "Stock Adjustment", JOptionPane.QUESTION_MESSAGE);

            if (deltaStr != null && !deltaStr.trim().isEmpty()) {
                try {
                    int delta = Integer.parseInt(deltaStr.trim());
                    int newStock = Math.max(0, currentStock + delta);
                    if (productDAO.updateStock(productId, newStock)) {
                        JOptionPane.showMessageDialog(this, "Stock for '" + name + "' updated to " + newStock + " units!");
                        loadInventory();
                    } else {
                        JOptionPane.showMessageDialog(this, "Database update failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid integer quantity.", "Invalid Number", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        getContentPane().add(main);
    }

    private void loadInventory() {
        tableModel.setRowCount(0);
        List<Product> products = productDAO.findAll();
        int lowStockCount = 0;

        for (Product p : products) {
            boolean isLow = p.getStock() <= 5;
            if (isLow) lowStockCount++;
            tableModel.addRow(new Object[]{
                    p.getProductId(),
                    p.getName(),
                    p.getBrand(),
                    p.getCategoryName() != null ? p.getCategoryName() : "General",
                    p.getStock(),
                    p.isStatus() ? "Active" : "Inactive",
                    isLow ? "⚠️ LOW STOCK" : "NORMAL"
            });
        }
        lowStockCountLabel.setText("Low Stock Items: " + lowStockCount);
    }
}
