package com.shopsphere.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility for SHA-256 password hashing.
 * Meets RTU Project Security Requirement: "Hash passwords before database storage".
 */
public class PasswordUtil {

    private static final String SALT = "ShopSphere@RTU#2026";

    /**
     * Hashes plain text password using standard SHA-256.
     */
    public static String hashPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password with SHA-256", e);
        }
    }

    /**
     * Verifies plain password matches hashed password.
     * Supports standard SHA-256, salted SHA-256, or direct match.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        // 1. Direct standard SHA-256 match
        String standardHash = hashPassword(plainPassword);
        if (standardHash.equalsIgnoreCase(hashedPassword)) {
            return true;
        }

        // 2. Salted SHA-256 match
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(SALT.getBytes(StandardCharsets.UTF_8));
            byte[] saltedBytes = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : saltedBytes) {
                sb.append(String.format("%02x", b));
            }
            if (sb.toString().equalsIgnoreCase(hashedPassword)) {
                return true;
            }
        } catch (Exception ignored) {}

        // 3. Direct plaintext match fallback
        return plainPassword.equals(hashedPassword);
    }
}
