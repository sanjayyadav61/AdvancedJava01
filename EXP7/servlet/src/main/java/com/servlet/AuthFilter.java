package com.servlet;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/home")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // initialization code
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        if (loggedIn) {
            String user = (String) session.getAttribute("user");
            req.setAttribute("user", user);
            chain.doFilter(request, response);
        } else {
            res.sendRedirect("index.html");
        }
    }

    @Override
    public void destroy() {
        // cleanup code
    }
}
