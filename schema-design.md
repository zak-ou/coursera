# MySQL Database Schema Design - Smart Clinic Management System

## Overview
This document describes the relational database schema for the Smart Clinic Management System. The schema is designed to support three main user roles (Admin, Doctor, Patient) and their interactions within a clinical environment.

## Entity Relationship Diagram (ERD)

```
┌─────────────┐       ┌──────────────┐       ┌─────────────┐
│   Admin     │       │   Doctor     │       │   Patient   │
├─────────────┤       ├──────────────┤       ├─────────────┤
│ id (PK)     │       │ id (PK)      │       │ id (PK)     │
│ name        │       │ name         │       │ name        │
│ email       │       │ email        │       │ email       │
│ password    │       │ password     │       │ password    │
│ phone       │       │ phone        │       │ phone       │
│ created_at  │       │ specialization│      │ date_of_birth│
└─────────────┘       │ available_times│     │ address     │
                      │ created_at   │       │ created_at  │
                      └──────┬───────┘       └──────┬──────┘
                             │                      │
                             │      ┌───────────────┤
                             │      │               │
                             ├──────▼───────┐       │
                             │ Appointment  │◄──────┘
                             ├──────────────┤
                             │ id (PK)      │
                             │ doctor_id(FK)│
                             │ patient_id(FK)│
                             │ appointment_time│
                             │ status       │
                             │ reason       │
                             │ created_at   │
                             └──────┬───────┘
                                    │
                             ┌──────▼───────┐
                             │ Prescription │
                             ├──────────────┤
                             │ id (PK)      │
                             │ appointment_id(FK)│
                             │ doctor_id(FK)│
                             │ patient_id(FK)│
                             │ medication   │
                             │ dosage       │
                             │ frequency    │
                             │ duration     │
                             │ created_at   │
                             └──────────────┘
```

## Table Definitions

### 1. Admin Table
Stores administrator account information.

```sql
CREATE TABLE admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Fields:**
- `id`: Primary key, auto-incrementing identifier
- `name`: Administrator's full name
- `email`: Unique email address for login
- `password`: Hashed password (bcrypt)
- `phone`: Contact phone number
- `created_at`: Timestamp of account creation
- `updated_at`: Timestamp of last update

---

### 2. Doctor Table
Stores doctor profiles and availability information.

```sql
CREATE TABLE doctor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    specialization VARCHAR(100) NOT NULL,
    available_times TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_email (email),
    INDEX idx_specialization (specialization)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Fields:**
- `id`: Primary key, auto-incrementing identifier
- `name`: Doctor's full name
- `email`: Unique email address for login
- `password`: Hashed password (bcrypt)
- `phone`: Contact phone number
- `specialization`: Medical specialization (e.g., Cardiology, Pediatrics)
- `available_times`: JSON string storing available time slots
- `created_at`: Timestamp of account creation
- `updated_at`: Timestamp of last update

**Relationships:**
- One-to-Many with Appointment (one doctor has many appointments)
- One-to-Many with Prescription (one doctor writes many prescriptions)

---

### 3. Patient Table
Stores patient account and personal information.

