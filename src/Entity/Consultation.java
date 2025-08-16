package Entity;

import java.time.LocalDateTime;

/**
 * Consultation entity to store consultation appointment and record data
 */
public class Consultation {
    private String consultationId;
    private String patientId;
    private String doctorId;
    private LocalDateTime appointmentDateTime;
    private String status; // SCHEDULED, COMPLETED, CANCELLED, RESCHEDULED
    private String symptoms;
    private String diagnosis;
    private String prescription;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String followUpConsultationId; // NEW: Link to follow-up consultation
    
    // Constructor for creating new consultation
    public Consultation(String consultationId, String patientId, String doctorId, 
                       LocalDateTime appointmentDateTime, String status) {
        this.consultationId = consultationId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.followUpConsultationId = null;
    }
    
    // Constructor for consultation with medical record
    public Consultation(String consultationId, String patientId, String doctorId, 
                       LocalDateTime appointmentDateTime, String status, 
                       String symptoms, String diagnosis, String prescription, String notes) {
        this.consultationId = consultationId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.followUpConsultationId = null;
    }
    
    // Getters
    public String getConsultationId() { return consultationId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public String getStatus() { return status; }
    public String getSymptoms() { return symptoms; }
    public String getDiagnosis() { return diagnosis; }
    public String getPrescription() { return prescription; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getFollowUpConsultationId() { return followUpConsultationId; }
    
    // Setters
    public void setConsultationId(String consultationId) { this.consultationId = consultationId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { 
        this.appointmentDateTime = appointmentDateTime; 
    }
    public void setStatus(String status) { 
        this.status = status; 
        this.updatedAt = LocalDateTime.now();
    }
    public void setSymptoms(String symptoms) { 
        this.symptoms = symptoms; 
        this.updatedAt = LocalDateTime.now();
    }
    public void setDiagnosis(String diagnosis) { 
        this.diagnosis = diagnosis; 
        this.updatedAt = LocalDateTime.now();
    }
    public void setPrescription(String prescription) { 
        this.prescription = prescription; 
        this.updatedAt = LocalDateTime.now();
    }
    public void setNotes(String notes) { 
        this.notes = notes; 
        this.updatedAt = LocalDateTime.now();
    }
    public void setFollowUpConsultationId(String followUpConsultationId) { 
        this.followUpConsultationId = followUpConsultationId; 
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return String.format("Consultation ID: %s | Patient: %s | Doctor: %s | Date: %s | Status: %s",
                consultationId, patientId, doctorId, 
                appointmentDateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), 
                status);
    }
} 