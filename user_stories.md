# User Stories - Smart Clinic Management System

## Admin User Stories

### Epic: Doctor Management
**As an** Administrator  
**I want to** add new doctors to the system  
**So that** patients can book appointments with them

**Acceptance Criteria:**
- Admin can enter doctor's name, email, phone, specialization, and available times
- System validates all required fields before saving
- Success message is displayed after successful addition
- New doctor appears in the doctor list immediately

---

**As an** Administrator  
**I want to** view all doctors in the system  
**So that** I can manage and monitor the medical staff

**Acceptance Criteria:**
- Admin can see a list of all registered doctors
- List displays doctor name, specialization, email, and phone
- Admin can search/filter doctors by specialization
- List updates in real-time when changes are made

---

**As an** Administrator  
**I want to** update doctor information  
**So that** I can keep the system data current and accurate

**Acceptance Criteria:**
- Admin can edit doctor details (name, email, phone, specialization, availability)
- System validates changes before saving
- Updated information reflects immediately in the system
- Audit log records who made the changes and when

---

**As an** Administrator  
**I want to** remove doctors from the system  
**So that** inactive or departed staff are no longer bookable

**Acceptance Criteria:**
- Admin can delete doctor records
- System warns about existing appointments before deletion
- Deleted doctors are archived, not permanently removed
- Patients cannot book new appointments with deleted doctors

---

### Epic: System Reporting
**As an** Administrator  
**I want to** view daily appointment reports by doctor  
**So that** I can monitor clinic utilization and workload

**Acceptance Criteria:**
- Report shows all appointments for a selected date and doctor
- Includes patient name, appointment time, and status
- Can export report to PDF/Excel
- Report updates automatically as appointments change

---

**As an** Administrator  
**I want to** identify the doctor with the most patients per month  
**So that** I can recognize high-performing staff and balance workload

**Acceptance Criteria:**
- Report shows doctors ranked by patient count for selected month
- Displays total patient count and appointment count
- Can compare across multiple months
- Visual chart shows distribution

---

**As an** Administrator  
**I want to** identify the doctor with the most patients per year  
**So that** I can make informed decisions about staffing and resources

**Acceptance Criteria:**
- Annual report shows top doctors by patient volume
- Includes year-over-year comparison
- Shows trends and growth patterns
- Can filter by specialization

---

## Doctor User Stories

### Epic: Appointment Management
**As a** Doctor  
**I want to** view all my appointments  
**So that** I can see my schedule and prepare for patient visits

**Acceptance Criteria:**
- Doctor sees list of all upcoming appointments
- Each appointment shows patient name, time, and reason for visit
- Appointments are sorted chronologically
- Can filter by date range
- Past appointments are also accessible

---

**As a** Doctor  
**I want to** set my available time slots  
**So that** patients can only book when I'm available

**Acceptance Criteria:**
- Doctor can specify available days and time slots
- Can block out unavailable times (holidays, meetings)
- Changes take effect immediately
- Patients cannot book outside available times
- Recurring availability patterns can be set

---

**As a** Doctor  
**I want to** view patient medical history  
**So that** I can provide informed care during appointments

**Acceptance Criteria:**
- Doctor can access patient's previous visits
- View past prescriptions and diagnoses
- See medical notes from previous doctors
- Information is organized chronologically
- Search functionality for specific conditions/medications

---

### Epic: Prescription Management
**As a** Doctor  
**I want to** create prescriptions for patients  
**So that** patients can receive proper medication guidance

**Acceptance Criteria:**
- Doctor can enter medication name, dosage, frequency, and duration
- Can add multiple medications to one prescription
- System validates medication names and dosages
- Prescription is linked to specific appointment
- Patient receives notification when prescription is ready
- Prescription can be printed or sent electronically

---

**As a** Doctor  
**I want to** view all prescriptions I've written  
**So that** I can track patient medication history

**Acceptance Criteria:**
- Doctor sees list of all prescriptions written
- Can filter by patient, date, or medication
- Can view prescription details
- Shows whether prescription has been filled

---

## Patient User Stories

### Epic: Doctor Discovery
**As a** Patient  
**I want to** search for doctors by name  
**So that** I can find a specific doctor I want to see

**Acceptance Criteria:**
- Patient can enter doctor name in search field
- Search results display matching doctors
- Shows doctor's specialization and availability
- Partial name matches are supported
- Results update as patient types

---

**As a** Patient  
**I want to** filter doctors by specialization  
**So that** I can find the right type of doctor for my needs

**Acceptance Criteria:**
- Patient can select from list of specializations
- System shows all doctors with selected specialization
- Each result shows doctor name, availability, and rating
- Can combine specialization filter with other filters

---

**As a** Patient  
**I want to** see doctor availability  
**So that** I can choose a convenient appointment time

**Acceptance Criteria:**
- Available time slots are clearly marked
- Unavailable times are grayed out or hidden
- Can view availability for multiple days
- Shows doctor's next available slot prominently

---

### Epic: Appointment Booking
**As a** Patient  
**I want to** book an appointment with a doctor  
**So that** I can receive medical care

**Acceptance Criteria:**
- Patient selects doctor, date, and time
- Can provide reason for visit (optional)
- System confirms booking immediately
- Confirmation email/SMS is sent
- Appointment appears in patient's appointment list
- Cannot double-book the same time slot

---

**As a** Patient  
**I want to** view all my appointments  
**So that** I can keep track of my scheduled visits

**Acceptance Criteria:**
- Patient sees list of upcoming and past appointments
- Each appointment shows doctor name, date, time, and status
- Can filter by upcoming/past/cancelled
- Reminders are sent before appointment time

---

**As a** Patient  
**I want to** cancel an appointment  
**So that** I can free up the slot if I cannot attend

**Acceptance Criteria:**
- Patient can cancel upcoming appointments
- Cancellation requires confirmation
- Time slot becomes available for other patients
- Doctor is notified of cancellation
- Cancellation reason can be provided (optional)

---

### Epic: Medical Records Access
**As a** Patient  
**I want to** view my prescriptions  
**So that** I can track my medications and follow doctor's orders

**Acceptance Criteria:**
- Patient sees list of all prescriptions
- Each prescription shows medication, dosage, doctor, and date
- Can view prescription details and instructions
- Shows active vs. completed prescriptions
- Can download/print prescription

---

**As a** Patient  
**I want to** view my appointment history  
**So that** I can track my medical visits and care

**Acceptance Criteria:**
- Patient sees chronological list of past appointments
- Each entry shows doctor, date, and diagnosis (if any)
- Can view notes and prescriptions from each visit
- Search functionality available
- Can export history to PDF

---

## Authentication & Security Stories

**As a** User (Admin/Doctor/Patient)  
**I want to** securely log in to my portal  
**So that** my information remains private and secure

**Acceptance Criteria:**
- User enters email and password
- System validates credentials
- JWT token is issued upon successful login
- Session expires after inactivity
- Invalid attempts are logged
- Password must meet security requirements

---

**As a** User (Admin/Doctor/Patient)  
**I want to** log out of the system  
**So that** others cannot access my account from the same device

**Acceptance Criteria:**
- Logout button is accessible from all pages
- Session is immediately terminated
- JWT token is invalidated
- User is redirected to login page
- Confirmation message is displayed

---

## Summary

This Smart Clinic Management System provides comprehensive functionality for three user roles:
- **Administrators**: Manage doctors and generate reports
- **Doctors**: Manage appointments and prescriptions
- **Patients**: Find doctors, book appointments, and access medical records

All user stories follow the format: "As a [role], I want to [action], so that [benefit]" and include clear acceptance criteria for implementation and testing.
