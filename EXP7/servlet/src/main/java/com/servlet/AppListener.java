package com.servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Application Started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application Stopped");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("Session Created: " + se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Session Destroyed: " + se.getSession().getId());
    }
}