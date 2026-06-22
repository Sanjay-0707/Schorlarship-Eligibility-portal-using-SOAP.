package com.scholarship.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * DatabaseUtil.java
 * ─────────────────────────────────────────────────────────
 * Provides a simple JDBC connection to MySQL.
 *
 * ⚙ Configuration:
 *   Update DB_URL, DB_USER, DB_PASS below to match your
 *   local MySQL installation before running the project.
 * ─────────────────────────────────────────────────────────
 */
public class DatabaseUtil {

    private static final Logger LOG = Logger.getLogger(DatabaseUtil.class.getName());

    // ── Connection Settings ────────────────────────────
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL    = "jdbc:mysql://localhost:3306/scholarship_db"
                                          + "?useSSL=false&serverTimezone=Asia/Kolkata"
                                          + "&allowPublicKeyRetrieval=true";
    private static final String DB_USER   = "root";       // ← Change if needed
    private static final String DB_PASS   = "root";   // ← Change to your MySQL password

    // ── Static block: load JDBC driver once ───────────
    static {
        try {
            Class.forName(DB_DRIVER);
            LOG.info("✅ MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            LOG.severe("❌ MySQL JDBC Driver NOT found: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Returns a new JDBC Connection to the scholarship database.
     * Caller is responsible for closing the connection.
     */
    public static Connection getConnection() throws SQLException {
        LOG.info("🔗 Acquiring DB connection...");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    /**
     * Silently close a connection (null-safe).
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.warning("⚠ Failed to close DB connection: " + e.getMessage());
            }
        }
    }
}
