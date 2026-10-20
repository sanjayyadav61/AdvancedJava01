package com.shopsphere.demo;

/**
 * Educational demonstration of the Applet lifecycle and historical evolution.
 * Fulfills RTU Syllabus topic: Applets.
 */
public class AppletConceptDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("RTU Advanced Java Demo: Java Applets Historical Overview");
        System.out.println("==========================================================");

        System.out.println("1. Applet Lifecycle Methods (java.applet.Applet):");
        System.out.println("   • init():    Called once when the applet is first loaded into memory.");
        System.out.println("                Used for initialization (UI components, params).");
        System.out.println("   • start():   Called when the browser visits or returns to the HTML page.");
        System.out.println("                Starts animations, background threads.");
        System.out.println("   • paint(g):  Called automatically whenever the applet redraws its screen.");
        System.out.println("                Uses java.awt.Graphics (e.g. g.drawString(\"Hello\", 20, 20)).");
        System.out.println("   • stop():    Called when the user navigates away from the page.");
        System.out.println("                Suspends threads and heavy operations.");
        System.out.println("   • destroy(): Called when browser completely unloads applet from cache.");
        System.out.println("                Releases native resources.");

        System.out.println("\n2. Why the Industry Shifted from Applets to Servlets/JSP & Modern Web:");
        System.out.println("   • Security Vulnerabilities: NPAPI browser plugin model posed severe sandbox risks.");
        System.out.println("   • Browser Deprecation: Chrome, Firefox, Safari and Edge removed NPAPI support.");
        System.out.println("   • Architectural Shift: Logic moved from client-side JVM execution (Applets)");
        System.out.println("     to server-side enterprise web containers (Jakarta Servlets, JSP, Tomcat).");
        System.out.println("   • ShopSphere provides modern Jakarta EE Servlets and dynamic JSP views");
        System.out.println("     with an interactive visual Applet simulator in the web UI!");

        System.out.println("\n[SUCCESS] Applet lifecycle and architectural context explained.");
    }
}
