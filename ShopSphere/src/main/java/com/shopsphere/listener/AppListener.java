package com.shopsphere.listener;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.util.DBConnection;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.logging.Logger;

/**
 * ServletContextListener handling application lifecycle events.
 * Implements RTU Project Syllabus topic: Event Handling & Web Listeners.
 */
@WebListener
public class AppListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        LOGGER.info("ShopSphere Application starting up...");

        // Set application-wide attributes (ServletContext demonstration)
        context.setAttribute("appName", "ShopSphere");
        context.setAttribute("appTagline", "RTU Advanced Java Capstone E-Commerce");
        context.setAttribute("appVersion", "1.0.0");
        context.setAttribute("activeDbInfo", DBConnection.getActiveDatabaseInfo());

        // Preload active categories for global navigation
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            context.setAttribute("globalCategories", categoryDAO.findActive());
        } catch (Exception e) {
            LOGGER.warning("Could not pre-populate global categories: " + e.getMessage());
        }

        LOGGER.info("ShopSphere contextInitialized complete. Database: " + DBConnection.getActiveDatabaseInfo());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("ShopSphere Application shutting down. Releasing global resources.");
    }
}
