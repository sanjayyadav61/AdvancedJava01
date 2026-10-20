package com.shopsphere.swing.view;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Swing JDialog for adding and editing products.
 * Demonstrates Swing Form Components: JTextField, JComboBox, JCheckBox, JTextArea, JButton.
 */
public class ProductDialog extends JDialog {

    private final Product product;
    private boolean saved = false;

    private JComboBox<CategoryItem> categoryCombo;
    private JTextField nameField;
    private JTextField brandField;
    private JTextArea descArea;
    private JTextField priceField;
    private JTextField discountField;
    private JSpinner stockSpinner;
    private JTextField imageUrlField;
    private JCheckBox statusCheckBox;

    public static class CategoryItem {
        private final int id;
        private final String name;

        public CategoryItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }

        @Override
        public String toString() { return name; }
    }

    public ProductDialog(Frame parent, Product product) {
        this(parent, product != null ? "Edit Product" : "Add New Product", product, new com.shopsphere.dao.CategoryDAO().findAll());
    }

    public ProductDialog(Frame parent, String title, Product product, List<Category> categories) {
        super(parent, title, true);
        this.product = product != null ? product : new Product();

        initComponents(categories);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void initComponents(List<Category> categories) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Category
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Category:"), gbc);
        categoryCombo = new JComboBox<>();
        int selectedIndex = 0;
        for (int i = 0; i < categories.size(); i++) {
            Category c = categories.get(i);
            categoryCombo.addItem(new CategoryItem(c.getCategoryId(), c.getName()));
            if (product.getCategoryId() == c.getCategoryId()) {
                selectedIndex = i;
            }
        }
        if (!categories.isEmpty()) categoryCombo.setSelectedIndex(selectedIndex);
        gbc.gridx = 1;
        formPanel.add(categoryCombo, gbc);

        // Product Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Product Name:"), gbc);
        nameField = new JTextField(product.getName() != null ? product.getName() : "", 25);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Brand
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Brand:"), gbc);
        brandField = new JTextField(product.getBrand() != null ? product.getBrand() : "", 25);
        gbc.gridx = 1;
        formPanel.add(brandField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Description:"), gbc);
        descArea = new JTextArea(product.getDescription() != null ? product.getDescription() : "", 3, 25);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(descArea), gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Price (₹):"), gbc);
        priceField = new JTextField(product.getPrice() > 0 ? String.valueOf(product.getPrice()) : "999.00", 25);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        // Discount
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Discount (%):"), gbc);
        discountField = new JTextField(String.valueOf(product.getDiscount()), 25);
        gbc.gridx = 1;
        formPanel.add(discountField, gbc);

        // Stock
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Stock Quantity:"), gbc);
        stockSpinner = new JSpinner(new SpinnerNumberModel(product.getStock() > 0 ? product.getStock() : 10, 0, 10000, 1));
        gbc.gridx = 1;
        formPanel.add(stockSpinner, gbc);

        // Image URL
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Image URL:"), gbc);
        imageUrlField = new JTextField(product.getImageUrl() != null ? product.getImageUrl() : "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800", 25);
        gbc.gridx = 1;
        formPanel.add(imageUrlField, gbc);

        // Active Status Checkbox
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("Status:"), gbc);
        statusCheckBox = new JCheckBox("Active / Visible in Catalog", product.getProductId() == 0 || product.isStatus());
        gbc.gridx = 1;
        formPanel.add(statusCheckBox, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JButton saveBtn = new JButton("Save Product");
        saveBtn.setBackground(new Color(24, 119, 242));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> onSave());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(formPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void onSave() {
        try {
            String name = nameField.getText().trim();
            String brand = brandField.getText().trim();
            if (name.isEmpty() || brand.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Product Name and Brand are mandatory.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double price = Double.parseDouble(priceField.getText().trim());
            double discount = Double.parseDouble(discountField.getText().trim());
            int stock = (Integer) stockSpinner.getValue();

            CategoryItem cat = (CategoryItem) categoryCombo.getSelectedItem();
            if (cat != null) {
                product.setCategoryId(cat.getId());
            }
            product.setName(name);
            product.setBrand(brand);
            product.setDescription(descArea.getText().trim());
            product.setPrice(price);
            product.setDiscount(discount);
            product.setStock(stock);
            product.setImageUrl(imageUrlField.getText().trim());
            product.setStatus(statusCheckBox.isSelected());

            saved = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for Price and Discount.", "Number Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() { return saved; }
    public Product getProduct() { return product; }
}
