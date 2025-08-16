package Controller;

import Entity.Consultation;
import Controller.PatientController;

import ADT.ArrayList;
import ADT.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultationController {
    private ArrayList<Consultation> consultations;
    private HashMap<String, Consultation> consultationsById;
    private HashMap<String, ArrayList<Consultation>> consultationsByPatient;
    private HashMap<String, ArrayList<Consultation>> consultationsByDoctor;
    
    private int consultationCounter = 1;

    public ConsultationController() {
        this.consultations = new ArrayList<>();
        this.consultationsById = new HashMap<>();
        this.consultationsByPatient = new HashMap<>();
        this.consultationsByDoctor = new HashMap<>();
    }
    

    // Create a new consultation appointment
    public Consultation createConsultation(String patientId, String doctorId, LocalDateTime dateTime) {
        String consultationId = generateConsultationId();
        Consultation c = new Consultation(consultationId, patientId, doctorId, dateTime, "SCHEDULED");
        
        // Add to general consultations list
        consultations.add(c);
        
        // Add to HashMaps for O(1) lookup
        consultationsById.put(consultationId, c);
        addToPatientHashMap(c);
        addToDoctorHashMap(c);
        
        // Book the doctor's time slot
        Controller.DoctorController doctorController = new Controller.DoctorController();
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        doctorController.bookSlot(doctorId, date, time);
        
        return c;
    }
    
    // Add consultation to patient HashMap for O(1) lookup
    private void addToPatientHashMap(Consultation consultation) {
        String patientId = consultation.getPatientId();
        ArrayList<Consultation> patientConsultations = consultationsByPatient.get(patientId);
        
        if (patientConsultations == null) {
            patientConsultations = new ArrayList<>();
            consultationsByPatient.put(patientId, patientConsultations);
        }
        
        patientConsultations.add(consultation);
    }
    
    // Add consultation to doctor HashMap for O(1) lookup
    private void addToDoctorHashMap(Consultation consultation) {
        String doctorId = consultation.getDoctorId();
        ArrayList<Consultation> doctorConsultations = consultationsByDoctor.get(doctorId);
        
        if (doctorConsultations == null) {
            doctorConsultations = new ArrayList<>();
            consultationsByDoctor.put(doctorId, doctorConsultations);
        }
        
        doctorConsultations.add(consultation);
    }
    
    // Remove consultation from patient HashMap
    private void removeFromPatientHashMap(Consultation consultation) {
        String patientId = consultation.getPatientId();
        ArrayList<Consultation> patientConsultations = consultationsByPatient.get(patientId);
        
        if (patientConsultations != null) {
            patientConsultations.remove(consultation);
            if (patientConsultations.isEmpty()) {
                consultationsByPatient.remove(patientId);
            }
        }
    }
    
    // Remove consultation from doctor HashMap
    private void removeFromDoctorHashMap(Consultation consultation) {
        String doctorId = consultation.getDoctorId();
        ArrayList<Consultation> doctorConsultations = consultationsByDoctor.get(doctorId);
        
        if (doctorConsultations != null) {
            doctorConsultations.remove(consultation);
            if (doctorConsultations.isEmpty()) {
                consultationsByDoctor.remove(doctorId);
            }
        }
    }



    // Generate consultation ID: C + date + sequence number
    private String generateConsultationId() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
        String sequence = String.format("%03d", consultationCounter++);
        return "C" + dateStr + sequence;
    }

    // Reschedule an existing consultation
    public boolean rescheduleConsultation(String consultationId, LocalDateTime newDateTime) {
        Consultation consultation = getConsultationById(consultationId);
        if (consultation == null) return false;
        
        // Get the old date and time for releasing the slot
        LocalDateTime oldDateTime = consultation.getAppointmentDateTime();
        String doctorId = consultation.getDoctorId();
        
        // Release the old doctor slot
        Controller.DoctorController doctorController = new Controller.DoctorController();
        LocalDate oldDate = oldDateTime.toLocalDate();
        LocalTime oldTime = oldDateTime.toLocalTime();
        doctorController.addTimeSlot(doctorId, oldDate, oldTime);
        
        // Update to new time
        consultation.setAppointmentDateTime(newDateTime);
        consultation.setStatus("RESCHEDULED");
        
        // Book the new doctor slot
        LocalDate newDate = newDateTime.toLocalDate();
        LocalTime newTime = newDateTime.toLocalTime();
        doctorController.bookSlot(doctorId, newDate, newTime);
        
        return true;
    }

    // Cancel a consultation
    public boolean cancelConsultation(String consultationId) {
        Consultation consultation = getConsultationById(consultationId);
        if (consultation == null) return false;
        
        consultation.setStatus("CANCELLED");
        
        // Release the doctor's time slot
        Controller.DoctorController doctorController = new Controller.DoctorController();
        LocalDate date = consultation.getAppointmentDateTime().toLocalDate();
        LocalTime time = consultation.getAppointmentDateTime().toLocalTime();
        doctorController.addTimeSlot(consultation.getDoctorId(), date, time);
        
        // Note: We keep the consultation in HashMaps for history tracking
        
        return true;
    }

    // Create or update consultation record (for doctor)
    public boolean updateConsultationRecord(String consultationId, String symptoms, String diagnosis, String prescription, String notes) {
        Consultation consultation = getConsultationById(consultationId);
        if (consultation == null) return false;
        
        consultation.setSymptoms(symptoms);
        consultation.setDiagnosis(diagnosis);
        consultation.setPrescription(prescription);
        consultation.setNotes(notes);
        consultation.setStatus("COMPLETED");
        
        return true;
    }

    // Fetch consultation by ID - O(1) using HashMap
    public Consultation getConsultationById(String consultationId) {
        return consultationsById.get(consultationId);
    }

    // Fetch all consultations for a patient - O(1) using HashMap
    public ArrayList<Consultation> getConsultationsByPatient(String patientId) {
        ArrayList<Consultation> patientConsultations = consultationsByPatient.get(patientId);
        if (patientConsultations == null) {
            return new ArrayList<>(); // Return empty list if patient not found
        }
        return patientConsultations;
    }

    // Fetch all consultations for a doctor - O(1) using HashMap
    public ArrayList<Consultation> getConsultationsByDoctor(String doctorId) {
        ArrayList<Consultation> doctorConsultations = consultationsByDoctor.get(doctorId);
        if (doctorConsultations == null) {
            return new ArrayList<>(); // Return empty list if doctor not found
        }
        return doctorConsultations;
    }

    // Get consultations by date (simplified implementation)
    public ArrayList<Consultation> getConsultationsByDate(LocalDateTime date) {
        ArrayList<Consultation> result = new ArrayList<>();
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        // Simple linear search through all consultations
        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            LocalDateTime consultationDate = c.getAppointmentDateTime();
            if (consultationDate.isAfter(startOfDay) && consultationDate.isBefore(endOfDay)) {
                result.add(c);
            }
        }
        
        return result;
    }

    // Get consultations within a time range (simplified implementation)
    public ArrayList<Consultation> getConsultationsInTimeRange(LocalDateTime start, LocalDateTime end) {
        ArrayList<Consultation> result = new ArrayList<>();
        
        // Simple linear search through all consultations
        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            LocalDateTime consultationDate = c.getAppointmentDateTime();
            if (consultationDate.isAfter(start) && consultationDate.isBefore(end)) {
                result.add(c);
            }
        }
        
        return result;
    }

    // Get upcoming consultations (future appointments)
    public ArrayList<Consultation> getUpcomingConsultations() {
        ArrayList<Consultation> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        // Simple linear search through all consultations
        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            LocalDateTime consultationDate = c.getAppointmentDateTime();
            if (consultationDate.isAfter(now) && 
                (c.getStatus().equals("SCHEDULED") || c.getStatus().equals("RESCHEDULED"))) {
                result.add(c);
            }
        }
        
        return result;
    }

    // Get past consultations (completed/cancelled appointments)
    public ArrayList<Consultation> getPastConsultations() {
        ArrayList<Consultation> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        // Simple linear search through all consultations
        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            LocalDateTime consultationDate = c.getAppointmentDateTime();
            if (consultationDate.isBefore(now) && 
                (c.getStatus().equals("COMPLETED") || c.getStatus().equals("CANCELLED"))) {
                result.add(c);
            }
        }
        
        return result;
    }



    // NEW: Check if a time slot is available
    public boolean isSlotAvailable(LocalDateTime dateTime) {
        // Simplified check - in reality, you'd want to check against booked consultations
        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            if (c.getAppointmentDateTime().equals(dateTime) && 
                !c.getStatus().equals("CANCELLED")) {
                return false;
            }
        }
        return true;
    }





    // Print doctor availability for a date by integrating with DoctorController
    public void printDoctorAvailabilityForDate(java.time.LocalDate date) {
        Controller.DoctorController doctorController = new Controller.DoctorController();
        ADT.LinkedList<Entity.Doctor> doctors = doctorController.getAllDoctors();
        System.out.println("Doctor Availability on " + date + ":");
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
            return;
        }
        for (int i = 0; i < doctors.size(); i++) {
            Entity.Doctor d = doctors.get(i);
            ADT.LinkedList<java.time.LocalTime> slots = doctorController.getSlotsForDate(d.getDoctorId(), date);
            System.out.print(d.getDoctorId() + " - " + d.getName() + " (" + d.getSpecialization() + ") | ");
            if (slots.isEmpty()) {
                System.out.println("No available slots");
            } else {
                StringBuilder sb = new StringBuilder("Available: ");
                for (int j = 0; j < slots.size(); j++) {
                    sb.append(slots.get(j).toString());
                    if (j < slots.size() - 1) sb.append(", ");
                }
                System.out.println(sb.toString());
            }
        }
    }
    
    // Check doctor availability for a specific date and time
    public ArrayList<String> getAvailableDoctorsForDateTime(LocalDateTime dateTime) {
        ArrayList<String> availableDoctors = new ArrayList<>();
        Controller.DoctorController doctorController = new Controller.DoctorController();
        ADT.LinkedList<Entity.Doctor> doctors = doctorController.getAllDoctors();
        
        for (int i = 0; i < doctors.size(); i++) {
            Entity.Doctor doctor = doctors.get(i);
            if (doctor.isActive() && isDoctorAvailableAtTime(doctor.getDoctorId(), dateTime)) {
                availableDoctors.add(doctor.getDoctorId() + " - " + doctor.getName() + " (" + doctor.getSpecialization() + ")");
            }
        }
        
        return availableDoctors;
    }
    
    // Check if a specific doctor is available at a given time
    private boolean isDoctorAvailableAtTime(String doctorId, LocalDateTime dateTime) {
        // Check if the doctor has any consultations at this time
        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            if (c.getDoctorId().equals(doctorId) && 
                c.getAppointmentDateTime().equals(dateTime) &&
                !c.getStatus().equals("CANCELLED")) {
                return false; // Doctor is busy at this time
            }
        }
        
        // Check if the doctor has this time slot available in their schedule
        Controller.DoctorController doctorController = new Controller.DoctorController();
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        
        // Check if the doctor has this time slot in their schedule
        if (!doctorController.isSlotAvailable(doctorId, date, time)) {
            return false; // Doctor doesn't have this slot in their schedule
        }
        
        return true;
    }
    
    // Get available time slots for a specific date
    public ArrayList<LocalDateTime> getAvailableTimeSlotsForDate(LocalDateTime date) {
        ArrayList<LocalDateTime> availableSlots = new ArrayList<>();
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        
        // Get all doctors and their available slots for this date
        Controller.DoctorController doctorController = new Controller.DoctorController();
        ADT.LinkedList<Entity.Doctor> doctors = doctorController.getAllDoctors();
        java.time.LocalDate localDate = date.toLocalDate();
        
        // Collect all available time slots from all doctors
        for (int i = 0; i < doctors.size(); i++) {
            Entity.Doctor doctor = doctors.get(i);
            if (doctor.isActive()) {
                ADT.LinkedList<java.time.LocalTime> doctorSlots = doctorController.getSlotsForDate(doctor.getDoctorId(), localDate);
                for (int j = 0; j < doctorSlots.size(); j++) {
                    java.time.LocalTime timeSlot = doctorSlots.get(j);
                    LocalDateTime dateTimeSlot = localDate.atTime(timeSlot);
                    
                    // Check if this slot is not already booked
                    if (isSlotAvailable(dateTimeSlot)) {
                        // Add to available slots if not already present
                        boolean alreadyAdded = false;
                        for (int k = 0; k < availableSlots.size(); k++) {
                            if (availableSlots.get(k).equals(dateTimeSlot)) {
                                alreadyAdded = true;
                                break;
                            }
                        }
                        if (!alreadyAdded) {
                            availableSlots.add(dateTimeSlot);
                        }
                    }
                }
            }
        }
        
        return availableSlots;
    }
    
    // Validate patient ID exists (simplified - always returns true for now)
    public boolean validatePatientId(String patientId) {
        // For now, assume patient exists to avoid compilation issues
        // In a real system, this would validate against the patient database
        return patientId != null && !patientId.trim().isEmpty();
    }
    
    // NEW: Validate doctor ID exists
    public boolean validateDoctorId(String doctorId) {
        Controller.DoctorController doctorController = new Controller.DoctorController();
        return doctorController.getDoctorById(doctorId) != null;
    }

    // NEW: Get all consultations (for report generation)
    public ArrayList<Consultation> getAllConsultations() {
        return consultations;
    }

    // NEW: Get consultation count
    public int getConsultationCount() {
        return consultations.size();
    }

    // NEW: Get consultations by status
    public ArrayList<Consultation> getConsultationsByStatus(String status) {
        ArrayList<Consultation> result = new ArrayList<>();
        
        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            if (c.getStatus().equals(status)) {
                result.add(c);
            }
        }
        
        return result;
    }

    // NEW: Get consultations with follow-ups
    public ArrayList<Consultation> getConsultationsWithFollowUps() {
        ArrayList<Consultation> result = new ArrayList<>();
        
        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            if (c.getFollowUpConsultationId() != null) {
                result.add(c);
            }
        }
        
        return result;
    }

} 