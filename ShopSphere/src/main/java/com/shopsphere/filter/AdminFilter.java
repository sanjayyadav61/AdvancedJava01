package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter that protects Admin dashboard and management routes (/admin/*).
 * Implements RTU Project Syllabus topic: Admin Authorization Filter.
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        String uri = httpRequest.getRequestURI();
        if (uri.endsWith("/admin/login")) {
            chain.doFilter(request, response);
            return;
        }

        if (user != null && user.isAdmin()) {
            // Authorized admin user
            chain.doFilter(request, response);
        } else {
            // Unauthorized access attempt
            if (user == null) {
                httpRequest.getSession(true).setAttribute("redirectAfterLogin", httpRequest.getRequestURI());
                httpRequest.getSession().setAttribute("authError", "Admin privileges required. Please sign in with an Admin account.");
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            } else {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: You do not possess administrator rights.");
            }
        }
    }

    @Override
    public void destroy() {}
}
