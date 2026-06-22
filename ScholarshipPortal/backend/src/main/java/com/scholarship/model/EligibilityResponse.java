package com.scholarship.model;

import java.util.List;
import java.util.ArrayList;

/**
 * EligibilityResponse.java
 * ─────────────────────────────────────────────────────────
 * Wraps the SOAP response from checkEligibility().
 * Contains:
 *   - eligibility flag
 *   - list of matched scholarship schemes
 *   - a human-readable summary message
 * ─────────────────────────────────────────────────────────
 */
public class EligibilityResponse {

    private boolean eligible;
    private String  message;
    private List<Scholarship> scholarships;

    // ── Constructors ─────────────────────────────────────
    public EligibilityResponse() {
        this.scholarships = new ArrayList<>();
    }

    public EligibilityResponse(boolean eligible, String message,
                                List<Scholarship> scholarships) {
        this.eligible     = eligible;
        this.message      = message;
        this.scholarships = scholarships != null ? scholarships : new ArrayList<>();
    }

    // ── Getters & Setters ─────────────────────────────────
    public boolean isEligible()                { return eligible; }
    public void    setEligible(boolean val)    { this.eligible = val; }

    public String getMessage()                 { return message; }
    public void   setMessage(String msg)       { this.message = msg; }

    public List<Scholarship> getScholarships()                   { return scholarships; }
    public void setScholarships(List<Scholarship> scholarships)  { this.scholarships = scholarships; }

    public void addScholarship(Scholarship s) {
        this.scholarships.add(s);
    }
}
