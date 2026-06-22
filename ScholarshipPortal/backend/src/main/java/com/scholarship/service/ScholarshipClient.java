package com.scholarship.service;

import com.scholarship.model.EligibilityResponse;
import com.scholarship.model.Scholarship;
import com.scholarship.util.AppLogger;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.logging.Logger;

/**
 * ScholarshipClient.java  ← Service Consumer (Java Client)
 * ─────────────────────────────────────────────────────────────
 * Demonstrates how a Java consumer discovers the service
 * via UDDI and invokes the SOAP checkEligibility operation.
 *
 * ▶ Run AFTER ServicePublisher is already running.
 * ─────────────────────────────────────────────────────────────
 */
public class ScholarshipClient {

    private static final Logger LOG = AppLogger.getLogger(ScholarshipClient.class.getName());

    public static void main(String[] args) throws Exception {

        LOG.info("════════════════════════════════════════════════");
        LOG.info("  Scholarship Portal — Java SOAP Client Test   ");
        LOG.info("════════════════════════════════════════════════");

        // ── Step 1: UDDI Discovery ────────────────────────────
        String wsdlUrl = UDDIPublisher.discoverService("ScholarshipEligibilityService");
        if (wsdlUrl == null) {
            wsdlUrl = ServicePublisher.ENDPOINT_URL + "?wsdl";
            LOG.info("ℹ Using fallback WSDL URL: " + wsdlUrl);
        }

        // ── Step 2: Create proxy via WSDL ────────────────────
        URL            url   = new URL(wsdlUrl);
        QName          qname = new QName("http://scholarship.com/service",
                                         "ScholarshipEligibilityService");
        Service        svc   = Service.create(url, qname);
        ScholarshipService port = svc.getPort(ScholarshipService.class);

        LOG.info("✅ SOAP proxy created. Sending test requests...\n");

        // ── Step 3: Test cases ────────────────────────────────
        testStudent(port, "Rahul Kumar",  88.5, 120000, "SC");
        testStudent(port, "Priya Sharma", 92.0, 550000, "GEN");
        testStudent(port, "Amit Patel",   67.5, 180000, "OBC");
        testStudent(port, "Sneha Reddy",  75.0, 220000, "ST");
        testStudent(port, "Invalid User", -5,  -1000,  "XYZ"); // Validation test
    }

    private static void testStudent(ScholarshipService port, String name,
                                     double marks, double income, String category) {
        System.out.println("\n──────────────────────────────────────────");
        System.out.printf("📤 Checking: %s | %.1f%% | ₹%.0f | %s%n",
                name, marks, income, category);

        EligibilityResponse resp = port.checkEligibility(name, marks, income, category);

        System.out.println("📥 Message  : " + resp.getMessage());
        System.out.println("✅ Eligible : " + resp.isEligible());

        if (resp.isEligible()) {
            System.out.println("🎓 Scholarships:");
            for (Scholarship s : resp.getScholarships()) {
                System.out.printf("   • [%s] %s — ₹%.0f/year%n",
                        s.getSchemeCode(), s.getName(), s.getAmountPerYear());
            }
        }
    }
}
