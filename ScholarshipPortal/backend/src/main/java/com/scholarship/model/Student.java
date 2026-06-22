package com.scholarship.model;

/**
 * Student.java
 * ─────────────────────────────────────────────────────────
 * POJO representing a student's profile submitted to the
 * Scholarship Eligibility Portal.
 *
 * Used as input to the SOAP checkEligibility() operation.
 * ─────────────────────────────────────────────────────────
 */
public class Student {

    private String name;
    private double marks;          // Percentage (0–100)
    private double annualIncome;   // Family annual income in INR
    private String category;       // GEN / OBC / SC / ST

    // ── Constructors ─────────────────────────────────────
    public Student() {}

    public Student(String name, double marks, double annualIncome, String category) {
        this.name         = name;
        this.marks        = marks;
        this.annualIncome = annualIncome;
        this.category     = category;
    }

    // ── Getters & Setters ─────────────────────────────────
    public String getName()               { return name; }
    public void   setName(String name)    { this.name = name; }

    public double getMarks()              { return marks; }
    public void   setMarks(double marks)  { this.marks = marks; }

    public double getAnnualIncome()                    { return annualIncome; }
    public void   setAnnualIncome(double annualIncome) { this.annualIncome = annualIncome; }

    public String getCategory()                  { return category; }
    public void   setCategory(String category)   { this.category = category; }

    @Override
    public String toString() {
        return String.format("Student{name='%s', marks=%.2f, income=%.2f, category='%s'}",
                name, marks, annualIncome, category);
    }
}
