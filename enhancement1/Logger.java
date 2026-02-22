package com.contactservice.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple logging utility for the Contact Service.
 * Provides timestamped console logging.
 */
public class Logger {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static boolean enabled = true;

    /**
     * Logs a message with timestamp
     * @param message The message to log
     */
    public static void log(String message) {
        if (enabled) {
            String timestamp = LocalDateTime.now().format(formatter);
            System.out.println("[" + timestamp + "] " + message);
        }
    }

    /**
     * Logs an error message
     * @param message The error message
     * @param e The exception
     */
    public static void error(String message, Exception e) {
        if (enabled) {
            String timestamp = LocalDateTime.now().format(formatter);
            System.err.println("[" + timestamp + "] ERROR: " + message);
            if (e != null) {
                System.err.println("Exception: " + e.getMessage());
            }
        }
    }

    /**
     * Enables or disables logging
     */
    public static void setEnabled(boolean enabled) {
        Logger.enabled = enabled;
    }
}