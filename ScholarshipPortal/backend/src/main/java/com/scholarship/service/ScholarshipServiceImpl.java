package com.scholarship.service;

import com.scholarship.db.DatabaseUtil;
import com.scholarship.model.EligibilityResponse;
import com.scholarship.model.Scholarship;
import com.scholarship.model.Student;
import com.scholarship.util.AppLogger;

import javax.jws.WebService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * ScholarshipServiceImpl.java  ← Service Implementation Bean (SIB)
 * ──────────────────────────────────────────────────────────────────
 * Implements the SOAP checkEligibility operation.
 *
 * Eligibility Rules applied:
 *   1. Input validation (marks 0–100, income > 0, valid category)
 *   2. Queries the scholarships table for ALL active schemes
 *   3. Filters schemes where:
 *        marks       >= scheme.min_marks
 *        annualIncome <= scheme.max_income
 *        category      is in scheme.eligible_categories (SET column)
 *   4. Saves student record and audit rows to MySQL
 *   5. Returns list of matched scholarships as XML response
 * ──────────────────────────────────────────────────────────────────
 */
@WebService(
    endpointInterface = "com.scholarship.service.ScholarshipService",
    serviceName       = "ScholarshipEligibilityService",
    portName          = "ScholarshipServicePort",
    targetNamespace   = "http://scholarship.com/service",
    wsdlLocation      = ""  // auto-generated
)
public class ScholarshipServiceImpl implements ScholarshipService {

    private static final Logger LOG = AppLogger.getLogger(ScholarshipServiceImpl.class.getName());

    // Valid categories accepted by the service
    private static final String[] VALID_CATEGORIES = {"GEN", "OBC", "SC", "ST"};

    /**
     * Core SOAP operation — checks student eligibility against the
     * scholarship database and returns matching schemes.
     */
    @Override
    public EligibilityResponse checkEligibility(String name,
                                                 double marks,
                                                 double annualIncome,
                                                 String category) {

        LOG.info("📥 Received checkEligibility request | " +
                 "Name=" + name + " Marks=" + marks +
                 " Income=" + annualIncome + " Category=" + category);

        // ── Step 1: Input Validation ───────────────────────────
        String validationError = validateInput(name, marks, annualIncome, category);
        if (validationError != null) {
            LOG.warning("⚠ Validation failed: " + validationError);
            return new EligibilityResponse(false, "❌ " + validationError, new ArrayList<>());
        }

        // ── Step 2: Normalise category ─────────────────────────
        category = category.trim().toUpperCase();

        // ── Step 3: Database query ─────────────────────────────
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();

            // Save student record (upsert by name + category)
            int studentId = saveStudent(conn, name, marks, annualIncome, category);

            // Find matching scholarships
            List<Scholarship> matched = findEligibleScholarships(conn, marks, annualIncome, category);

            // Audit — record each matched scholarship
            for (Scholarship s : matched) {
                saveAudit(conn, studentId, s.getSchemeCode());
            }

            // ── Step 4: Build response ─────────────────────────
            if (matched.isEmpty()) {
                LOG.info("📋 No scholarships matched for: " + name);
                return new EligibilityResponse(false,
                    "No scholarship schemes matched your profile. " +
                    "Check criteria and reapply when eligible.", matched);
            }

            LOG.info("✅ " + matched.size() + " scholarship(s) matched for: " + name);
            return new EligibilityResponse(true,
                "Congratulations! You are eligible for " + matched.size() + " scholarship(s).",
                matched);

        } catch (SQLException e) {
            LOG.severe("❌ Database error: " + e.getMessage());
            return new EligibilityResponse(false,
                "Service temporarily unavailable. Please try again later.", new ArrayList<>());
        } finally {
            DatabaseUtil.close(conn);
        }
    }

    // ────────────────────────────────────────────────────────────
    //  Private Helpers
    // ────────────────────────────────────────────────────────────

    /**
     * Validates all input fields.
     * @return null if valid, error message string if invalid.
     */
    private String validateInput(String name, double marks,
                                  double annualIncome, String category) {
        if (name == null || name.trim().isEmpty())
            return "Student name is required.";
        if (marks < 0 || marks > 100)
            return "Marks must be between 0 and 100.";
        if (annualIncome < 0)
            return "Annual income cannot be negative.";
        if (category == null || category.trim().isEmpty())
            return "Category is required.";

        String cat = category.trim().toUpperCase();
        for (String valid : VALID_CATEGORIES) {
            if (valid.equals(cat)) return null;
        }
        return "Invalid category '" + category + "'. Must be one of: GEN, OBC, SC, ST.";
    }

    /**
     * Inserts or retrieves the student record.
     * Returns the student's database ID.
     */
    private int saveStudent(Connection conn, String name, double marks,
                             double annualIncome, String category) throws SQLException {

        String sql = "INSERT INTO students (full_name, marks, annual_income, category) " +
                     "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setDouble(2, marks);
            ps.setDouble(3, annualIncome);
            ps.setString(4, category);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    /**
     * Queries the scholarships table and returns schemes
     * that the student qualifies for.
     *
     * MySQL SET column trick:
     *   FIND_IN_SET('SC', eligible_categories) > 0
     * works correctly with SET type columns.
     */
    private List<Scholarship> findEligibleScholarships(Connection conn,
                                                        double marks,
                                                        double annualIncome,
                                                        String category) throws SQLException {

        String sql = "SELECT scheme_code, name, description, amount_per_year " +
                     "FROM scholarships " +
                     "WHERE is_active = 1 " +
                     "  AND min_marks    <= ? " +
                     "  AND max_income   >= ? " +
                     "  AND FIND_IN_SET(?, eligible_categories) > 0 " +
                     "ORDER BY amount_per_year DESC";

        List<Scholarship> result = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, marks);
            ps.setDouble(2, annualIncome);
            ps.setString(3, category);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new Scholarship(
                    rs.getString("scheme_code"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("amount_per_year")
                ));
            }
        }
        return result;
    }

    /**
     * Saves an eligibility audit row by student ID and scheme code.
     */
    private void saveAudit(Connection conn, int studentId,
                            String schemeCode) throws SQLException {
        if (studentId <= 0) return;
        String sql = "INSERT INTO eligibility_results (student_id, scholarship_id) " +
                     "SELECT ?, id FROM scholarships WHERE scheme_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setString(2, schemeCode);
            ps.executeUpdate();
        }
    }
}
