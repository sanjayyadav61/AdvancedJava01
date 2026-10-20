package com.shopsphere.swing.view;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.swing.controller.ProductController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * RTU Swing MVC View: ProductFrame
 * Standalone product management frame fulfilling Section 21 of the project guide.
 * Demonstrates: JFrame, JPanel, JTable, JScrollPane, JTextField, JComboBox,
 * JRadioButton, JCheckBox, JButton, FlowLayout, BorderLayout, MVC pattern.
 */
public class ProductFrame extends JFrame {

    private final ProductController controller = new ProductController();
    private DefaultTableModel tableModel;
    private JTable productTable;
    private JTextField searchField;
    private JComboBox<String> categoryComboBox;
    private JCheckBox inStockOnlyCheckBox;
    private JRadioButton allStatusRadio;
    private JRadioButton activeOnlyRadio;

    public ProductFrame() {
        setTitle("ShopSphere - Product Catalog Management (Swing MVC View)");
        setSize(980, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadProducts();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Top Control Panel (Search, Filter, Radios, Checkbox)
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter Controls (RTU Swing Components)"));

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchRow.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        JButton resetBtn = new JButton("Reset");
        searchRow.add(searchField);
        searchRow.add(searchBtn);
        searchRow.add(resetBtn);

        searchRow.add(new JSeparator(SwingConstants.VERTICAL));

        searchRow.add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("All Categories");
        List<Category> categories = controller.getAllCategories();
        for (Category c : categories) {
            categoryComboBox.addItem(c.getName());
        }
        searchRow.add(categoryComboBox);

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        inStockOnlyCheckBox = new JCheckBox("In Stock Only (> 0)");
        filterRow.add(inStockOnlyCheckBox);

        allStatusRadio = new JRadioButton("All Statuses", true);
        activeOnlyRadio = new JRadioButton("Active Only");
        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(allStatusRadio);
        statusGroup.add(activeOnlyRadio);
        filterRow.add(new JLabel("| Status:"));
        filterRow.add(allStatusRadio);
        filterRow.add(activeOnlyRadio);

        topPanel.add(searchRow, BorderLayout.NORTH);
        topPanel.add(filterRow, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center Table
        String[] columns = {"ID", "Product Name", "Brand", "Price", "Discount", "Stock", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setRowHeight(26);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(productTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Action Bar
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton addBtn = new JButton("Add New Product");
        JButton editBtn = new JButton("Edit Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton refreshBtn = new JButton("Refresh");
        JButton closeBtn = new JButton("Close");

        bottomBar.add(addBtn);
        bottomBar.add(editBtn);
        bottomBar.add(deleteBtn);
        bottomBar.add(refreshBtn);
        bottomBar.add(closeBtn);
        mainPanel.add(bottomBar, BorderLayout.SOUTH);

        // Listeners
        searchBtn.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            categoryComboBox.setSelectedIndex(0);
            inStockOnlyCheckBox.setSelected(false);
            allStatusRadio.setSelected(true);
            loadProducts();
        });
        categoryComboBox.addActionListener(e -> applyFilters());
        inStockOnlyCheckBox.addActionListener(e -> applyFilters());
        allStatusRadio.addActionListener(e -> applyFilters());
        activeOnlyRadio.addActionListener(e -> applyFilters());

        refreshBtn.addActionListener(e -> loadProducts());
        closeBtn.addActionListener(e -> dispose());

        addBtn.addActionListener(e -> {
            ProductDialog dialog = new ProductDialog(this, null);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                loadProducts();
            }
        });

        editBtn.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a product row to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            List<Product> products = controller.getAllProducts();
            Product target = products.stream().filter(p -> p.getProductId() == productId).findFirst().orElse(null);
            if (target != null) {
                ProductDialog dialog = new ProductDialog(this, target);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    loadProducts();
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a product row to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            if (controller.deleteProduct(productId, this)) {
                loadProducts();
            }
        });

        getContentPane().add(mainPanel);
    }

    private void loadProducts() {
        List<Product> products = controller.getAllProducts();
        controller.populateTableModel(tableModel, products);
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        List<Product> products = controller.searchProducts(keyword);
        controller.populateTableModel(tableModel, products);
    }

    private void applyFilters() {
        List<Product> products = controller.getAllProducts();
        String selectedCat = (String) categoryComboBox.getSelectedItem();
        boolean inStockOnly = inStockOnlyCheckBox.isSelected();
        boolean activeOnly = activeOnlyRadio.isSelected();

        List<Product> filtered = products.stream().filter(p -> {
            if (inStockOnly && p.getStock() <= 0) return false;
            if (activeOnly && !p.isStatus()) return false;
            if (selectedCat != null && !selectedCat.equals("All Categories")) {
                if (p.getCategoryName() != null && !p.getCategoryName().equalsIgnoreCase(selectedCat)) {
                    return false;
                }
            }
            return true;
        }).toList();

        controller.populateTableModel(tableModel, filtered);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductFrame().setVisible(true));
    }
}
