#!/bin/bash
# ════════════════════════════════════════════════════════════
#  build.sh — Linux/macOS build script
#  Scholarship Eligibility Portal
# ════════════════════════════════════════════════════════════

set -e  # Exit on any error

echo "============================================="
echo "  Scholarship Portal — Build Script (Linux)  "
echo "============================================="

# ── Paths ────────────────────────────────────────────────
LIB_DIR="lib"
SRC_DIR="src/main/java"
OUT_DIR="out/classes"
MAIN_CLASS="com.scholarship.service.ServicePublisher"

# ── Create output directory ──────────────────────────────
mkdir -p "$OUT_DIR"

# ── Build classpath ──────────────────────────────────────
CP="$OUT_DIR:$LIB_DIR/*"

# ── Compile ──────────────────────────────────────────────
echo ""
echo "[1/3] Compiling Java sources..."
javac -cp "$LIB_DIR/*" -d "$OUT_DIR" \
  "$SRC_DIR/com/scholarship/util/AppLogger.java" \
  "$SRC_DIR/com/scholarship/db/DatabaseUtil.java" \
  "$SRC_DIR/com/scholarship/model/Student.java" \
  "$SRC_DIR/com/scholarship/model/Scholarship.java" \
  "$SRC_DIR/com/scholarship/model/EligibilityResponse.java" \
  "$SRC_DIR/com/scholarship/service/ScholarshipService.java" \
  "$SRC_DIR/com/scholarship/service/ScholarshipServiceImpl.java" \
  "$SRC_DIR/com/scholarship/service/UDDIPublisher.java" \
  "$SRC_DIR/com/scholarship/service/ServicePublisher.java" \
  "$SRC_DIR/com/scholarship/service/ScholarshipClient.java"

echo "✅ Compilation successful."

# ── UDDI registration ─────────────────────────────────
echo ""
echo "[2/3] Registering service with UDDI (simulated)..."
java -cp "$CP" com.scholarship.service.UDDIPublisher

# ── Start server ─────────────────────────────────────
echo ""
echo "[3/3] Starting SOAP server..."
echo "       Service  : http://localhost:8888/scholarship/service"
echo "       WSDL     : http://localhost:8888/scholarship/service?wsdl"
echo ""
java -cp "$CP" "$MAIN_CLASS"
