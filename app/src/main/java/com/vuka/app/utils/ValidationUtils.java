package com.vuka.app.utils;

/**
 * Input validation utilities for VUKA forms.
 * Uses plain Java only — no Android dependencies — so it is unit-testable.
 */
public class ValidationUtils {

    private ValidationUtils() {}

    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Validates a South African mobile number.
     * Accepts formats: +27XXXXXXXXX, 0XXXXXXXXX, 27XXXXXXXXX
     */
    public static boolean isValidSAPhone(String phone) {
        if (isEmpty(phone)) return false;
        String cleaned = phone.replaceAll("[\\s\\-]", "");
        return cleaned.matches("(\\+27|0027|27|0)[6-8][0-9]{8}");
    }

    /**
     * Validates a South African ID number (13 digits).
     * Basic format check only — not Luhn algorithm for demo.
     */
    public static boolean isValidSAId(String id) {
        if (isEmpty(id)) return false;
        return id.matches("[0-9]{13}");
    }

    /**
     * Validates that a string is not empty.
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     * Validates a vehicle registration number (South African format).
     */
    public static boolean isValidRegistration(String reg) {
        if (isEmpty(reg)) return false;
        String cleaned = reg.trim().toUpperCase();
        return cleaned.length() >= 5 && cleaned.length() <= 12;
    }

    /**
     * Formats a phone number for display.
     */
    public static String formatPhone(String phone) {
        if (isEmpty(phone)) return "";
        String cleaned = phone.replaceAll("[\\s\\-]", "");
        if (cleaned.startsWith("0") && cleaned.length() == 10) {
            return "+27 " + cleaned.substring(1, 3) + " " +
                    cleaned.substring(3, 6) + " " + cleaned.substring(6);
        }
        return phone;
    }

    /**
     * Normalizes phone to E.164 for SA (+27XXXXXXXXX).
     */
    public static String normalizePhone(String phone) {
        if (isEmpty(phone)) return "";
        String cleaned = phone.replaceAll("[\\s\\-\\(\\)]", "");
        if (cleaned.startsWith("0") && cleaned.length() == 10) {
            return "+27" + cleaned.substring(1);
        }
        if (cleaned.startsWith("27") && cleaned.length() == 11) {
            return "+" + cleaned;
        }
        return cleaned;
    }
}
