package com.shopsphere.swing.view;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * RTU Swing MVC View: CustomerFrame
 * Standalone Customer/User Management Frame fulfilling Section 21 of the project guide.
 * Demonstrates: JFrame, JTable, DefaultTableModel, JButton, JOptionPane.
 */
public class CustomerFrame extends JFrame {

    private final UserDAO userDAO = new UserDAO();
    private DefaultTableModel tableModel;
    private JTable userTable;

    public CustomerFrame() {
        setTitle("ShopSphere - Customer Administration (Swing MVC View)");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        loadCustomers();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel header = new JLabel("Registered Customer Accounts", SwingConstants.LEFT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        main.add(header, BorderLayout.NORTH);

        String[] cols = {"User ID", "Full Name", "Email Address", "Phone", "Role", "Status", "Joined On"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        userTable = new JTable(tableModel);
        userTable.setRowHeight(25);
        main.add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        JButton refreshBtn = new JButton("Refresh Accounts");
        JButton toggleRoleBtn = new JButton("Toggle Admin Role");
        JButton closeBtn = new JButton("Close");

        bottom.add(refreshBtn);
        bottom.add(toggleRoleBtn);
        bottom.add(closeBtn);
        main.add(bottom, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadCustomers());
        closeBtn.addActionListener(e -> dispose());

        toggleRoleBtn.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a user row.", "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int userId = (int) tableModel.getValueAt(row, 0);
            String currentRole = (String) tableModel.getValueAt(row, 4);
            String newRole = "ADMIN".equalsIgnoreCase(currentRole) ? "CUSTOMER" : "ADMIN";
            if (userDAO.updateRole(userId, newRole)) {
                JOptionPane.showMessageDialog(this, "User role updated to: " + newRole);
                loadCustomers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update role.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        getContentPane().add(main);
    }

    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<User> users = userDAO.findAll();
        for (User u : users) {
            tableModel.addRow(new Object[]{
                    u.getUserId(),
                    u.getName(),
                    u.getEmail(),
                    u.getMobile() != null ? u.getMobile() : "N/A",
                    u.getRole(),
                    u.isStatus() ? "Active" : "Disabled",
                    u.getCreatedAt() != null ? u.getCreatedAt().toString() : "N/A"
            });
        }
    }
}
