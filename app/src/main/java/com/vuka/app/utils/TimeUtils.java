package com.vuka.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Time and date formatting utilities.
 */
public class TimeUtils {

    private TimeUtils() {}

    public static String getGreeting() {
        int hour = new java.util.Calendar.Builder()
                .setInstant(new Date())
                .build()
                .get(java.util.Calendar.HOUR_OF_DAY);
        if (hour < 12) return "Good Morning";
        if (hour < 18) return "Good Afternoon";
        return "Good Evening";
    }

    public static String formatTime(long millis) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(millis));
    }

    public static String formatDate(long millis) {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date(millis));
    }

    public static String formatDateTime(long millis) {
        return new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(new Date(millis));
    }

    public static String formatCountdown(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
}
