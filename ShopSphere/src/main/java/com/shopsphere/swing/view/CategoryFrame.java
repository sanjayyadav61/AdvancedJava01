package com.shopsphere.swing.view;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * RTU Swing MVC View: CategoryFrame
 * Standalone Category Management Frame fulfilling Section 21 of the project guide.
 * Demonstrates: JFrame, JTable, DefaultTableModel, JButton, JOptionPane, CRUD.
 */
public class CategoryFrame extends JFrame {

    private final CategoryDAO categoryDAO = new CategoryDAO();
    private DefaultTableModel tableModel;
    private JTable categoryTable;

    public CategoryFrame() {
        setTitle("ShopSphere - Category Management (Swing MVC View)");
        setSize(750, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadCategories();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Header
        JLabel header = new JLabel("Category Administration Console", SwingConstants.LEFT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        main.add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Category Name", "Description", "Image URL"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        categoryTable = new JTable(tableModel);
        categoryTable.setRowHeight(25);
        main.add(new JScrollPane(categoryTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        JButton addBtn = new JButton("Add Category");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton refreshBtn = new JButton("Refresh");
        JButton closeBtn = new JButton("Close");

        buttonBar.add(addBtn);
        buttonBar.add(deleteBtn);
        buttonBar.add(refreshBtn);
        buttonBar.add(closeBtn);
        main.add(buttonBar, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadCategories());
        closeBtn.addActionListener(e -> dispose());

        addBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter Category Name:", "New Category", JOptionPane.PLAIN_MESSAGE);
            if (name != null && !name.trim().isEmpty()) {
                String desc = JOptionPane.showInputDialog(this, "Enter Description:", "New Category", JOptionPane.PLAIN_MESSAGE);
                Category cat = new Category(0, name.trim(), desc != null ? desc.trim() : "", "");
                if (categoryDAO.create(cat)) {
                    JOptionPane.showMessageDialog(this, "Category created successfully!");
                    loadCategories();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create category.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = categoryTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a category row to delete.", "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete Category #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (categoryDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Category deleted successfully!");
                    loadCategories();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not delete category (may contain products).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        getContentPane().add(main);
    }

    private void loadCategories() {
        tableModel.setRowCount(0);
        List<Category> list = categoryDAO.findAll();
        for (Category c : list) {
            tableModel.addRow(new Object[]{c.getCategoryId(), c.getName(), c.getDescription(), c.getImageUrl()});
        }
    }
}
