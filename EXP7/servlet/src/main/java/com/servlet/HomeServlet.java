package com.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String user = (String) request.getAttribute("user");
        if (user == null) {
            user = "Guest";
        }

        out.println("<h1>Welcome, " + user + "!</h1>");
        out.println("<p>This is the Home Page after successful login.</p>");
        out.println("<a href=\"logout\">Logout</a>");
    }
}
