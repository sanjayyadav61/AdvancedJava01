package com.shopsphere.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates RTU Syllabus Topic:
 * java.net URL, URLConnection, Content and Protocol Handlers.
 */
public class UrlConnectionDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("RTU Advanced Java Demo: java.net URL and URLConnection");
        System.out.println("==========================================================");

        try {
            String testUrlStr = "https://httpbin.org/get";
            System.out.println("1. Parsing URL: " + testUrlStr);
            URL url = new URL(testUrlStr);

            System.out.println("   • Protocol:      " + url.getProtocol());
            System.out.println("   • Host:          " + url.getHost());
            System.out.println("   • Default Port:  " + url.getDefaultPort());
            System.out.println("   • File/Path:     " + url.getPath());

            System.out.println("\n2. Opening URLConnection...");
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            System.out.println("   • Content Type:     " + connection.getContentType());
            System.out.println("   • Content Encoding: " + connection.getContentEncoding());
            System.out.println("   • Content Length:   " + connection.getContentLength());

            System.out.println("\n3. Inspecting Response Headers:");
            Map<String, List<String>> headerFields = connection.getHeaderFields();
            int count = 0;
            for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                if (entry.getKey() != null && count++ < 5) {
                    System.out.println("   • " + entry.getKey() + ": " + entry.getValue().get(0));
                }
            }

            System.out.println("\n4. Reading stream snippet:");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                int linesRead = 0;
                while ((line = reader.readLine()) != null && linesRead++ < 6) {
                    System.out.println("   " + line);
                }
            }
            System.out.println("\n[SUCCESS] URLConnection demonstration executed successfully.");
        } catch (Exception e) {
            System.out.println("[NOTE] Live network connection simulated (offline or timeout): " + e.getMessage());
            System.out.println("Protocol Handler for 'https' and Content Handler loaded via standard java.net package.");
        }
    }
}
