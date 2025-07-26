package Controller;

import Entity.Consultation;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConsultationController {
    private List<Consultation> consultations = new ArrayList<>();
    private int consultationCounter = 1;

    // Dummy doctor data
    private String[] doctorIds = {"D001", "D002", "D003"};
    private String[] doctorNames = {"Dr. Lim", "Dr. Tan", "Dr. Lee"};

    // Create a new consultation appointment
    public Consultation createConsultation(String patientId, String doctorId, LocalDateTime dateTime) {
        String consultationId = generateConsultationId();
        Consultation c = new Consultation(consultationId, patientId, doctorId, dateTime, "SCHEDULED");
        consultations.add(c);
        return c;
    }

    // Generate simple consultation ID: C + date + sequence number
    private String generateConsultationId() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
        String sequence = String.format("%03d", consultationCounter++);
        return "C" + dateStr + sequence;
    }

    // Reschedule an existing consultation
    public boolean rescheduleConsultation(String consultationId, LocalDateTime newDateTime) {
        for (Consultation c : consultations) {
            if (c.getConsultationId().equals(consultationId)) {
                c.setAppointmentDateTime(newDateTime);
                c.setStatus("RESCHEDULED");
                return true;
            }
        }
        return false;
    }

    // Cancel a consultation
    public boolean cancelConsultation(String consultationId) {
        for (Consultation c : consultations) {
            if (c.getConsultationId().equals(consultationId)) {
                c.setStatus("CANCELLED");
                return true;
            }
        }
        return false;
    }

    // Create or update consultation record (for doctor)
    public boolean updateConsultationRecord(String consultationId, String symptoms, String diagnosis, String prescription, String notes) {
        for (Consultation c : consultations) {
            if (c.getConsultationId().equals(consultationId)) {
                c.setSymptoms(symptoms);
                c.setDiagnosis(diagnosis);
                c.setPrescription(prescription);
                c.setNotes(notes);
                c.setStatus("COMPLETED");
                return true;
            }
        }
        return false;
    }

    // Fetch consultation by ID
    public Consultation getConsultationById(String consultationId) {
        for (Consultation c : consultations) {
            if (c.getConsultationId().equals(consultationId)) {
                return c;
            }
        }
        return null;
    }

    // Fetch all consultations for a patient
    public List<Consultation> getConsultationsByPatient(String patientId) {
        List<Consultation> result = new ArrayList<>();
        for (Consultation c : consultations) {
            if (c.getPatientId().equals(patientId)) {
                result.add(c);
            }
        }
        return result;
    }

    // Fetch all consultations for a doctor
    public List<Consultation> getConsultationsByDoctor(String doctorId) {
        List<Consultation> result = new ArrayList<>();
        for (Consultation c : consultations) {
            if (c.getDoctorId().equals(doctorId)) {
                result.add(c);
            }
        }
        return result;
    }

    // Dummy doctor availability (for UI)
    public void printDoctorAvailability() {
        System.out.println("Doctor Availability (dummy data):");
        for (int i = 0; i < doctorIds.length; i++) {
            System.out.println(doctorIds[i] + " - " + doctorNames[i] + " | Available slots: 9:00, 10:00, 11:00");
        }
    }

    public String[] getDoctorIds() {
        return doctorIds;
    }
    public String[] getDoctorNames() {
        return doctorNames;
    }
} 