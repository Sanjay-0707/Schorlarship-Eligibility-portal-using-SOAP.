package com.scholarship.util;

import java.io.IOException;
import java.util.logging.*;

/**
 * AppLogger.java
 * ─────────────────────────────────────────────────────────
 * Centralised logging configuration.
 * Logs are written to:
 *   - Console (INFO level)
 *   - scholarship.log file (ALL levels)
 * ─────────────────────────────────────────────────────────
 */
public class AppLogger {

    private static final String LOG_FILE = "scholarship.log";
    private static boolean initialized   = false;

    /**
     * Returns a named Logger with file + console handlers attached.
     */
    public static Logger getLogger(String name) {
        if (!initialized) {
            setupRootLogger();
            initialized = true;
        }
        return Logger.getLogger(name);
    }

    private static void setupRootLogger() {
        Logger root = Logger.getLogger("");
        // Remove default handlers
        for (Handler h : root.getHandlers()) {
            root.removeHandler(h);
        }

        // Console handler — INFO and above
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new SimpleFormatter());
        root.addHandler(consoleHandler);

        // File handler — ALL levels
        try {
            FileHandler fileHandler = new FileHandler(LOG_FILE, true); // append=true
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            root.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("⚠ Could not create log file: " + e.getMessage());
        }

        root.setLevel(Level.ALL);
    }
}
