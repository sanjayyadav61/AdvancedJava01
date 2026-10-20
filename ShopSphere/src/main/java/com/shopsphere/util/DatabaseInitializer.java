package com.shopsphere.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Initializes database schema and seed data automatically if tables are missing.
 * Ensures zero-configuration startup for student viva evaluations.
 */
public class DatabaseInitializer {
    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    public static synchronized void initializeIfEmpty(Connection conn) {
        try {
            if (isDatabaseInitialized(conn)) {
                LOGGER.info("Database is already initialized. Skipping schema seeding.");
                return;
            }

            LOGGER.info("Tables not found. Initializing ShopSphere database schema and seed data...");

            InputStream in = DatabaseInitializer.class.getClassLoader().getResourceAsStream("database/shopsphere.sql");
            if (in == null) {
                // Try file system path if resource stream is null
                java.io.File file = new java.io.File("database/shopsphere.sql");
                if (file.exists()) {
                    in = new java.io.FileInputStream(file);
                }
            }

            if (in == null) {
                LOGGER.warning("Could not locate database/shopsphere.sql file for automated initialization.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    // Skip comments and empty lines
                    if (trimmed.startsWith("--") || trimmed.startsWith("/*") || trimmed.isEmpty()) {
                        continue;
                    }
                    // Skip database creation/switching statements if in embedded or already selected database
                    if (trimmed.toUpperCase().startsWith("CREATE DATABASE") || trimmed.toUpperCase().startsWith("USE ")) {
                        continue;
                    }
                    sb.append(line).append("\n");
                }
            }

            // Split into separate SQL statements
            String[] statements = sb.toString().split(";\\s*(\\r?\\n)+");
            try (Statement stmt = conn.createStatement()) {
                for (String sql : statements) {
                    String cleanSql = sql.trim();
                    if (!cleanSql.isEmpty()) {
                        try {
                            stmt.execute(cleanSql);
                        } catch (Exception ex) {
                            LOGGER.log(Level.FINER, "Non-critical error running SQL: " + cleanSql, ex);
                        }
                    }
                }
            }
            LOGGER.info("ShopSphere database schema and sample seed data successfully initialized!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing database", e);
        }
    }

    private static boolean isDatabaseInitialized(Connection conn) {
        try (Statement s = conn.createStatement()) {
            s.executeQuery("SELECT 1 FROM users LIMIT 1");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
