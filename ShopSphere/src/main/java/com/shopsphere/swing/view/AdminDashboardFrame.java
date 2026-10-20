package com.shopsphere.swing.view;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.Category;
import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.model.Product;
import com.shopsphere.model.User;
import com.shopsphere.util.CurrencyFormatter;
import com.shopsphere.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Main Swing Desktop Administration Application.
 * Demonstrates RTU Swing Syllabus components:
 * JFrame, JPanel, JTabbedPane, JTable, JScrollPane, JButton, JTextField, JComboBox,
 * JOptionPane, JMenuBar, JMenu, JMenuItem, Pluggable Look and Feel.
 */
public class AdminDashboardFrame extends JFrame {

    private final User adminUser;
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final UserDAO userDAO = new UserDAO();

    // Tables & Models
    private DefaultTableModel productTableModel;
    private JTable productTable;
    private DefaultTableModel orderTableModel;
    private JTable orderTable;
    private DefaultTableModel userTableModel;
    private JTable userTable;
    private DefaultTableModel categoryTableModel;
    private JTable categoryTable;

    // Overview Stats Labels
    private JLabel revenueValLabel;
    private JLabel ordersValLabel;
    private JLabel productsValLabel;
    private JLabel customersValLabel;

    public AdminDashboardFrame(User adminUser) {
        this.adminUser = adminUser;
        setTitle("ShopSphere Desktop Management Console - Administrator: " + adminUser.getName());
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initMenuBar();
        initComponents();
        loadAllData();
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem refreshItem = new JMenuItem("Refresh All Data");
        refreshItem.addActionListener(e -> loadAllData());
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(logoutItem);
        fileMenu.add(exitItem);

        // Standalone Management Frames (Section 21 of Guide)
        JMenu windowsMenu = new JMenu("Windows");
        JMenuItem openProductWin = new JMenuItem("Product Management (ProductFrame)");
        openProductWin.addActionListener(e -> new ProductFrame().setVisible(true));
        JMenuItem openCategoryWin = new JMenuItem("Category Management (CategoryFrame)");
        openCategoryWin.addActionListener(e -> new CategoryFrame().setVisible(true));
        JMenuItem openCustomerWin = new JMenuItem("Customer Accounts (CustomerFrame)");
        openCustomerWin.addActionListener(e -> new CustomerFrame().setVisible(true));
        JMenuItem openOrderWin = new JMenuItem("Order Management (OrderFrame)");
        openOrderWin.addActionListener(e -> new OrderFrame().setVisible(true));
        JMenuItem openInventoryWin = new JMenuItem("Inventory & Stock Monitor (InventoryFrame)");
        openInventoryWin.addActionListener(e -> new InventoryFrame().setVisible(true));

        windowsMenu.add(openProductWin);
        windowsMenu.add(openCategoryWin);
        windowsMenu.add(openCustomerWin);
        windowsMenu.add(openOrderWin);
        windowsMenu.add(openInventoryWin);

        // Pluggable Look and Feel Demonstration (RTU Syllabus Topic)
        JMenu lafMenu = new JMenu("Look & Feel");
        JMenuItem systemLaf = new JMenuItem("System Native");
        systemLaf.addActionListener(e -> setLaf(UIManager.getSystemLookAndFeelClassName()));
        JMenuItem crossPlatformLaf = new JMenuItem("Cross-Platform (Metal)");
        crossPlatformLaf.addActionListener(e -> setLaf(UIManager.getCrossPlatformLookAndFeelClassName()));
        JMenuItem nimbusLaf = new JMenuItem("Nimbus");
        nimbusLaf.addActionListener(e -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        setLaf(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ignored) {}
        });
        lafMenu.add(systemLaf);
        lafMenu.add(crossPlatformLaf);
        lafMenu.add(nimbusLaf);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About ShopSphere Swing Admin");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "ShopSphere Desktop Admin Management Module\n" +
                "Architecture: Swing MVC + JDBC DAO + Database Persistence\n" +
                "Active Database: " + DBConnection.getActiveDatabaseInfo() + "\n" +
                "Designed for RTU Advanced Java Capstone Practical Exam.",
                "About ShopSphere Admin", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(windowsMenu);
        menuBar.add(lafMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void setLaf(String lafClassName) {
        try {
            UIManager.setLookAndFeel(lafClassName);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not change Look & Feel: " + ex.getMessage());
        }
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 30, 42));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel titleLabel = new JLabel("SHOPSPHERE ADMIN CONSOLE");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel dbStatusLabel = new JLabel("Database: " + (DBConnection.isUsingFallback() ? "Embedded H2 Mode" : "MySQL 8.0"));
        dbStatusLabel.setForeground(new Color(100, 220, 150));
        dbStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(dbStatusLabel, BorderLayout.EAST);

