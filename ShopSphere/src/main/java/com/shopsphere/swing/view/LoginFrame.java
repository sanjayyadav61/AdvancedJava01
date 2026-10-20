package com.shopsphere.swing.view;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.swing.controller.LoginController;
import com.shopsphere.util.DBConnection;

import javax.swing.*;
import java.awt.*;

/**
 * Swing Desktop Login Frame.
 * Demonstrates: JFrame, JPanel, JLabel, JTextField, JPasswordField,
 * JButton, JCheckBox, JOptionPane, GridBagLayout, MVC View layer.
 */
public class LoginFrame extends JFrame {

    private final UserDAO userDAO = new UserDAO();
    private final LoginController controller = new LoginController(this);

    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;
    private JCheckBox rememberMeCheckBox;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginFrame() {
        setTitle("ShopSphere Desktop Admin - Login");
        setSize(420, 480);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);

        // Header Banner
        JPanel banner = new JPanel(new GridLayout(2, 1));
        banner.setBackground(new Color(24, 30, 42));
        banner.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("SHOPSPHERE", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Desktop Management System", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(180, 190, 205));

        banner.add(title);
        banner.add(subtitle);

        // Form Body
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);

        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel emailLbl = new JLabel("Administrator Email:");
        emailLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        form.add(emailLbl, gbc);

        gbc.gridy = 1;
        emailField = new JTextField("admin@shopsphere.com", 20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailField.setPreferredSize(new Dimension(280, 32));
        form.add(emailField, gbc);

        // Password
        gbc.gridy = 2;
        JLabel passLbl = new JLabel("Password:");
        passLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        form.add(passLbl, gbc);

        gbc.gridy = 3;
        passwordField = new JPasswordField("admin123", 20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.setPreferredSize(new Dimension(280, 32));
        form.add(passwordField, gbc);

        // Options Checkboxes
        gbc.gridy = 4;
        JPanel checksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        checksPanel.setBackground(Color.WHITE);
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBackground(Color.WHITE);
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        rememberMeCheckBox = new JCheckBox("Remember Me");
        rememberMeCheckBox.setBackground(Color.WHITE);
        rememberMeCheckBox.setSelected(true);

        checksPanel.add(showPasswordCheckBox);
        checksPanel.add(Box.createHorizontalStrut(15));
        checksPanel.add(rememberMeCheckBox);
        form.add(checksPanel, gbc);

        // Login Button
        gbc.gridy = 5;
        gbc.insets = new Insets(16, 4, 8, 4);
        loginButton = new JButton("SIGN IN TO CONSOLE");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginButton.setBackground(new Color(24, 119, 242));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(280, 38));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> performLogin());
        form.add(loginButton, gbc);

        // Status Label
        gbc.gridy = 6;
        gbc.insets = new Insets(4, 4, 4, 4);
        statusLabel = new JLabel("Default: admin@shopsphere.com / admin123", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        statusLabel.setForeground(new Color(130, 140, 150));
        form.add(statusLabel, gbc);

        container.add(banner, BorderLayout.NORTH);
        container.add(form, BorderLayout.CENTER);

        // Footnote with DB status
        JLabel dbFooter = new JLabel("Engine: " + (DBConnection.isUsingFallback() ? "Embedded H2" : "MySQL 8.0"), SwingConstants.CENTER);
        dbFooter.setBorder(BorderFactory.createEmptyBorder(6, 6, 8, 6));
        dbFooter.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        dbFooter.setForeground(Color.GRAY);
        container.add(dbFooter, BorderLayout.SOUTH);

        getContentPane().add(container);
    }

    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        controller.handleLogin(email, password);
    }

    public static void main(String[] args) {
        try {
            // Apply Modern Look and Feel (FlatLaf or System)
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
