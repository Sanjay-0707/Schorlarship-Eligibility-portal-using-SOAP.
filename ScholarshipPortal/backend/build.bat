@echo off
REM ════════════════════════════════════════════════════════════
REM  build.bat — Windows build script
REM  Scholarship Eligibility Portal
REM ════════════════════════════════════════════════════════════

echo =============================================
echo   Scholarship Portal — Build Script
echo =============================================

REM ── Set paths (edit if needed) ───────────────────────────
set JAVA_HOME=C:\Program Files\Java\jdk-17
set LIB_DIR=lib
set SRC_DIR=src\main\java
set OUT_DIR=out\classes
set MAIN_CLASS=com.scholarship.service.ServicePublisher

REM ── Create output directory ──────────────────────────────
if not exist %OUT_DIR% mkdir %OUT_DIR%

REM ── Compile ──────────────────────────────────────────────
echo.
echo [1/3] Compiling Java sources...
javac -cp "%LIB_DIR%\*" -d %OUT_DIR% ^
  %SRC_DIR%\com\scholarship\util\AppLogger.java ^
  %SRC_DIR%\com\scholarship\db\DatabaseUtil.java ^
  %SRC_DIR%\com\scholarship\model\Student.java ^
  %SRC_DIR%\com\scholarship\model\Scholarship.java ^
  %SRC_DIR%\com\scholarship\model\EligibilityResponse.java ^
  %SRC_DIR%\com\scholarship\service\ScholarshipService.java ^
  %SRC_DIR%\com\scholarship\service\ScholarshipServiceImpl.java ^
  %SRC_DIR%\com\scholarship\service\UDDIPublisher.java ^
  %SRC_DIR%\com\scholarship\service\ServicePublisher.java ^
  %SRC_DIR%\com\scholarship\service\ScholarshipClient.java

if %ERRORLEVEL% neq 0 (
    echo ❌ Compilation FAILED. Check errors above.
    exit /b 1
)
echo ✅ Compilation successful.

REM ── Run ─────────────────────────────────────────────────
echo.
echo [2/3] Registering service with UDDI...
java -cp "%OUT_DIR%;%LIB_DIR%\*" com.scholarship.service.UDDIPublisher

echo.
echo [3/3] Starting SOAP server...
echo        Service  : http://localhost:8888/scholarship/service
echo        WSDL     : http://localhost:8888/scholarship/service?wsdl
echo.
java -cp "%OUT_DIR%;%LIB_DIR%\*" %MAIN_CLASS%
