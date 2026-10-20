package com.shopsphere;

import com.shopsphere.util.DBConnection;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * Embedded Tomcat Server Runner for ShopSphere.
 * Allows 1-click startup of the complete Jakarta Servlet / JSP web application
 * without requiring a separate external Apache Tomcat installation.
 * 
 * Fulfills RTU Syllabus: Web Containers, Apache Tomcat, HTTP request/response.
 */
public class ShopSphereWebServer {

    public static final int PORT = 8080;
    public static final String CONTEXT_PATH = "/shopsphere";

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("  SHOPSPHERE - RTU Advanced Java E-Commerce Platform");
        System.out.println("  Embedded Tomcat Web Container Launcher");
        System.out.println("==================================================================");

        try {
            // Check Database connection
            String dbInfo = DBConnection.getActiveDatabaseInfo();
            System.out.println("• Database Status: " + dbInfo);

            Tomcat tomcat = new Tomcat();
            tomcat.setPort(PORT);
            tomcat.getConnector(); // Initialize HTTP connector

            // Configure base directory for temporary Tomcat working files
            String baseDir = new File("target/tomcat-embed").getAbsolutePath();
            tomcat.setBaseDir(baseDir);

            // Configure web application context
            File webappDir = new File("src/main/webapp");
            if (!webappDir.exists()) {
                webappDir = new File("webapp");
            }

            StandardContext ctx = (StandardContext) tomcat.addWebapp(CONTEXT_PATH, webappDir.getAbsolutePath());
            ctx.setParentClassLoader(ShopSphereWebServer.class.getClassLoader());
            ctx.setReloadable(true);

            // Mount compiled classes so servlets and DAOs are discoverable by Tomcat
            File classesDir = new File("target/classes");
            if (classesDir.exists()) {
                WebResourceRoot resources = new StandardRoot(ctx);
                resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", classesDir.getAbsolutePath(), "/"));
                ctx.setResources(resources);
            }

            System.out.println("• Web Application Context mounted at: " + CONTEXT_PATH);
            System.out.println("• Document Base: " + webappDir.getAbsolutePath());
            System.out.println("• Starting Apache Tomcat Server on port " + PORT + "...");

            tomcat.start();

            System.out.println("\n------------------------------------------------------------------");
            System.out.println("  ShopSphere Web Store is LIVE and accessible at:");
            System.out.println("  >> http://localhost:" + PORT + CONTEXT_PATH + " <<");
            System.out.println("------------------------------------------------------------------");
            System.out.println("  Customer Login: customer@shopsphere.com / password123");
            System.out.println("  Admin Login:    admin@shopsphere.com / admin123");
            System.out.println("  Admin Portal:   http://localhost:" + PORT + CONTEXT_PATH + "/admin/dashboard");
            System.out.println("  RTU Demos:      http://localhost:" + PORT + CONTEXT_PATH + "/demos");
            System.out.println("------------------------------------------------------------------\n");

            tomcat.getServer().await();

        } catch (Exception e) {
            System.err.println("Failed to start Embedded Tomcat: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
