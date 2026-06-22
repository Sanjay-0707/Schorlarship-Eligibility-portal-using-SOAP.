-- ============================================================
--  Scholarship Eligibility Portal - Database Schema
--  MCA Final Year Project
--  Author: Scholarship Portal Team
--  DB: MySQL 8.x
-- ============================================================

CREATE DATABASE IF NOT EXISTS scholarship_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE scholarship_db;

-- ------------------------------------------------------------
-- Table: students
-- Stores student profiles submitted via the portal
-- ------------------------------------------------------------
DROP TABLE IF EXISTS eligibility_results;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS scholarships;

CREATE TABLE students (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    full_name     VARCHAR(100)   NOT NULL,
    email         VARCHAR(150)   UNIQUE,
    marks         DECIMAL(5,2)   NOT NULL COMMENT 'Percentage marks (0-100)',
    annual_income DECIMAL(12,2)  NOT NULL COMMENT 'Annual family income in INR',
    category      ENUM('GEN','OBC','SC','ST') NOT NULL DEFAULT 'GEN',
    applied_on    TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_marks (marks)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Table: scholarships
-- Master list of government scholarship schemes
-- ------------------------------------------------------------
CREATE TABLE scholarships (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(200)   NOT NULL,
    scheme_code         VARCHAR(50)    UNIQUE NOT NULL,
    description         TEXT,
    min_marks           DECIMAL(5,2)   DEFAULT 0   COMMENT 'Minimum % marks required',
    max_income          DECIMAL(12,2)  DEFAULT 999999999 COMMENT 'Max annual income (INR)',
    eligible_categories SET('GEN','OBC','SC','ST') NOT NULL DEFAULT 'GEN,OBC,SC,ST',
    amount_per_year     DECIMAL(10,2)  COMMENT 'Scholarship amount in INR per year',
    is_active           TINYINT(1)     DEFAULT 1,
    INDEX idx_active (is_active)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Table: eligibility_results
-- Audit log of all eligibility checks performed
-- ------------------------------------------------------------
CREATE TABLE eligibility_results (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    student_id      INT NOT NULL,
    scholarship_id  INT NOT NULL,
    checked_on      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id)     REFERENCES students(id),
    FOREIGN KEY (scholarship_id) REFERENCES scholarships(id)
) ENGINE=InnoDB;

-- ============================================================
--  SAMPLE DATA
-- ============================================================

-- Government Scholarship Schemes (realistic Indian schemes)
INSERT INTO scholarships (name, scheme_code, description, min_marks, max_income, eligible_categories, amount_per_year) VALUES

('Post-Matric Scholarship for SC Students',
 'PM-SC-001',
 'Central government scholarship for Scheduled Caste students pursuing post-matriculation education.',
 50.00, 250000.00, 'SC', 15000.00),

('Post-Matric Scholarship for ST Students',
 'PM-ST-001',
 'Central government scholarship for Scheduled Tribe students pursuing post-matriculation education.',
 50.00, 250000.00, 'ST', 15000.00),

('Post-Matric Scholarship for OBC Students',
 'PM-OBC-001',
 'Central government scholarship for Other Backward Class students for post-matriculation studies.',
 55.00, 150000.00, 'OBC', 10000.00),

('National Merit Scholarship',
 'NMS-GEN-001',
 'Merit-based scholarship open to all categories for outstanding academic performance.',
 85.00, 600000.00, 'GEN,OBC,SC,ST', 20000.00),

('Prime Minister Scholarship Scheme',
 'PMSS-001',
 'Scholarship for wards of ex-servicemen and ex-coast guard personnel.',
 75.00, 500000.00, 'GEN,OBC,SC,ST', 25000.00),

('Dr. Ambedkar Post-Matric Scholarship',
 'DA-PMS-001',
 'Scholarship for OBC/EBC students for post-matric education.',
 60.00, 300000.00, 'OBC', 12000.00),

('Central Sector Scheme of Scholarships',
 'CSS-001',
 'For college and university students based on merit and means criteria.',
 80.00, 450000.00, 'GEN,OBC,SC,ST', 12000.00),

('National Scholarship for Minorities',
 'NSM-001',
 'Pre-matric and post-matric scholarships for students from minority communities.',
 50.00, 200000.00, 'OBC,SC,ST', 10000.00),

('Top Class Education Scheme for SC',
 'TCES-SC-001',
 'For SC students studying in top institutions (IITs, NITs, IIMs, etc.).',
 70.00, 600000.00, 'SC', 75000.00),

('Begum Hazrat Mahal National Scholarship',
 'BHM-001',
 'For meritorious girls belonging to minorities who passed class X/XII.',
 65.00, 200000.00, 'OBC,SC,ST', 10000.00);

-- Sample Students
INSERT INTO students (full_name, email, marks, annual_income, category) VALUES
('Rahul Kumar',    'rahul.kumar@email.com',   88.5,  120000,  'SC'),
('Priya Sharma',   'priya.sharma@email.com',  92.0,  550000,  'GEN'),
('Amit Patel',     'amit.patel@email.com',    67.5,  180000,  'OBC'),
('Sneha Reddy',    'sneha.reddy@email.com',   75.0,  220000,  'ST'),
('Vikram Singh',   'vikram.singh@email.com',  55.0,  350000,  'GEN');

-- Sample eligibility audit
INSERT INTO eligibility_results (student_id, scholarship_id) VALUES
(1, 1), (1, 7), (2, 7), (3, 3), (4, 2);
