package com.shopsphere.rmi.server;

import com.shopsphere.rmi.remote.InventoryService;
import com.shopsphere.util.DBConnection;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

/**
 * RMI Server Launcher.
 * Starts RMI Registry and binds InventoryService implementation.
 * Fulfills RTU Syllabus: RMI Registry & Naming.
 */
public class RmiServer {
    private static final Logger LOGGER = Logger.getLogger(RmiServer.class.getName());
    public static final int RMI_PORT = 1099;
    public static final String SERVICE_NAME = "InventoryService";

    public static void main(String[] args) {
        try {
            LOGGER.info("Starting ShopSphere RMI Server...");
            LOGGER.info("Connected to database: " + DBConnection.getActiveDatabaseInfo());

            // Create or locate RMI Registry on port 1099
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(RMI_PORT);
                LOGGER.info("RMI Registry successfully created on port " + RMI_PORT);
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(RMI_PORT);
                LOGGER.info("Located existing RMI Registry on port " + RMI_PORT);
            }

            // Create Remote Service instance
            InventoryService service = new InventoryServiceImpl();

            // Bind service to registry
            String url = "rmi://localhost:" + RMI_PORT + "/" + SERVICE_NAME;
            Naming.rebind(url, service);

            LOGGER.info("==================================================");
            LOGGER.info("ShopSphere RMI Service bound successfully at: " + url);
            LOGGER.info("RMI Server is ready and waiting for remote invocations.");
            LOGGER.info("==================================================");

        } catch (Exception e) {
            LOGGER.severe("Failed to start RMI Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
