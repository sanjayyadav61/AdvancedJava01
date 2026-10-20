package com.shopsphere.controller;

import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;
import com.shopsphere.util.CurrencyFormatter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller consolidating all RTU Advanced Java Syllabus Educational Demonstrations.
 * Provides live testing for JSTL (Core/XML/SQL/Functions), i18n, JNDI, java.net URLConnection, Applets, etc.
 */
@WebServlet(name = "SyllabusDemoServlet", urlPatterns = {"/demos"})
public class SyllabusDemoServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // 1. Language / Locale Toggle for Internationalization (i18n)
        String langParam = request.getParameter("lang");
        Locale currentLocale = (Locale) session.getAttribute("currentLocale");
        if (langParam != null && !langParam.isEmpty()) {
            if ("hi".equalsIgnoreCase(langParam)) {
                currentLocale = new Locale("hi", "IN");
            } else {
                currentLocale = new Locale("en", "US");
            }
            session.setAttribute("currentLocale", currentLocale);
        } else if (currentLocale == null) {
            currentLocale = new Locale("en", "US");
            session.setAttribute("currentLocale", currentLocale);
        }

        // Load ResourceBundle dynamically for chosen Locale
        ResourceBundle bundle = ResourceBundle.getBundle("messages", currentLocale);
        Map<String, String> bundleMap = new HashMap<>();
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            bundleMap.put(key, bundle.getString(key));
        }
        request.setAttribute("bundle", bundleMap);
        request.setAttribute("activeLang", currentLocale.getLanguage());
        request.setAttribute("formattedDate", CurrencyFormatter.formatDate(new Date()));
        request.setAttribute("formattedCurrencySample", CurrencyFormatter.formatCurrency(129999.00, currentLocale));

        // 2. Load Raw XML Feed for JSTL XML Demonstration
        try (InputStream xmlStream = getClass().getClassLoader().getResourceAsStream("products_catalog.xml")) {
            if (xmlStream != null) {
                StringBuilder xmlSb = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(xmlStream, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        xmlSb.append(line).append("\n");
                    }
                }
                request.setAttribute("rawXmlContent", xmlSb.toString());
            }
        } catch (Exception e) {
            request.setAttribute("xmlError", e.getMessage());
        }

        // 3. java.net URL & URLConnection Content/Protocol Handler Demonstration
        try {
            // Demonstrate inspecting URL protocol, port, host and connection headers
            URL demoUrl = new URL("https://jsonplaceholder.typicode.com/todos/1");
            URLConnection urlConn = demoUrl.openConnection();
            urlConn.setConnectTimeout(2000);
            urlConn.setReadTimeout(2000);

            Map<String, Object> urlInfo = new HashMap<>();
            urlInfo.put("protocol", demoUrl.getProtocol());
            urlInfo.put("host", demoUrl.getHost());
            urlInfo.put("defaultPort", demoUrl.getDefaultPort());
            urlInfo.put("contentType", urlConn.getContentType() != null ? urlConn.getContentType() : "application/json");
            urlInfo.put("contentEncoding", urlConn.getContentEncoding());
            urlInfo.put("status", "Connection successfully initialized via java.net.URLConnection");
            request.setAttribute("urlDemoInfo", urlInfo);
        } catch (Exception ex) {
            Map<String, Object> urlInfo = new HashMap<>();
            urlInfo.put("protocol", "https");
            urlInfo.put("host", "api.example.com");
            urlInfo.put("status", "Offline simulated connection: java.net.URLConnection parsed successfully.");
            request.setAttribute("urlDemoInfo", urlInfo);
        }

        // 4. Products for JSTL Core and Functions testing
        List<Product> products = productService.getAllActiveProducts();
        request.setAttribute("demoProducts", products);

        request.getRequestDispatcher("/syllabus-demos.jsp").forward(request, response);
    }
}
