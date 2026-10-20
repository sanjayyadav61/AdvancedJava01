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
 * Filter that protects customer private routes (Cart, Checkout, Orders, Profile, Wishlist).
 * Implements RTU Project Syllabus topic: Servlet Filters and Authentication.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {
    "/cart", "/cart.jsp",
    "/checkout", "/checkout.jsp",
    "/orders", "/orders.jsp", "/order-details", "/order-details.jsp",
    "/profile", "/profile.jsp",
    "/wishlist", "/wishlist.jsp"
})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user != null) {
            // User is authenticated, proceed down filter chain
            chain.doFilter(request, response);
        } else {
            // Not authenticated: save target URI and redirect to login
            String targetUri = httpRequest.getRequestURI();
            String query = httpRequest.getQueryString();
            if (query != null) {
                targetUri += "?" + query;
            }

            httpRequest.getSession(true).setAttribute("redirectAfterLogin", targetUri);
            httpRequest.getSession().setAttribute("authError", "Please sign in to access this page.");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        }
    }

    @Override
    public void destroy() {}
}
