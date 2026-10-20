package com.shopsphere.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * HttpSessionListener tracking active sessions and online users.
 * Implements RTU Project Syllabus topic: Event Handling & HttpSessionListener.
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    private static final Logger LOGGER = Logger.getLogger(SessionListener.class.getName());
    private static final AtomicInteger ACTIVE_SESSIONS = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int count = ACTIVE_SESSIONS.incrementAndGet();
        HttpSession session = se.getSession();
        // Set standard session timeout to 30 minutes
        session.setMaxInactiveInterval(30 * 60);
        session.getServletContext().setAttribute("activeUserCount", count);
        LOGGER.info("HttpSession created: " + session.getId() + " | Active Sessions: " + count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int count = ACTIVE_SESSIONS.decrementAndGet();
        if (count < 0) count = 0;
        se.getSession().getServletContext().setAttribute("activeUserCount", count);
        LOGGER.info("HttpSession destroyed. Active Sessions: " + count);
    }

    public static int getActiveSessionsCount() {
        return ACTIVE_SESSIONS.get();
    }
}
