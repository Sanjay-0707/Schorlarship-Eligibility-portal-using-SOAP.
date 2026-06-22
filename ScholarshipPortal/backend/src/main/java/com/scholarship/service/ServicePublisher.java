package com.scholarship.service;

import com.scholarship.util.AppLogger;
import javax.xml.ws.Endpoint;
import java.util.logging.Logger;

/**
 * ServicePublisher.java
 * ─────────────────────────────────────────────────────────────
 * Standalone JAX-WS endpoint publisher.
 *
 * ▶ Run this class directly (java ServicePublisher) to start
 *   the SOAP web service without needing Apache Tomcat.
 *
 * Service URL : http://localhost:8888/scholarship/service
 * WSDL URL    : http://localhost:8888/scholarship/service?wsdl
 * ─────────────────────────────────────────────────────────────
 */
public class ServicePublisher {

    private static final Logger LOG = AppLogger.getLogger(ServicePublisher.class.getName());

    /** SOAP endpoint address */
    public static final String ENDPOINT_URL =
            "http://localhost:8888/scholarship/service";

    public static void main(String[] args) {

        LOG.info("════════════════════════════════════════════════");
        LOG.info("  Scholarship Eligibility Portal — SOAP Server  ");
        LOG.info("════════════════════════════════════════════════");

        try {
            // Publish the service endpoint
            Endpoint endpoint = Endpoint.publish(ENDPOINT_URL, new ScholarshipServiceImpl());

            if (endpoint.isPublished()) {
                LOG.info("✅ Service published successfully!");
                LOG.info("🔗 Service  : " + ENDPOINT_URL);
                LOG.info("📄 WSDL     : " + ENDPOINT_URL + "?wsdl");
                LOG.info("⏳ Press Ctrl+C to stop the server.");
                LOG.info("════════════════════════════════════════════════");
            } else {
                LOG.severe("❌ Service failed to publish.");
                System.exit(1);
            }

            // Keep the JVM alive (block main thread)
            Thread.currentThread().join();

        } catch (InterruptedException e) {
            LOG.info("🛑 Server shutting down...");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            LOG.severe("❌ Fatal error starting server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
