# Smart Clinic Management System - Capstone Project

## 🏥 Project Overview

The Smart Clinic Management System is a comprehensive web application designed to streamline clinical operations by connecting administrators, doctors, and patients through an integrated digital platform. This capstone project demonstrates a complete software development lifecycle from requirements gathering to deployment.

## 📋 Project Structure

```
smart-clinic-management/
├── app/                                    # Backend Spring Boot Application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/project/back_end/
│   │   │   │   ├── controllers/          # REST API Controllers
│   │   │   │   │   ├── DoctorController.java
│   │   │   │   │   └── PrescriptionController.java
│   │   │   │   ├── models/               # JPA Entities
│   │   │   │   │   ├── Admin.java
│   │   │   │   │   ├── Doctor.java
│   │   │   │   │   ├── Patient.java
│   │   │   │   │   ├── Appointment.java
│   │   │   │   │   └── Prescription.java
│   │   │   │   ├── repo/                 # Data Repositories
│   │   │   │   │   ├── AdminRepository.java
│   │   │   │   │   ├── DoctorRepository.java
│   │   │   │   │   ├── PatientRepository.java
│   │   │   │   │   ├── AppointmentRepository.java
│   │   │   │   │   └── PrescriptionRepository.java
│   │   │   │   ├── services/             # Business Logic
│   │   │   │   │   ├── DoctorService.java
│   │   │   │   │   ├── TokenService.java
│   │   │   │   │   └── AppointmentService.java
│   │   │   │   └── Application.java      # Main Entry Point
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                         # Unit Tests
│   └── pom.xml                           # Maven Configuration
├── frontend/                              # Frontend Application
│   ├── admin/                            # Admin Portal
│   │   ├── index.html
│   │   ├── styles.css
│   │   └── script.js
│   ├── doctor/                           # Doctor Portal
│   │   ├── index.html
│   │   ├── styles.css
│   │   └── script.js
│   └── patient/                          # Patient Portal
│       ├── index.html
│       ├── styles.css
│       └── script.js
├── .github/workflows/                     # CI/CD Pipelines
│   └── compile-backend.yml               # GitHub Actions Workflow
├── Dockerfile                             # Docker Container Configuration
├── user_stories.md                        # User Stories Documentation
├── schema-design.md                       # Database Schema Design
└── README.md                              # This file

```

## 🎯 Submission Links (For Coursera)

### Question 1-12: Code Repository Files

**Q1: User Stories**
```
https://github.com/zak-ou/coursera/blob/main/user_stories.md
```

**Q2: Schema Design**
```
https://github.com/zak-ou/coursera/blob/main/schema-design.md
```

**Q3: Doctor.java**
```
https://github.com/zak-ou/coursera/blob/main/app/src/main/java/com/project/back_end/models/Doctor.java
```

**Q4: Appointment.java**
```
https://github.com/zak-ou/coursera/blob/main/app/src/main/java/com/project/back_end/models/Appointment.java
```

**Q5: DoctorController.java**
```
https://github.com/zak-ou/coursera/blob/main/app/src/main/java/com/project/back_end/controllers/DoctorController.java
```

**Q6: AppointmentService.java**
```
https://github.com/zak-ou/coursera/blob/main/app/src/main/java/com/project/back_end/services/AppointmentService.java
```

**Q7: PrescriptionController.java**
```
https://github.com/zak-ou/coursera/blob/main/app/src/main/java/com/project/back_end/controllers/PrescriptionController.java
```

**Q8: PatientRepository.java**
```
https://github.com/zak-ou/coursera/blob/main/app/src/main/java/com/project/back_end/repo/PatientRepository.java
```

**Q9: TokenService.java**
```
https://github.com/zak-ou/coursera/blob/main/app/src/main/java/com/project/back_end/services/TokenService.java
```

**Q10: DoctorService.java**
```
https://github.com/zak-ou/coursera/blob/main/app/src/main/java/com/project/back_end/services/DoctorService.java
```

**Q11: Dockerfile**
```
https://github.com/zak-ou/coursera/blob/main/Dockerfile
```

**Q12: GitHub Actions Workflow**
```
https://github.com/zak-ou/coursera/blob/main/.github/workflows/compile-backend.yml
```

## 🚀 Setup & Installation

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Docker (optional, for containerization)
- Git

### Database Setup

1. **Install MySQL** and create the database:

```sql
CREATE DATABASE smart_clinic;
USE smart_clinic;
```

2. **Run the schema creation scripts** (from `schema-design.md`):

```sql
-- Create tables
SOURCE schema-design.md;

-- Create stored procedures
DELIMITER $$

CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
    IN doctor_id_param BIGINT,
    IN report_date DATE
)
BEGIN
    SELECT 
        a.id AS appointment_id,
        p.name AS patient_name,
        a.appointment_time,
        a.status,
        a.reason
    FROM appointment a
    INNER JOIN patient p ON a.patient_id = p.id
    WHERE a.doctor_id = doctor_id_param
        AND DATE(a.appointment_time) = report_date
    ORDER BY a.appointment_time ASC;
END$$

-- Additional stored procedures...
DELIMITER ;
```

3. **Insert sample data**:

