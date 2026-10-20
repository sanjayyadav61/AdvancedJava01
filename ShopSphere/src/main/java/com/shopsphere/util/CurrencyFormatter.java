package com.shopsphere.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility for formatting currency and dates based on Locale.
 * Meets RTU Project Syllabus topic: Internationalization (Locale, ResourceBundle, formatting).
 */
public class CurrencyFormatter {

    private static final Locale INDIA_LOCALE = new Locale("en", "IN");
    private static final NumberFormat INR_FORMAT = NumberFormat.getCurrencyInstance(INDIA_LOCALE);

    public static String formatINR(double amount) {
        return "₹" + String.format(Locale.US, "%,.2f", amount);
    }

    public static String formatCurrency(double amount, Locale locale) {
        if (locale == null) {
            locale = INDIA_LOCALE;
        }
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        return nf.format(amount);
    }

    public static String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH);
        return sdf.format(date);
    }
}
