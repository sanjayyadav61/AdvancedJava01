package com.shopsphere.demo;

import com.shopsphere.util.CurrencyFormatter;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Demonstrates RTU Syllabus Topic:
 * Internationalization (Locale, ResourceBundle, formatting prices and dates).
 */
public class I18nDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("RTU Advanced Java Demo: Internationalization (i18n)");
        System.out.println("==========================================================");

        double testPrice = 114900.00;
        Date testDate = new Date();

        // 1. English Locale
        Locale enLocale = new Locale("en", "US");
        ResourceBundle enBundle = ResourceBundle.getBundle("messages", enLocale);

        System.out.println("--- 1. ENGLISH LOCALE (en_US) ---");
        System.out.println("• App Title:       " + enBundle.getString("app.title"));
        System.out.println("• Tagline:         " + enBundle.getString("app.tagline"));
        System.out.println("• Nav Home:        " + enBundle.getString("nav.home"));
        System.out.println("• Nav Cart:        " + enBundle.getString("nav.cart"));
        System.out.println("• Formatted Price: " + CurrencyFormatter.formatCurrency(testPrice, enLocale));
        System.out.println("• Formatted Date:  " + CurrencyFormatter.formatDate(testDate));

        // 2. Hindi Locale
        Locale hiLocale = new Locale("hi", "IN");
        ResourceBundle hiBundle = ResourceBundle.getBundle("messages", hiLocale);

        System.out.println("\n--- 2. HINDI LOCALE (hi_IN) ---");
        System.out.println("• App Title:       " + hiBundle.getString("app.title"));
        System.out.println("• Tagline:         " + hiBundle.getString("app.tagline"));
        System.out.println("• Nav Home:        " + hiBundle.getString("nav.home"));
        System.out.println("• Nav Cart:        " + hiBundle.getString("nav.cart"));
        System.out.println("• Formatted Price: " + CurrencyFormatter.formatCurrency(testPrice, hiLocale));
        System.out.println("• Formatted Date:  " + CurrencyFormatter.formatDate(testDate));

        System.out.println("\n[SUCCESS] Internationalization ResourceBundle and Locale demonstration complete.");
    }
}