```sql
-- Sample Admin
INSERT INTO admin (name, email, password, phone) VALUES
('System Admin', 'admin@clinic.com', '$2a$10$encoded_password', '+1234567890');

-- Sample Doctors
INSERT INTO doctor (name, email, password, phone, specialization, available_times) VALUES
('Dr. John Smith', 'john.smith@clinic.com', '$2a$10$encoded_password', '+1234567891', 'Cardiology', '{"Monday": ["09:00-17:00"]}'),
('Dr. Sarah Johnson', 'sarah.johnson@clinic.com', '$2a$10$encoded_password', '+1234567892', 'Pediatrics', '{"Tuesday": ["08:00-16:00"]}');

-- Sample Patients
INSERT INTO patient (name, email, password, phone, date_of_birth, address) VALUES
('Alice Williams', 'alice@email.com', '$2a$10$encoded_password', '+1234567894', '1985-03-15', '123 Main St'),
('Bob Davis', 'bob@email.com', '$2a$10$encoded_password', '+1234567895', '1990-07-22', '456 Oak Ave');
```

### Backend Setup

1. **Clone the repository**:
```bash
git clone https://github.com/zak-ou/coursera.git
cd coursera
```

2. **Configure database** in `app/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smart_clinic
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

3. **Build and run**:
```bash
cd app
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8080/api`

### Docker Deployment

1. **Build Docker image**:
```bash
docker build -t smart-clinic-backend .
```

2. **Run container**:
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/smart_clinic \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  smart-clinic-backend
```

### Frontend Setup

1. **Open frontend files** in a web browser or use a local server:
```bash
cd frontend
python -m http.server 5500
```

2. **Access portals**:
- Admin Portal: `http://localhost:5500/admin/`
- Doctor Portal: `http://localhost:5500/doctor/`
- Patient Portal: `http://localhost:5500/patient/`

## 📊 Database Queries (For Questions 19-23)

### Q19: Show all tables
```sql
SHOW TABLES;
```

Expected output:
```
+-------------------------+
| Tables_in_smart_clinic  |
+-------------------------+
| admin                   |
| appointment             |
| doctor                  |
| patient                 |
| prescription            |
+-------------------------+
```

### Q20: Select 5 patients
```sql
SELECT * FROM patient LIMIT 5;
```

### Q21: Daily Appointment Report
```sql
CALL GetDailyAppointmentReportByDoctor(1, '2024-01-15');
```

### Q22: Doctor with Most Patients by Month
```sql
CALL GetDoctorWithMostPatientsByMonth(2024, 1);
```

### Q23: Doctor with Most Patients by Year
```sql
CALL GetDoctorWithMostPatientsByYear(2024);
```

## 🌐 API Testing with cURL (For Questions 24-26)

### Q24: GET all doctors
```bash
curl -X GET http://localhost:8080/api/doctor/all
```

### Q25: GET patient appointments (with authentication)
```bash
# First login to get token
curl -X POST http://localhost:8080/api/patient/login \
  -H "Content-Type: application/json" \
  -d '{"email": "alice@email.com", "password": "password123"}'

# Use the token to get appointments
curl -X GET http://localhost:8080/api/appointment/patient/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Q26: GET doctors by speciality
```bash
curl -X GET "http://localhost:8080/api/doctor/filter?specialization=Cardiology&time=09:00"
```

## ✅ Project Completion Checklist

### Code Requirements
- [x] User stories documented for all roles
- [x] MySQL schema with 5+ tables
- [x] JPA entities with proper annotations
- [x] @ManyToOne relationships configured
- [x] LocalDateTime with validations
- [x] Controller endpoints with token validation
- [x] Service layer with business logic
- [x] Repository custom queries
- [x] JWT token generation and validation
- [x] Multi-stage Dockerfile
- [x] GitHub Actions CI/CD workflow

### Documentation Requirements
- [x] User stories (user_stories.md)
- [x] Schema design (schema-design.md)
- [x] Stored procedures
- [x] API documentation
- [x] Setup instructions

### Frontend Requirements
- [x] Admin portal login screen
- [x] Doctor portal login screen
- [x] Patient portal login screen
- [x] Admin add doctor functionality
- [x] Patient search doctor functionality
- [x] Doctor view appointments functionality

## 🛠️ Technologies Used

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **ORM**: Hibernate/JPA
- **Security**: Spring Security + JWT
- **Database**: MySQL 8.0
- **Build Tool**: Maven 3.9
- **Testing**: JUnit 5, Mockito

### Frontend
- **Languages**: HTML5, CSS3, JavaScript (ES6+)
- **Styling**: Custom CSS
- **HTTP Client**: Fetch API

### DevOps
- **Containerization**: Docker
- **CI/CD**: GitHub Actions
- **Version Control**: Git/GitHub

## 📝 Key Features

### For Administrators
- ✅ Add, update, and remove doctors
- ✅ View all system users
- ✅ Generate daily appointment reports
- ✅ Identify top-performing doctors
- ✅ System-wide analytics

### For Doctors
- ✅ Set available time slots
- ✅ View appointment schedule
- ✅ Create prescriptions
- ✅ Access patient medical history
- ✅ Update profile information

### For Patients
- ✅ Search doctors by name or specialization
- ✅ View doctor availability
- ✅ Book appointments
- ✅ View appointment history
- ✅ Access prescriptions
- ✅ Cancel appointments

## 🔒 Security Features

- **Password Encryption**: BCrypt hashing
- **JWT Authentication**: Secure token-based auth
- **CORS Protection**: Configured allowed origins
- **Input Validation**: Bean validation on all inputs
- **SQL Injection Prevention**: Parameterized queries

## 🧪 Testing

Run tests with:
```bash
cd app
mvn test
```

## 📄 License

This project is for educational purposes as part of a Coursera Capstone Project.

## 👨‍💻 Author

Created for Coursera Java Database Capstone Project

## 🤝 Contributing

This is an academic project. For suggestions or issues, please open a GitHub issue.

---

**Note**: Remember to replace `YOUR_PASSWORD` and other placeholder values with actual credentials when deploying.
