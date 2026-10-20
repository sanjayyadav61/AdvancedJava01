package com.shopsphere.swing;

import com.shopsphere.swing.view.LoginFrame;

import javax.swing.*;

/**
 * Entry point for launching the ShopSphere Swing Desktop Admin Module.
 * Fulfills RTU Syllabus: Desktop Admin/Management application in Swing.
 */
public class SwingAdminLauncher {
    public static void main(String[] args) {
        try {
            // Attempt FlatLaf or System Look and Feel
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
            } catch (Exception e) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }

            SwingUtilities.invokeLater(() -> {
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