```sql
CREATE TABLE patient (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    date_of_birth DATE,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_email (email),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Fields:**
- `id`: Primary key, auto-incrementing identifier
- `name`: Patient's full name
- `email`: Unique email address for login
- `password`: Hashed password (bcrypt)
- `phone`: Contact phone number
- `date_of_birth`: Patient's date of birth
- `address`: Residential address
- `created_at`: Timestamp of account creation
- `updated_at`: Timestamp of last update

**Relationships:**
- One-to-Many with Appointment (one patient has many appointments)
- One-to-Many with Prescription (one patient receives many prescriptions)

---

### 4. Appointment Table
Stores appointment bookings between patients and doctors.

```sql
CREATE TABLE appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    appointment_time DATETIME NOT NULL,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_patient_id (patient_id),
    INDEX idx_appointment_time (appointment_time),
    INDEX idx_status (status),
    
    CONSTRAINT unique_appointment UNIQUE (doctor_id, appointment_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Fields:**
- `id`: Primary key, auto-incrementing identifier
- `doctor_id`: Foreign key referencing doctor.id
- `patient_id`: Foreign key referencing patient.id
- `appointment_time`: Date and time of the appointment
- `status`: Appointment status (SCHEDULED, COMPLETED, CANCELLED, NO_SHOW)
- `reason`: Patient's reason for visit
- `created_at`: Timestamp of booking
- `updated_at`: Timestamp of last update

**Relationships:**
- Many-to-One with Doctor (many appointments belong to one doctor)
- Many-to-One with Patient (many appointments belong to one patient)
- One-to-Many with Prescription (one appointment can have many prescriptions)

**Constraints:**
- Unique constraint on (doctor_id, appointment_time) prevents double-booking

---

### 5. Prescription Table
Stores medication prescriptions issued by doctors to patients.

```sql
CREATE TABLE prescription (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT,
    doctor_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    medication VARCHAR(200) NOT NULL,
    dosage VARCHAR(100) NOT NULL,
    frequency VARCHAR(100) NOT NULL,
    duration VARCHAR(100) NOT NULL,
    instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (appointment_id) REFERENCES appointment(id) ON DELETE SET NULL,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    
    INDEX idx_appointment_id (appointment_id),
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_patient_id (patient_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Fields:**
- `id`: Primary key, auto-incrementing identifier
- `appointment_id`: Foreign key referencing appointment.id (nullable)
- `doctor_id`: Foreign key referencing doctor.id
- `patient_id`: Foreign key referencing patient.id
- `medication`: Name of prescribed medication
- `dosage`: Dosage amount (e.g., "500mg")
- `frequency`: How often to take (e.g., "twice daily")
- `duration`: How long to take (e.g., "10 days")
- `instructions`: Additional instructions for patient
- `created_at`: Timestamp of prescription creation
- `updated_at`: Timestamp of last update

**Relationships:**
- Many-to-One with Appointment (many prescriptions can reference one appointment)
- Many-to-One with Doctor (many prescriptions written by one doctor)
- Many-to-One with Patient (many prescriptions for one patient)

---

## Stored Procedures

### 1. GetDailyAppointmentReportByDoctor
Returns all appointments for a specific doctor on a given date.

```sql
DELIMITER $$

CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
    IN doctor_id_param BIGINT,
    IN report_date DATE
)
BEGIN
    SELECT 
        a.id AS appointment_id,
        p.name AS patient_name,
        p.email AS patient_email,
        p.phone AS patient_phone,
        a.appointment_time,
        a.status,
        a.reason,
        a.created_at AS booked_at
    FROM appointment a
    INNER JOIN patient p ON a.patient_id = p.id
    WHERE a.doctor_id = doctor_id_param
        AND DATE(a.appointment_time) = report_date
    ORDER BY a.appointment_time ASC;
END$$

DELIMITER ;
```

**Usage:**
```sql
CALL GetDailyAppointmentReportByDoctor(1, '2024-01-15');
```

---

### 2. GetDoctorWithMostPatientsByMonth
Identifies the doctor with the most unique patients in a given month.

```sql
DELIMITER $$

CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(
    IN report_year INT,
    IN report_month INT
)
BEGIN
    SELECT 
        d.id AS doctor_id,
        d.name AS doctor_name,
        d.specialization,
        COUNT(DISTINCT a.patient_id) AS unique_patients,
        COUNT(a.id) AS total_appointments
    FROM doctor d
    INNER JOIN appointment a ON d.id = a.doctor_id
    WHERE YEAR(a.appointment_time) = report_year
        AND MONTH(a.appointment_time) = report_month
    GROUP BY d.id, d.name, d.specialization
    ORDER BY unique_patients DESC, total_appointments DESC
    LIMIT 1;
END$$

DELIMITER ;
```

**Usage:**
```sql
CALL GetDoctorWithMostPatientsByMonth(2024, 1);
```

---

### 3. GetDoctorWithMostPatientsByYear
Identifies the doctor with the most unique patients in a given year.

```sql
DELIMITER $$

