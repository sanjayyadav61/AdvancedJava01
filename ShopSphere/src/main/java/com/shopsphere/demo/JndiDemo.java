package com.shopsphere.demo;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

/**
 * Demonstrates RTU Syllabus Topic:
 * Naming and JNDI (Java Naming and Directory Interface).
 */
public class JndiDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("RTU Advanced Java Demo: JNDI (Java Naming & Directory Interface)");
        System.out.println("==========================================================");

        try {
            // Setup JNDI environment properties
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
            env.put(Context.PROVIDER_URL, "file:///");

            System.out.println("1. Initializing JNDI InitialContext...");
            Context ctx = null;
            try {
                ctx = new InitialContext(env);
            } catch (Exception ex) {
                // Use default InitialContext
                ctx = new InitialContext();
            }

            System.out.println("2. Explaining JNDI Architecture in J2EE & Tomcat:");
            System.out.println("   • JNDI provides a unified interface to multiple naming and directory services.");
            System.out.println("   • In Apache Tomcat, DataSource lookups use the 'java:comp/env' naming context:");
            System.out.println("     Context envCtx = (Context) initCtx.lookup(\"java:comp/env\");");
            System.out.println("     DataSource ds = (DataSource) envCtx.lookup(\"jdbc/ShopSphereDB\");");

            System.out.println("\n3. RMI Registry JNDI Naming Integration:");
            System.out.println("   • RMI services can also be discovered via JNDI Naming Provider:");
            System.out.println("     env.put(Context.INITIAL_CONTEXT_FACTORY, \"com.sun.jndi.rmi.registry.RegistryContextFactory\");");
            System.out.println("     env.put(Context.PROVIDER_URL, \"rmi://localhost:1099\");");
            System.out.println("     Object remoteObj = ctx.lookup(\"InventoryService\");");

            System.out.println("\n[SUCCESS] JNDI demonstration completed with standard Java naming concepts.");
        } catch (NamingException e) {
            System.out.println("JNDI Context Note: " + e.getMessage());
        }
    }
}
