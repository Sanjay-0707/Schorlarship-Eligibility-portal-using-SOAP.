# Scholarship Eligibility Portal

A comprehensive web-based application for managing and determining scholarship eligibility for students. This project features a Java-based SOAP web service backend, a MySQL database, and a responsive HTML frontend.

## 📋 Project Overview

The Scholarship Eligibility Portal is an **MCA Final Year Project** designed to:
- Allow students to submit their application details (name, email, marks, family income, category)
- Determine scholarship eligibility based on predefined criteria
- Manage scholarship records and eligibility results
- Provide a SOAP-based web service API for integration with other systems

## 🏗️ Architecture

### Technology Stack
- **Backend:** Java (SOAP Web Services with JAX-WS)
- **Database:** MySQL 8.x
- **Frontend:** HTML5, CSS3
- **Build Tool:** Custom build scripts (build.bat, build.sh)

### Project Structure
```
ScholarshipPortal/
├── backend/                      # Java backend service
│   ├── src/main/java/
│   │   └── com/scholarship/
│   │       ├── db/              # Database utilities
│   │       ├── model/           # Data models (Student, Scholarship, etc.)
│   │       ├── service/         # Business logic & SOAP services
│   │       └── util/            # Utility classes (logging, etc.)
│   ├── src/main/webapp/         # Web configuration
│   │   └── WEB-INF/
│   │       ├── sun-jaxws.xml   # JAX-WS configuration
│   │       └── web.xml          # Deployment descriptor
│   ├── lib/                     # External libraries (place MySQL JAR here)
│   ├── build.bat                # Windows build script
│   └── build.sh                 # Unix/Linux build script
├── database/                    # Database schema and scripts
│   └── schema.sql               # Database initialization script
├── frontend/                    # Web UI
│   └── index.html               # Main portal page
└── docs/                        # Documentation
    └── soap_request_sample.xml  # Example SOAP requests
```

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher (with `--add-opens` support)
- MySQL Server 8.0+
- MySQL JDBC driver JAR file

### Installation & Setup

#### 1. Database Setup
```bash
# Connect to MySQL
mysql -u root -p

# Run the schema script
mysql> source database/schema.sql

# Verify tables were created
mysql> use scholarship_db;
mysql> show tables;
```

#### 2. Backend Setup
```bash
# Place MySQL Connector JAR in the backend/lib directory
# Rename: lib/PUT_MYSQL_CONNECTOR_JAR_HERE.txt

# Build the project (Windows)
cd backend
build.bat

# Or for Unix/Linux
./build.sh
```

#### 3. Run the Backend Service
```bash
java --add-opens java.base/jdk.internal.misc=ALL-UNNAMED \
  -cp "out/classes;lib/*" \
  com.scholarship.service.ServicePublisher
```

The SOAP service will start and be available for client requests.

#### 4. Access the Frontend
Open `frontend/index.html` in your web browser to access the scholarship portal UI.

## 📚 Database Schema

### Tables

#### `students`
Stores student application details:
- `id` - Auto-increment primary key
- `full_name` - Student name (max 100 chars)
- `email` - Student email (unique)
- `marks` - Percentage marks (0-100)
- `annual_income` - Family annual income (INR)
- `category` - Category (GEN, OBC, SC, ST)
- `applied_on` - Application timestamp

#### `scholarships`
Stores available scholarship schemes

#### `eligibility_results`
Stores scholarship eligibility determination results

## 🔌 API Integration

The backend exposes SOAP web services for:
- Checking scholarship eligibility
- Retrieving student applications
- Managing scholarship records
- Publishing eligibility results

See `docs/soap_request_sample.xml` for example SOAP request formats.

## 📝 Key Components

### Backend Services
- **ScholarshipService** - SOAP service interface definition
- **ScholarshipServiceImpl** - Implementation of scholarship eligibility logic
- **ScholarshipClient** - Client for calling the SOAP service
- **ServicePublisher** - Publishes the SOAP service endpoint
- **UDDIPublisher** - UDDI registry integration

### Database Utilities
- **DatabaseUtil** - Handles database connections and queries

### Models
- **Student** - Student data entity
- **Scholarship** - Scholarship data entity
- **EligibilityResponse** - Response model for eligibility checks

### Utilities
- **AppLogger** - Application logging

## 🛠️ Build & Compilation

### Windows
```bash
cd backend
build.bat
```

### Unix/Linux/macOS
```bash
cd backend
chmod +x build.sh
./build.sh
```

Compiled classes will be placed in `backend/out/classes/`

## 📋 Development Notes

- Ensure MySQL connector JAR is placed in `backend/lib/`
- The application uses JAX-WS for SOAP web services
- Database connection details should be configured in `DatabaseUtil.java`
- Logs are stored in `scholarship.log`

## 🔐 Important Configuration

Before running the backend:
1. Update database credentials in the backend code if needed
2. Ensure MySQL server is running
3. Verify the database `scholarship_db` exists with proper schema
4. Place the MySQL JDBC driver in `backend/lib/`

## 📄 Documentation

Additional documentation is available in:
- `docs/soap_request_sample.xml` - Example SOAP requests
- `docs/soap_response_sample.xml` - Example SOAP responses
- `docs/whattodo.txt` - Setup instructions
- Backend code comments and JavaDoc

## 👥 Team

**Project Type:** MCA Final Year Project  
**Database:** MySQL 8.x  
**Architecture:** Java SOAP Web Services

## 📜 License

[Add your license here - e.g., MIT, Apache 2.0, etc.]

## 🤝 Contributing

[Add contribution guidelines here if applicable]

## 🐛 Troubleshooting

### Port Already in Bind
If the service fails to start with a port bind error, change the port in `ServicePublisher.java`

### Database Connection Issues
- Verify MySQL server is running
- Check database credentials
- Ensure database schema exists: `mysql -u root -p scholarship_db < database/schema.sql`

### Build Errors
- Ensure Java 11+ is installed
- Verify MySQL JAR is in `backend/lib/`
- Check that `WEB-INF/web.xml` is properly configured

---

**Last Updated:** June 2026  
**Version:** 1.0.0
