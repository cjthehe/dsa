package Entity;

import java.time.LocalDateTime;

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
    private double consultationHr;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String followUpConsultationId; 
    
    // Validation constants
    private static final String[] VALID_STATUSES = {"SCHEDULED", "COMPLETED", "CANCELLED", "RESCHEDULED"};
    private static final int MIN_CONSULTATION_HOURS = 0;
    private static final int MAX_CONSULTATION_HOURS = 8;
    private static final int MIN_SYMPTOMS_LENGTH = 3;
    private static final int MAX_SYMPTOMS_LENGTH = 500;
    private static final int MIN_DIAGNOSIS_LENGTH = 3;
    private static final int MAX_DIAGNOSIS_LENGTH = 200;
    private static final int MAX_PRESCRIPTION_LENGTH = 300;
    private static final int MAX_NOTES_LENGTH = 1000;
    
    // Constructor
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
    
    public Consultation(String consultationId, String patientId, String doctorId, 
                       LocalDateTime appointmentDateTime, String status, String symptoms, 
                       String diagnosis, String prescription, String notes, double consultationHr) {
        this.consultationId = consultationId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
        this.consultationHr = consultationHr;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.followUpConsultationId = null;
    }
    
    // Validation methods
    public static boolean isValidConsultationId(String consultationId) {
        return consultationId != null && 
               consultationId.matches("^C\\d{9}$") && // C + 9 digits
               consultationId.length() == 10;
    }
    
    public static boolean isValidPatientId(String patientId) {
        return patientId != null && 
               patientId.matches("^P\\d{3}$") && // P + 3 digits
               patientId.length() == 4;
    }
    
    public static boolean isValidDoctorId(String doctorId) {
        return doctorId != null && 
               doctorId.matches("^D\\d{3}$") && // D + 3 digits
               doctorId.length() == 4;
    }
    
    public static boolean isValidAppointmentDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minDateTime = now.minusDays(1); // Allow appointments up to 1 day in the past
        
        // Check if appointment is not too far in the past
        if (dateTime.isBefore(minDateTime)) {
            return false;
        }
        
        // Check if appointment is not more than 1 year in the future
        LocalDateTime maxDateTime = now.plusYears(1);
        if (dateTime.isAfter(maxDateTime)) {
            return false;
        }
        
        return true;
    }
    
    public static boolean isValidStatus(String status) {
        if (status == null) return false;
        
        for (String validStatus : VALID_STATUSES) {
            if (validStatus.equals(status)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isValidSymptoms(String symptoms) {
        if (symptoms == null) return false;
        
        String trimmed = symptoms.trim();
        return trimmed.length() >= MIN_SYMPTOMS_LENGTH && 
               trimmed.length() <= MAX_SYMPTOMS_LENGTH &&
               !trimmed.isEmpty();
    }
    
    public static boolean isValidDiagnosis(String diagnosis) {
        if (diagnosis == null) return false;
        
        String trimmed = diagnosis.trim();
        return trimmed.length() >= MIN_DIAGNOSIS_LENGTH && 
               trimmed.length() <= MAX_DIAGNOSIS_LENGTH &&
               !trimmed.isEmpty();
    }
    
    public static boolean isValidPrescription(String prescription) {
        if (prescription == null) return false;
        
        String trimmed = prescription.trim();
        return trimmed.length() <= MAX_PRESCRIPTION_LENGTH;
    }
    
    public static boolean isValidNotes(String notes) {
        if (notes == null) return false;
        
        return notes.length() <= MAX_NOTES_LENGTH;
    }
    
    public static boolean isValidConsultationHours(double hours) {
        return hours >= MIN_CONSULTATION_HOURS && hours <= MAX_CONSULTATION_HOURS;
    }
    
    public static boolean isValidFollowUpConsultationId(String followUpId) {
        if (followUpId == null) return true; 
        return isValidConsultationId(followUpId);
    }
    
    public static boolean isWorkingHours(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        
        int hour = dateTime.getHour();
        int dayOfWeek = dateTime.getDayOfWeek().getValue();
        
        return dayOfWeek >= 1 && dayOfWeek <= 7 && hour >= 9 && hour < 17;
    }
    
    public static boolean canBeCompleted(String currentStatus) {
        return "SCHEDULED".equals(currentStatus) || "RESCHEDULED".equals(currentStatus);
    }
    
    public static boolean canBeCancelled(String currentStatus) {
        return "SCHEDULED".equals(currentStatus) || "RESCHEDULED".equals(currentStatus);
    }
    
    public static boolean canBeRescheduled(String currentStatus) {
        return "SCHEDULED".equals(currentStatus);
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
    public double getConsultationHr() { return consultationHr; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getFollowUpConsultationId() { return followUpConsultationId; }
    
    // Setters
    public void setConsultationId(String consultationId) { 
        if (isValidConsultationId(consultationId)) {
            this.consultationId = consultationId; 
        } else {
            throw new IllegalArgumentException("Invalid consultation ID format. Expected: C + 9 digits");
        }
    }
    
    public void setPatientId(String patientId) { 
        if (isValidPatientId(patientId)) {
            this.patientId = patientId; 
        } else {
            throw new IllegalArgumentException("Invalid patient ID format. Expected: P + 3 digits");
        }
    }
    
    public void setDoctorId(String doctorId) { 
        if (isValidDoctorId(doctorId)) {
            this.doctorId = doctorId; 
        } else {
            throw new IllegalArgumentException("Invalid doctor ID format. Expected: D + 3 digits");
        }
    }
    
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { 
        if (isValidAppointmentDateTime(appointmentDateTime)) {
            this.appointmentDateTime = appointmentDateTime; 
        } else {
            throw new IllegalArgumentException("Invalid appointment date/time. Must be within valid range and working hours.");
        }
    }
    
    public void setStatus(String status) { 
        if (isValidStatus(status)) {
            this.status = status; 
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("Invalid status. Must be one of: SCHEDULED, COMPLETED, CANCELLED, RESCHEDULED");
        }
    }
    
    public void setSymptoms(String symptoms) { 
        if (isValidSymptoms(symptoms)) {
            this.symptoms = symptoms.trim(); 
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("Invalid symptoms. Must be 3-500 characters long.");
        }
    }
    
    public void setDiagnosis(String diagnosis) { 
        if (isValidDiagnosis(diagnosis)) {
            this.diagnosis = diagnosis.trim(); 
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("Invalid diagnosis. Must be 3-200 characters long.");
        }
    }
    
    public void setPrescription(String prescription) { 
        if (isValidPrescription(prescription)) {
            this.prescription = prescription.trim(); 
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("Invalid prescription. Must be 0-300 characters long.");
        }
    }
    
    public void setNotes(String notes) { 
        if (isValidNotes(notes)) {
            this.notes = notes; 
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("Invalid notes. Must be 0-1000 characters long.");
        }
    }
    
    public void setConsultationHr(double consultationHr) { 
        if (isValidConsultationHours(consultationHr)) {
            this.consultationHr = consultationHr; 
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("Invalid consultation hours. Must be between 0 and 8 hours.");
        }
    }

    public void setFollowUpConsultationId(String followUpConsultationId) { 
        if (isValidFollowUpConsultationId(followUpConsultationId)) {
            this.followUpConsultationId = followUpConsultationId; 
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("Invalid follow-up consultation ID format.");
        }
    }
    
    @Override
    public String toString() {
        return String.format("Consultation ID: %s | Patient: %s | Doctor: %s | Date: %s | Status: %s",
                consultationId, patientId, doctorId, 
                appointmentDateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), 
                status);
    }
} 