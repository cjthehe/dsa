package Controller;

import Entity.Consultation;

import ADT.AVLTree;
import ADT.QueueADT;
import ADT.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsultationController {
    // Use AVL tree for efficient date/time-based searching
    private AVLTree<LocalDateTime, ArrayList<Consultation>> consultationsByDateTime;
    // Use ArrayList for general consultation storage
    private ArrayList<Consultation> consultations;
    // Use QueueADT for managing available consultation slots
    private QueueADT<LocalDateTime> availableSlots;
    
    private int consultationCounter = 1;

    // Dummy doctor data
    private String[] doctorIds = {"D001", "D002", "D003"};
    private String[] doctorNames = {"Dr. Lim", "Dr. Tan", "Dr. Lee"};

    public ConsultationController() {
        this.consultationsByDateTime = new AVLTree<LocalDateTime, ArrayList<Consultation>>();
        this.consultations = new ArrayList<>();
        // Initialize available slots queue with common time slots
        this.availableSlots = new QueueADT<>(100);
        initializeAvailableSlots();
        initializeSampleData(); // Add sample data for testing
    }
    
    // Initialize sample data for testing
    private void initializeSampleData() {
        // Initialize some sample doctors
        Controller.DoctorController doctorController = new Controller.DoctorController();
        doctorController.addDoctor("Dr. Lim", "General Practitioner", 5, 'M', "012-3456789", "dr.lim@clinic.com", null);
        doctorController.addDoctor("Dr. Tan", "Pediatrics", 8, 'F', "012-3456790", "dr.tan@clinic.com", null);
        doctorController.addDoctor("Dr. Lee", "Cardiology", 12, 'M', "012-3456791", "dr.lee@clinic.com", null);
        
        // Initialize some sample patients
        Controller.PatientController patientController = new Controller.PatientController();
        patientController.patientRegistration("John Doe", "900101012345", "012-3456789", "john.doe@email.com");
        patientController.patientRegistration("Jane Smith", "920202023456", "012-3456790", "jane.smith@email.com");
        patientController.patientRegistration("Mike Johnson", "880303034567", "012-3456791", "mike.johnson@email.com");
        
        // Set up some sample doctor schedules for today and tomorrow
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate tomorrow = today.plusDays(1);
        
        // Set working hours for all doctors (9 AM to 5 PM)
        for (String doctorId : doctorIds) {
            doctorController.defineAvailableSlots(doctorId, today, java.time.LocalTime.of(9, 0), java.time.LocalTime.of(17, 0), 60);
            doctorController.defineAvailableSlots(doctorId, tomorrow, java.time.LocalTime.of(9, 0), java.time.LocalTime.of(17, 0), 60);
        }
        
        System.out.println("Sample data initialized successfully!");
        System.out.println("Sample Doctors: D001 (Dr. Lim), D002 (Dr. Tan), D003 (Dr. Lee)");
        System.out.println("Sample Patients: P0001 (John Doe), P0002 (Jane Smith), P0003 (Mike Johnson)");
        System.out.println("Working hours: 9:00 AM to 5:00 PM");
    }

    // Initialize available consultation slots (9:00 AM to 5:00 PM, 1-hour intervals)
    private void initializeAvailableSlots() {
        LocalDateTime baseDate = LocalDateTime.now().toLocalDate().atStartOfDay();
        for (int hour = 9; hour < 17; hour++) {
            availableSlots.enqueue(baseDate.plusHours(hour));
        }
    }

    // Create a new consultation appointment
    public Consultation createConsultation(String patientId, String doctorId, LocalDateTime dateTime) {
        String consultationId = generateConsultationId();
        Consultation c = new Consultation(consultationId, patientId, doctorId, dateTime, "SCHEDULED");
        
        // Add to general consultations list
        consultations.add(c);
        
        // Add to AVL tree for date/time-based searching
        addToDateTimeTree(c);
        
        // Remove the time slot from available slots
        removeAvailableSlot(dateTime);
        
        return c;
    }

    // Add consultation to AVL tree organized by date/time
    private void addToDateTimeTree(Consultation consultation) {
        LocalDateTime dateTime = consultation.getAppointmentDateTime();
        ArrayList<Consultation> consultationsAtTime = consultationsByDateTime.search(dateTime);
        
        if (consultationsAtTime == null) {
            consultationsAtTime = new ArrayList<>();
            consultationsByDateTime.insert(dateTime, consultationsAtTime);
        }
        
        consultationsAtTime.add(consultation);
    }

    // Remove consultation from AVL tree
    private void removeFromDateTimeTree(Consultation consultation) {
        LocalDateTime dateTime = consultation.getAppointmentDateTime();
        ArrayList<Consultation> consultationsAtTime = consultationsByDateTime.search(dateTime);
        
        if (consultationsAtTime != null) {
            consultationsAtTime.remove(consultation);
            if (consultationsAtTime.isEmpty()) {
                consultationsByDateTime.delete(dateTime);
            }
        }
    }

    // Remove time slot from available slots
    private void removeAvailableSlot(LocalDateTime dateTime) {
        // This is a simplified implementation - in a real system, you'd want more sophisticated slot management
        // For now, we'll just track that the slot is taken
    }

    // Add time slot back to available slots (when consultation is cancelled)
    private void addAvailableSlot(LocalDateTime dateTime) {
        if (!availableSlots.isFull()) {
            availableSlots.enqueue(dateTime);
        }
    }

    // Generate simple consultation ID: C + date + sequence number
    private String generateConsultationId() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
        String sequence = String.format("%03d", consultationCounter++);
        return "C" + dateStr + sequence;
    }

    // Reschedule an existing consultation
    public boolean rescheduleConsultation(String consultationId, LocalDateTime newDateTime) {
        Consultation consultation = getConsultationById(consultationId);
        if (consultation == null) return false;
        
        // Remove from old time slot
        removeFromDateTimeTree(consultation);
        addAvailableSlot(consultation.getAppointmentDateTime());
        
        // Update to new time
        consultation.setAppointmentDateTime(newDateTime);
        consultation.setStatus("RESCHEDULED");
        
        // Add to new time slot
        addToDateTimeTree(consultation);
        removeAvailableSlot(newDateTime);
        
        return true;
    }

    // Cancel a consultation
    public boolean cancelConsultation(String consultationId) {
        Consultation consultation = getConsultationById(consultationId);
        if (consultation == null) return false;
        
        consultation.setStatus("CANCELLED");
        
        // Remove from date/time tree
        removeFromDateTimeTree(consultation);
        
        // Add the time slot back to available slots
        addAvailableSlot(consultation.getAppointmentDateTime());
        
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

    // Fetch consultation by ID
    public Consultation getConsultationById(String consultationId) {
        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            if (c.getConsultationId().equals(consultationId)) {
                return c;
            }
        }
        return null;
    }

    // Fetch all consultations for a patient
    public ArrayList<Consultation> getConsultationsByPatient(String patientId) {
        ArrayList<Consultation> result = new ArrayList<>();
        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            if (c.getPatientId().equals(patientId)) {
                result.add(c);
            }
        }
        return result;
    }

    // Fetch all consultations for a doctor
    public ArrayList<Consultation> getConsultationsByDoctor(String doctorId) {
        ArrayList<Consultation> result = new ArrayList<>();
        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            if (c.getDoctorId().equals(doctorId)) {
                result.add(c);
            }
        }
        return result;
    }

    // NEW: Efficient date/time-based searching using AVL tree
    public ArrayList<Consultation> getConsultationsByDate(LocalDateTime date) {
        ArrayList<Consultation> result = new ArrayList<>();
        
        // Use AVL tree range search for better efficiency
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        // Get all consultations in the date range using AVL tree
        ArrayList<ArrayList<Consultation>> consultationsInRange = 
            consultationsByDateTime.searchRange(startOfDay, endOfDay);
        
        // Flatten the results
        for (int i = 0; i < consultationsInRange.size(); i++) {
            ArrayList<Consultation> consultationsAtTime = consultationsInRange.get(i);
            for (int j = 0; j < consultationsAtTime.size(); j++) {
                result.add(consultationsAtTime.get(j));
            }
        }
        
        return result;
    }

    // NEW: Get consultations within a time range (efficient using AVL tree structure)
    public ArrayList<Consultation> getConsultationsInTimeRange(LocalDateTime start, LocalDateTime end) {
        ArrayList<Consultation> result = new ArrayList<>();
        
        // Use AVL tree range search for O(log n) complexity
        ArrayList<ArrayList<Consultation>> consultationsInRange = 
            consultationsByDateTime.searchRange(start, end);
        
        // Flatten the results
        for (int i = 0; i < consultationsInRange.size(); i++) {
            ArrayList<Consultation> consultationsAtTime = consultationsInRange.get(i);
            for (int j = 0; j < consultationsAtTime.size(); j++) {
                result.add(consultationsAtTime.get(j));
            }
        }
        
        return result;
    }

    // NEW: Get upcoming consultations (future appointments)
    public ArrayList<Consultation> getUpcomingConsultations() {
        ArrayList<Consultation> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        // Use AVL tree search for consultations greater than current time
        ArrayList<ArrayList<Consultation>> futureConsultations = 
            consultationsByDateTime.searchGreaterThan(now);
        
        // Flatten and filter by status
        for (int i = 0; i < futureConsultations.size(); i++) {
            ArrayList<Consultation> consultationsAtTime = futureConsultations.get(i);
            for (int j = 0; j < consultationsAtTime.size(); j++) {
                Consultation c = consultationsAtTime.get(j);
                if (c.getStatus().equals("SCHEDULED") || c.getStatus().equals("RESCHEDULED")) {
                    result.add(c);
                }
            }
        }
        
        return result;
    }

    // NEW: Get past consultations (completed/cancelled appointments)
    public ArrayList<Consultation> getPastConsultations() {
        ArrayList<Consultation> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        // Use AVL tree search for consultations less than current time
        ArrayList<ArrayList<Consultation>> pastConsultations = 
            consultationsByDateTime.searchLessThan(now);
        
        // Flatten and filter by status
        for (int i = 0; i < pastConsultations.size(); i++) {
            ArrayList<Consultation> consultationsAtTime = pastConsultations.get(i);
            for (int j = 0; j < consultationsAtTime.size(); j++) {
                Consultation c = consultationsAtTime.get(j);
                if (c.getStatus().equals("COMPLETED") || c.getStatus().equals("CANCELLED")) {
                    result.add(c);
                }
            }
        }
        
        return result;
    }

    // NEW: Get next available consultation slot using QueueADT
    public LocalDateTime getNextAvailableSlot() {
        if (availableSlots.isEmpty()) {
            return null;
        }
        return availableSlots.peek(); // Don't remove, just peek
    }

    // NEW: Book a specific time slot
    public boolean bookTimeSlot(LocalDateTime dateTime) {
        // Check if slot is available
        if (isSlotAvailable(dateTime)) {
            removeAvailableSlot(dateTime);
            return true;
        }
        return false;
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

    // NEW: Get all available slots for a specific date
    public ArrayList<LocalDateTime> getAvailableSlotsForDate(LocalDateTime date) {
        ArrayList<LocalDateTime> availableSlotsForDate = new ArrayList<>();
        
        // Generate time slots for the specific date (9 AM to 5 PM)
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        for (int hour = 9; hour < 17; hour++) {
            LocalDateTime slot = startOfDay.plusHours(hour);
            if (isSlotAvailable(slot)) {
                availableSlotsForDate.add(slot);
            }
        }
        
        return availableSlotsForDate;
    }

    // Dummy doctor availability (for UI)
    public void printDoctorAvailability() {
        System.out.println("Doctor Availability (dummy data):");
        for (int i = 0; i < doctorIds.length; i++) {
            System.out.println(doctorIds[i] + " - " + doctorNames[i] + " | Available slots: 9:00, 10:00, 11:00");
        }
    }

    // NEW: Print doctor availability for a date by integrating with DoctorController
    public void printDoctorAvailabilityForDate(java.time.LocalDate date) {
        Controller.DoctorController doctorController = new Controller.DoctorController();
        java.util.List<Entity.Doctor> doctors = doctorController.getAllDoctors();
        System.out.println("Doctor Availability on " + date + ":");
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
            return;
        }
        for (Entity.Doctor d : doctors) {
            java.util.List<java.time.LocalTime> slots = doctorController.getSlotsForDate(d.getDoctorId(), date);
            System.out.print(d.getDoctorId() + " - " + d.getName() + " | ");
            if (slots.isEmpty()) {
                System.out.println("No available slots");
            } else {
                StringBuilder sb = new StringBuilder("Available: ");
                for (int i = 0; i < slots.size(); i++) {
                    sb.append(slots.get(i).toString());
                    if (i < slots.size() - 1) sb.append(", ");
                }
                System.out.println(sb.toString());
            }
        }
    }
    
    // NEW: Check doctor availability for a specific date and time
    public ArrayList<String> getAvailableDoctorsForDateTime(LocalDateTime dateTime) {
        ArrayList<String> availableDoctors = new ArrayList<>();
        Controller.DoctorController doctorController = new Controller.DoctorController();
        java.util.List<Entity.Doctor> doctors = doctorController.getAllDoctors();
        
        for (Entity.Doctor doctor : doctors) {
            if (doctor.isActive() && isDoctorAvailableAtTime(doctor.getDoctorId(), dateTime)) {
                availableDoctors.add(doctor.getDoctorId() + " - " + doctor.getName() + " (" + doctor.getSpecialization() + ")");
            }
        }
        
        return availableDoctors;
    }
    
    // NEW: Check if a specific doctor is available at a given time
    private boolean isDoctorAvailableAtTime(String doctorId, LocalDateTime dateTime) {
        // Check if the doctor has any consultations at this time
        ArrayList<Consultation> consultationsAtTime = consultationsByDateTime.search(dateTime);
        if (consultationsAtTime != null) {
            for (int i = 0; i < consultationsAtTime.size(); i++) {
                Consultation c = consultationsAtTime.get(i);
                if (c.getDoctorId().equals(doctorId) && 
                    !c.getStatus().equals("CANCELLED")) {
                    return false; // Doctor is busy at this time
                }
            }
        }
        
        // Check if the time is within working hours (9 AM to 5 PM)
        int hour = dateTime.getHour();
        if (hour < 9 || hour >= 17) {
            return false; // Outside working hours
        }
        
        return true;
    }
    
    // NEW: Get available time slots for a specific date
    public ArrayList<LocalDateTime> getAvailableTimeSlotsForDate(LocalDateTime date) {
        ArrayList<LocalDateTime> availableSlots = new ArrayList<>();
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        
        // Generate time slots from 9 AM to 5 PM
        for (int hour = 9; hour < 17; hour++) {
            LocalDateTime slot = startOfDay.plusHours(hour);
            if (isSlotAvailable(slot)) {
                availableSlots.add(slot);
            }
        }
        
        return availableSlots;
    }
    
    // NEW: Validate patient ID exists
    public boolean validatePatientId(String patientId) {
        Controller.PatientController patientController = new Controller.PatientController();
        return patientController.findPatientByID(patientId) != null;
    }
    
    // NEW: Validate doctor ID exists
    public boolean validateDoctorId(String doctorId) {
        Controller.DoctorController doctorController = new Controller.DoctorController();
        return doctorController.getDoctorById(doctorId) != null;
    }

    public String[] getDoctorIds() {
        return doctorIds;
    }
    
    public String[] getDoctorNames() {
        return doctorNames;
    }

    // NEW: Get consultation statistics
    public void printConsultationStats() {
        System.out.println("=== Consultation Statistics ===");
        System.out.println("Total consultations: " + consultations.size());
        System.out.println("Available slots: " + availableSlots.size());
        
        // Count consultations by status
        int scheduled = 0, completed = 0, cancelled = 0, rescheduled = 0;
        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);
            switch (c.getStatus()) {
                case "SCHEDULED": scheduled++; break;
                case "COMPLETED": completed++; break;
                case "CANCELLED": cancelled++; break;
                case "RESCHEDULED": rescheduled++; break;
            }
        }
        
        System.out.println("Scheduled: " + scheduled);
        System.out.println("Completed: " + completed);
        System.out.println("Cancelled: " + cancelled);
        System.out.println("Rescheduled: " + rescheduled);
    }
} 