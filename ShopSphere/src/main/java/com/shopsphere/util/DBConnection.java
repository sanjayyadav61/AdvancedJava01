package com.shopsphere.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC Connection Factory implementing RTU JDBC Syllabus requirements:
 * java.sql.Connection, DriverManager, PreparedStatement, Statement, ResultSet.
 * 
 * Supports MySQL primary connection with seamless embedded fallback
 * so that the application runs out of the box in student viva demonstrations.
 */
public class DBConnection {
    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static final Properties PROPS = new Properties();

    private static String primaryDriver;
    private static String primaryUrl;
    private static String primaryUser;
    private static String primaryPass;

    private static boolean fallbackEnabled = true;
    private static String fallbackDriver;
    private static String fallbackUrl;
    private static String fallbackUser;
    private static String fallbackPass;

    private static boolean usingFallback = false;
    private static boolean initialized = false;

    static {
        loadConfiguration();
    }

    private static synchronized void loadConfiguration() {
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "db.properties not found in classpath. Using defaults.", e);
        }

        primaryDriver = PROPS.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        primaryUrl = PROPS.getProperty("db.url", "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true");
        String envUser = System.getenv("SHOPSPHERE_DB_USER");
        if (envUser == null || envUser.trim().isEmpty()) {
            envUser = System.getenv("DB_USER");
        }
        primaryUser = (envUser != null && !envUser.trim().isEmpty()) ? envUser : PROPS.getProperty("db.username", "root");

        String envPass = System.getenv("SHOPSPHERE_DB_PASSWORD");
        if (envPass == null || envPass.trim().isEmpty()) {
            envPass = System.getenv("DB_PASSWORD");
        }
        primaryPass = (envPass != null) ? envPass : PROPS.getProperty("db.password", "");


        fallbackEnabled = Boolean.parseBoolean(PROPS.getProperty("db.fallback.enabled", "true"));
        fallbackDriver = PROPS.getProperty("db.fallback.driver", "org.h2.Driver");
        fallbackUrl = PROPS.getProperty("db.fallback.url", "jdbc:h2:mem:shopsphere;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1");
        fallbackUser = PROPS.getProperty("db.fallback.username", "sa");
        fallbackPass = PROPS.getProperty("db.fallback.password", "");
    }

    /**
     * Gets a database Connection using JDBC DriverManager.
     * @return active java.sql.Connection
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        if (!usingFallback) {
            try {
                Class.forName(primaryDriver);
                Connection conn = DriverManager.getConnection(primaryUrl, primaryUser, primaryPass);
                DatabaseInitializer.initializeIfEmpty(conn);
                return conn;
            } catch (ClassNotFoundException | SQLException ex) {
                LOGGER.warning("Could not connect to primary MySQL database: " + ex.getMessage());
                if (fallbackEnabled) {
                    LOGGER.info("Switching to embedded H2 database (MySQL compatibility mode) for seamless execution.");
                    usingFallback = true;
                } else {
                    throw new SQLException("Database connection failed and fallback is disabled.", ex);
                }
            }
        }

        // Fallback Connection
        try {
            Class.forName(fallbackDriver);
            Connection conn = DriverManager.getConnection(fallbackUrl, fallbackUser, fallbackPass);
            DatabaseInitializer.initializeIfEmpty(conn);
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Fallback JDBC Driver not found: " + fallbackDriver, e);
        }
    }

    /**
     * Closes JDBC resources safely.
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException ignored) {}
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException ignored) {}
        }
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }

    public static String getActiveDatabaseInfo() {
        return usingFallback ? "Embedded H2 (MySQL Mode)" : "MySQL 8.0+ (" + primaryUrl + ")";
    }

    public static boolean isUsingFallback() {
        return usingFallback;
    }
}