        // Tabbed Pane for Navigation
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("Dashboard Overview", createOverviewPanel());
        tabbedPane.addTab("Product Catalog", createProductsPanel());
        tabbedPane.addTab("Order Management", createOrdersPanel());
        tabbedPane.addTab("Customers", createCustomersPanel());
        tabbedPane.addTab("Categories", createCategoriesPanel());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Status Bar at bottom
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        JLabel statusText = new JLabel(" Logged in as: " + adminUser.getEmail() + " | Ready");
        statusBar.add(statusText);
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Metric Cards Grid
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 15));

        cardsPanel.add(createMetricCard("TOTAL REVENUE", revenueValLabel = new JLabel("₹0.00"), new Color(40, 167, 69)));
        cardsPanel.add(createMetricCard("TOTAL ORDERS", ordersValLabel = new JLabel("0"), new Color(23, 162, 184)));
        cardsPanel.add(createMetricCard("PRODUCTS", productsValLabel = new JLabel("0"), new Color(255, 193, 7)));
        cardsPanel.add(createMetricCard("CUSTOMERS", customersValLabel = new JLabel("0"), new Color(111, 66, 193)));

        panel.add(cardsPanel, BorderLayout.NORTH);

        // Quick inventory alert box
        JPanel lowStockPanel = new JPanel(new BorderLayout());
        lowStockPanel.setBorder(BorderFactory.createTitledBorder("Inventory Restock Alerts (Stock <= 5)"));

        String[] cols = {"ID", "Product Name", "Brand", "Price", "Current Stock", "Status"};
        DefaultTableModel lowStockModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable lowStockTable = new JTable(lowStockModel);
        lowStockPanel.add(new JScrollPane(lowStockTable), BorderLayout.CENTER);

        panel.add(lowStockPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 224, 230)),
                        BorderFactory.createEmptyBorder(12, 15, 12, 15)
                )
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLbl.setForeground(new Color(110, 120, 130));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(new Color(30, 40, 50));

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> loadProductData(searchField.getText().trim()));

        JButton addBtn = new JButton("+ Add Product");
        addBtn.setBackground(new Color(24, 119, 242));
        addBtn.setForeground(Color.WHITE);
        addBtn.addActionListener(e -> onAddProduct());

        JButton editBtn = new JButton("Edit Selected");
        editBtn.addActionListener(e -> onEditProduct());

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> onDeleteProduct());

        JButton restockBtn = new JButton("Quick Restock (+10)");
        restockBtn.addActionListener(e -> onQuickRestock(10));

        toolbar.add(new JLabel("Search:"));
        toolbar.add(searchField);
        toolbar.add(searchBtn);
        toolbar.add(new JSeparator(SwingConstants.VERTICAL));
        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);
        toolbar.add(restockBtn);

        // Products Table
        String[] columns = {"ID", "Category", "Name", "Brand", "Price", "Discount", "Stock", "Status"};
        productTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        productTable = new JTable(productTableModel);
        productTable.setRowHeight(24);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton viewItemsBtn = new JButton("View Order Items");
        viewItemsBtn.addActionListener(e -> onViewOrderItems());

        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"PLACED", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"});
        JButton updateStatusBtn = new JButton("Update Status");
        updateStatusBtn.addActionListener(e -> onUpdateOrderStatus((String) statusCombo.getSelectedItem()));

        toolbar.add(viewItemsBtn);
        toolbar.add(new JLabel("Change Status to:"));
        toolbar.add(statusCombo);
        toolbar.add(updateStatusBtn);

        String[] columns = {"Order ID", "Customer", "Total Amount", "Discount", "Payment Method", "Payment Status", "Order Status", "Date"};
        orderTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        orderTable = new JTable(orderTableModel);
        orderTable.setRowHeight(24);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(orderTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton toggleStatusBtn = new JButton("Toggle Active / Blocked Status");
        toggleStatusBtn.addActionListener(e -> onToggleUserStatus());
        toolbar.add(toggleStatusBtn);

        String[] columns = {"User ID", "Name", "Email", "Mobile", "Role", "Account Status", "Registered On"};
        userTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        userTable = new JTable(userTableModel);
        userTable.setRowHeight(24);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCategoriesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton addCatBtn = new JButton("+ Add Category");
        addCatBtn.addActionListener(e -> onAddCategory());
        JButton deleteCatBtn = new JButton("Delete Selected");
        deleteCatBtn.addActionListener(e -> onDeleteCategory());

        toolbar.add(addCatBtn);
        toolbar.add(deleteCatBtn);

        String[] columns = {"Category ID", "Name", "Description", "Status"};
        categoryTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        categoryTable = new JTable(categoryTableModel);
        categoryTable.setRowHeight(24);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(categoryTable), BorderLayout.CENTER);
        return panel;
    }

    // Data Loaders
    public void loadAllData() {
        loadMetrics();
        loadProductData("");
        loadOrderData();
        loadUserData();
        loadCategoryData();
    }

    private void loadMetrics() {
        revenueValLabel.setText(CurrencyFormatter.formatINR(orderDAO.getTotalRevenue()));
        ordersValLabel.setText(String.valueOf(orderDAO.countOrders()));
        productsValLabel.setText(String.valueOf(productDAO.countProducts()));
        customersValLabel.setText(String.valueOf(userDAO.countUsers()));
    }

    private void loadProductData(String query) {
        productTableModel.setRowCount(0);
        List<Product> list = query.isEmpty() ? productDAO.findAll() : productDAO.search(query, null, null, null, null);
        for (Product p : list) {
            productTableModel.addRow(new Object[]{
                    p.getProductId(),
                    p.getCategoryName() != null ? p.getCategoryName() : "Category " + p.getCategoryId(),
                    p.getName(),
                    p.getBrand(),
                    CurrencyFormatter.formatINR(p.getPrice()),
                    p.getDiscount() + "%",
                    p.getStock(),
                    p.isStatus() ? "Active" : "Inactive"
            });
        }
    }

    private void loadOrderData() {
        orderTableModel.setRowCount(0);
        List<Order> orders = orderDAO.findAll();
        for (Order o : orders) {
            orderTableModel.addRow(new Object[]{
                    o.getOrderId(),
                    o.getUserName() + " (" + o.getUserEmail() + ")",
                    CurrencyFormatter.formatINR(o.getTotalAmount()),
                    CurrencyFormatter.formatINR(o.getDiscount()),
                    o.getPaymentMethod(),
                    o.getPaymentStatus(),
                    o.getOrderStatus(),
                    o.getCreatedAt()
            });
        }
    }

    private void loadUserData() {
        userTableModel.setRowCount(0);
        List<User> users = userDAO.findAll();
        for (User u : users) {
            userTableModel.addRow(new Object[]{
                    u.getUserId(),
                    u.getName(),
                    u.getEmail(),
                    u.getMobile(),
                    u.getRole(),
                    u.getStatus(),
                    u.getCreatedAt()
            });
        }
    }

    private void loadCategoryData() {
        categoryTableModel.setRowCount(0);
        List<Category> categories = categoryDAO.findAll();
        for (Category c : categories) {
            categoryTableModel.addRow(new Object[]{
                    c.getCategoryId(),
                    c.getName(),
                    c.getDescription(),
                    c.isStatus() ? "Active" : "Inactive"
            });
        }
    }

    // Action Handlers
    private void onAddProduct() {
        List<Category> categories = categoryDAO.findAll();
        ProductDialog dialog = new ProductDialog(this, "Add New Product", null, categories);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            productDAO.create(dialog.getProduct());
            loadAllData();
            JOptionPane.showMessageDialog(this, "Product created successfully!");
        }
    }

    private void onEditProduct() {
        int row = productTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table first.");
            return;
        }
        int productId = (Integer) productTableModel.getValueAt(row, 0);
        Product p = productDAO.findById(productId);
        if (p != null) {
            List<Category> categories = categoryDAO.findAll();
            ProductDialog dialog = new ProductDialog(this, "Edit Product #" + p.getProductId(), p, categories);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                productDAO.update(dialog.getProduct());
                loadAllData();
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
            }
        }
    }

    private void onDeleteProduct() {
        int row = productTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
            return;
        }
        int productId = (Integer) productTableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Product #" + productId + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            productDAO.delete(productId);
            loadAllData();
        }
    }

    private void onQuickRestock(int qty) {
        int row = productTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a product to restock.");
            return;
        }
        int productId = (Integer) productTableModel.getValueAt(row, 0);
        productDAO.updateStock(productId, qty);
        loadAllData();
        JOptionPane.showMessageDialog(this, "Restocked Product #" + productId + " by +" + qty + " units!");
    }

    private void onViewOrderItems() {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order to view items.");
            return;
        }
        int orderId = (Integer) orderTableModel.getValueAt(row, 0);
        List<OrderItem> items = orderDAO.findItemsByOrderId(orderId);
        StringBuilder sb = new StringBuilder("Order #" + orderId + " Items:\n\n");
        for (OrderItem item : items) {
            sb.append("• ").append(item.getProductName())
              .append(" | Qty: ").append(item.getQuantity())
              .append(" | Price: ").append(CurrencyFormatter.formatINR(item.getPrice()))
              .append(" | Subtotal: ").append(CurrencyFormatter.formatINR(item.getSubtotal()))
              .append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Order Items", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onUpdateOrderStatus(String newStatus) {
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order to update status.");
            return;
        }
        int orderId = (Integer) orderTableModel.getValueAt(row, 0);
        orderDAO.updateStatus(orderId, newStatus);
        loadOrderData();
        JOptionPane.showMessageDialog(this, "Order #" + orderId + " status updated to " + newStatus);
    }

    private void onToggleUserStatus() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a customer first.");
            return;
        }
        int userId = (Integer) userTableModel.getValueAt(row, 0);
        String currentStatus = (String) userTableModel.getValueAt(row, 5);
        String newStatus = "ACTIVE".equalsIgnoreCase(currentStatus) ? "BLOCKED" : "ACTIVE";
        userDAO.updateStatus(userId, newStatus);
        loadUserData();
        JOptionPane.showMessageDialog(this, "User #" + userId + " is now " + newStatus);
    }

    private void onAddCategory() {
        String name = JOptionPane.showInputDialog(this, "Enter Category Name:");
        if (name != null && !name.trim().isEmpty()) {
            String desc = JOptionPane.showInputDialog(this, "Enter Category Description:");
            Category c = new Category();
            c.setName(name.trim());
            c.setDescription(desc != null ? desc.trim() : "");
            c.setStatus(true);
            categoryDAO.create(c);
            loadCategoryData();
        }
    }

    private void onDeleteCategory() {
        int row = categoryTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a category to delete.");
            return;
        }
        int catId = (Integer) categoryTableModel.getValueAt(row, 0);
        categoryDAO.delete(catId);
        loadCategoryData();
    }
}
