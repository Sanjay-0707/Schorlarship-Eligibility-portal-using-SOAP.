package com.scholarship.model;

/**
 * Scholarship.java
 * ─────────────────────────────────────────────────────────
 * Represents a government scholarship scheme returned as
 * part of the SOAP eligibility check response.
 * ─────────────────────────────────────────────────────────
 */
public class Scholarship {

    private String schemeCode;
    private String name;
    private String description;
    private double amountPerYear;   // INR per annum

    // ── Constructors ─────────────────────────────────────
    public Scholarship() {}

    public Scholarship(String schemeCode, String name,
                       String description, double amountPerYear) {
        this.schemeCode    = schemeCode;
        this.name          = name;
        this.description   = description;
        this.amountPerYear = amountPerYear;
    }

    // ── Getters & Setters ─────────────────────────────────
    public String getSchemeCode()                    { return schemeCode; }
    public void   setSchemeCode(String schemeCode)   { this.schemeCode = schemeCode; }

    public String getName()                          { return name; }
    public void   setName(String name)               { this.name = name; }

    public String getDescription()                   { return description; }
    public void   setDescription(String description) { this.description = description; }

    public double getAmountPerYear()                       { return amountPerYear; }
    public void   setAmountPerYear(double amountPerYear)   { this.amountPerYear = amountPerYear; }

    @Override
    public String toString() {
        return String.format("Scholarship{code='%s', name='%s', amount=%.2f}",
                schemeCode, name, amountPerYear);
    }
}