CREATE PROCEDURE GetDoctorWithMostPatientsByYear(
    IN report_year INT
)
BEGIN
    SELECT 
        d.id AS doctor_id,
        d.name AS doctor_name,
        d.specialization,
        COUNT(DISTINCT a.patient_id) AS unique_patients,
        COUNT(a.id) AS total_appointments
    FROM doctor d
    INNER JOIN appointment a ON d.id = a.doctor_id
    WHERE YEAR(a.appointment_time) = report_year
    GROUP BY d.id, d.name, d.specialization
    ORDER BY unique_patients DESC, total_appointments DESC
    LIMIT 1;
END$$

DELIMITER ;
```

**Usage:**
```sql
CALL GetDoctorWithMostPatientsByYear(2024);
```

---

## Sample Data

### Sample Admin
```sql
INSERT INTO admin (name, email, password, phone) VALUES
('System Admin', 'admin@clinic.com', '$2a$10$...', '+1234567890');
```

### Sample Doctors
```sql
INSERT INTO doctor (name, email, password, phone, specialization, available_times) VALUES
('Dr. John Smith', 'john.smith@clinic.com', '$2a$10$...', '+1234567891', 'Cardiology', '{"Monday": ["09:00-12:00", "14:00-17:00"], "Wednesday": ["09:00-12:00"]}'),
('Dr. Sarah Johnson', 'sarah.johnson@clinic.com', '$2a$10$...', '+1234567892', 'Pediatrics', '{"Tuesday": ["08:00-12:00"], "Thursday": ["13:00-17:00"]}'),
('Dr. Michael Brown', 'michael.brown@clinic.com', '$2a$10$...', '+1234567893', 'Dermatology', '{"Monday": ["10:00-13:00"], "Friday": ["09:00-16:00"]}');
```

### Sample Patients
```sql
INSERT INTO patient (name, email, password, phone, date_of_birth, address) VALUES
('Alice Williams', 'alice@email.com', '$2a$10$...', '+1234567894', '1985-03-15', '123 Main St, City'),
('Bob Davis', 'bob@email.com', '$2a$10$...', '+1234567895', '1990-07-22', '456 Oak Ave, City'),
('Carol Miller', 'carol@email.com', '$2a$10$...', '+1234567896', '1978-11-30', '789 Pine Rd, City');
```

---

## Indexing Strategy

The schema includes strategic indexes to optimize common queries:

1. **Email indexes** on all user tables for fast authentication lookups
2. **Foreign key indexes** for efficient JOIN operations
3. **Specialization index** on doctor table for filtering by specialty
4. **Phone index** on patient table for contact lookup
5. **Appointment time index** for schedule queries
6. **Status index** on appointments for filtering by status

---

## Data Integrity & Constraints

1. **Foreign Key Constraints**: Ensure referential integrity between tables
2. **Unique Constraints**: Prevent duplicate emails and double-booking
3. **NOT NULL Constraints**: Ensure required fields are always populated
4. **ON DELETE CASCADE**: Automatically clean up related records when parent is deleted
5. **Default Values**: Provide sensible defaults for timestamps and status fields

---

## Security Considerations

1. Passwords are stored as bcrypt hashes, never in plain text
2. Email addresses are unique to prevent account conflicts
3. Sensitive data uses appropriate character sets (utf8mb4) to support international characters
4. Indexes on email fields support fast authentication without exposing password columns

---

## Scalability Notes

The schema is designed to handle growth:
- Auto-incrementing IDs support billions of records
- Indexes optimize query performance as data grows
- TEXT fields for flexible data (available_times, instructions) allow future enhancements
- Timestamp fields enable audit trails and data analysis

---

## Future Enhancements

Potential schema extensions:
1. Medical records table for detailed patient history
2. Billing table for payment tracking
3. Reviews/ratings table for doctor feedback
4. Notification table for appointment reminders
5. Audit log table for compliance and security
