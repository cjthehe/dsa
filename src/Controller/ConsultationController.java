/**
 * ConsultationController class
 * Author: Chan Jean Theng
 */

 package Controller;

 import Entity.Consultation;
 import ADT.ArrayList;
 import ADT.HashMap;
 import ADT.LinkedList;
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
     
     // Validation methods
     public ValidationResult validateCreateConsultation(String patientId, String doctorId, LocalDateTime dateTime) {
         ValidationResult result = new ValidationResult();
         
         // Validate patient ID
         if (!Consultation.isValidPatientId(patientId)) {
             result.addError("Invalid patient ID format. Expected: P + 4 digits");
         } else if (!validatePatientId(patientId)) {
             result.addError("Patient ID not found in system");
         }
         
         // Validate doctor ID
         if (doctorId != null && !doctorId.trim().isEmpty()) {
             if (!Consultation.isValidDoctorId(doctorId)) {
                 result.addError("Invalid doctor ID format. Expected: D + 3 digits");
             } else if (!validateDoctorId(doctorId)) {
                 result.addError("Doctor ID not found in system");
             }
         }
         
         // Validate appointment date/time
         if (!Consultation.isValidAppointmentDateTime(dateTime)) {
             result.addError("Invalid appointment date/time. Must be within valid range");
         } else if (!Consultation.isWorkingHours(dateTime)) {
             result.addError("Appointment must be during working hours (8AM - 7PM)");
         } else if (!isSlotAvailable(dateTime)) {
             result.addError("Time slot is not available");
         } else if (doctorId != null && !doctorId.trim().isEmpty() && isDoctorBusyAtTime(doctorId, dateTime)) {
             result.addError("Doctor is busy at this time");
         }
         
         // Check for duplicate appointments
         if (hasPatientAppointmentAtTime(patientId, dateTime)) {
             result.addError("Patient already has an appointment at this time");
         }
         
         return result;
     }
     
     public ValidationResult validateRescheduleConsultation(String consultationId, LocalDateTime newDateTime) {
         ValidationResult result = new ValidationResult();
         
         // Validate consultation exists
         Consultation consultation = getConsultationById(consultationId);
         if (consultation == null) {
             result.addError("Consultation not found");
             return result;
         }
         
         // Validate current status, allows rescheduling
         if (!Consultation.canBeRescheduled(consultation.getStatus())) {
             result.addError("Consultation cannot be rescheduled. Current status: " + consultation.getStatus());
         }
         
         // Validate new date/time
         if (!Consultation.isValidAppointmentDateTime(newDateTime)) {
             result.addError("Invalid new appointment date/time. Must be within valid range");
         } else if (!Consultation.isWorkingHours(newDateTime)) {
             result.addError("New appointment must be during working hours (Monday-Friday, 8 AM - 7 PM)");
         } else if (!isSlotAvailable(newDateTime)) {
             result.addError("New time slot is not available");
         } else if (isDoctorBusyAtTime(consultation.getDoctorId(), newDateTime)) {
             result.addError("Doctor is busy at the new time");
         }
         
         // Check for duplicate appointments
         if (hasPatientAppointmentAtTime(consultation.getPatientId(), newDateTime, consultationId)) {
             result.addError("Patient already has another appointment at this time");
         }
         
         return result;
     }
     
     public ValidationResult validateCancelConsultation(String consultationId) {
         ValidationResult result = new ValidationResult();
         
         // Validate consultation exists
         Consultation consultation = getConsultationById(consultationId);
         if (consultation == null) {
             result.addError("Consultation not found");
             return result;
         }
         
         // Validate current status, allows cancellation
         if (!Consultation.canBeCancelled(consultation.getStatus())) {
             result.addError("Consultation cannot be cancelled. Current status: " + consultation.getStatus());
         }
         
         // Check if appointment is too close to cancel (within 24 hours)
         LocalDateTime now = LocalDateTime.now();
         LocalDateTime appointmentTime = consultation.getAppointmentDateTime();
         if (appointmentTime.isAfter(now) && appointmentTime.isBefore(now.plusHours(24))) {
             result.addWarning("Appointment is within 24 hours. Late cancellation may incur charges.");
         }
         
         return result;
     }
     
     public ValidationResult validateUpdateConsultationRecord(String consultationId, String symptoms, 
                                                            String diagnosis, String prescription, 
                                                            String notes, double consultationHr) {
         ValidationResult result = new ValidationResult();
         
         // Validate consultation exists
         Consultation consultation = getConsultationById(consultationId);
         if (consultation == null) {
             result.addError("Consultation not found");
             return result;
         }
         
         // Validate current status, allows completion
         if (!Consultation.canBeCompleted(consultation.getStatus())) {
             result.addError("Consultation cannot be completed. Current status: " + consultation.getStatus());
         }
         
         // Validate symptoms
         if (!Consultation.isValidSymptoms(symptoms)) {
             result.addError("Invalid symptoms. Must be 3-500 characters long.");
         }
         
         // Validate diagnosis
         if (!Consultation.isValidDiagnosis(diagnosis)) {
             result.addError("Invalid diagnosis. Must be 3-200 characters long.");
         }
         
         // Validate prescription
         if (!Consultation.isValidPrescription(prescription)) {
             result.addError("Invalid prescription. Must be 0-300 characters long.");
         }
         
         // Validate notes
         if (!Consultation.isValidNotes(notes)) {
             result.addError("Invalid notes. Must be 0-1000 characters long.");
         }
         
         // Validate consultation hours
         if (!Consultation.isValidConsultationHours(consultationHr)) {
             result.addError("Invalid consultation hours. Must be between 0 and 8 hours.");
         }
         
         return result;
     }
     
     private boolean isDoctorBusyAtTime(String doctorId, LocalDateTime dateTime) {
         if (doctorId == null || doctorId.trim().isEmpty()) {
             return false;
         }
         
         for (int i = 0; i < consultations.size(); i++) {
             Consultation c = consultations.get(i);
             if (c.getDoctorId().equals(doctorId) && 
                 c.getAppointmentDateTime().equals(dateTime) &&
                 !c.getStatus().equals("CANCELLED")) {
                 return true;
             }
         }
         return false;
     }
     
     private boolean hasPatientAppointmentAtTime(String patientId, LocalDateTime dateTime) {
         return hasPatientAppointmentAtTime(patientId, dateTime, null);
     }
     
     private boolean hasPatientAppointmentAtTime(String patientId, LocalDateTime dateTime, String excludeConsultationId) {
         for (int i = 0; i < consultations.size(); i++) {
             Consultation c = consultations.get(i);
             if (c.getPatientId().equals(patientId) && 
                 c.getAppointmentDateTime().equals(dateTime) &&
                 !c.getStatus().equals("CANCELLED") &&
                 (excludeConsultationId == null || !c.getConsultationId().equals(excludeConsultationId))) {
                 return true;
             }
         }
         return false;
     }
     
     public Consultation createConsultation(String patientId, String doctorId, LocalDateTime dateTime) {
         ValidationResult validation = validateCreateConsultation(patientId, doctorId, dateTime);
         if (!validation.isValid()) {
             throw new IllegalArgumentException("Validation failed: " + validation.getErrorsAsString());
         }
         
         String consultationId = generateConsultationId();
         Consultation c = new Consultation(consultationId, patientId, doctorId, dateTime, "SCHEDULED");
         
         consultations.add(c);
         
         consultationsById.put(consultationId, c);
         addToPatientHashMap(c);
         addToDoctorHashMap(c);
         
         Controller.DoctorController doctorController = new Controller.DoctorController();
         LocalDate date = dateTime.toLocalDate();
         LocalTime time = dateTime.toLocalTime();
         doctorController.bookSlot(doctorId, date, time);
         
         return c;
     }
     
     private void addToPatientHashMap(Consultation consultation) {
         String patientId = consultation.getPatientId();
         ArrayList<Consultation> patientConsultations = consultationsByPatient.get(patientId);
         
         if (patientConsultations == null) {
             patientConsultations = new ArrayList<>();
             consultationsByPatient.put(patientId, patientConsultations);
         }
         
         patientConsultations.add(consultation);
     }
     
     private void addToDoctorHashMap(Consultation consultation) {
         String doctorId = consultation.getDoctorId();
         ArrayList<Consultation> doctorConsultations = consultationsByDoctor.get(doctorId);
         
         if (doctorConsultations == null) {
             doctorConsultations = new ArrayList<>();
             consultationsByDoctor.put(doctorId, doctorConsultations);
         }
         
         doctorConsultations.add(consultation);
     }
 
     // Generate consultation ID: C + date + sequence number
     private String generateConsultationId() {
         String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
         String sequence = String.format("%03d", consultationCounter++);
         return "C" + dateStr + sequence;
     }
 
     // Reschedule an existing consultation with validation
     public boolean rescheduleConsultation(String consultationId, LocalDateTime newDateTime) {
         ValidationResult validation = validateRescheduleConsultation(consultationId, newDateTime);
         if (!validation.isValid()) {
             throw new IllegalArgumentException("Validation failed: " + validation.getErrorsAsString());
         }
         
         Consultation consultation = getConsultationById(consultationId);
         if (consultation == null) return false;
         
         LocalDateTime oldDateTime = consultation.getAppointmentDateTime();
         String doctorId = consultation.getDoctorId();
         
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
 
     // Cancel a consultation with validation
     public boolean cancelConsultation(String consultationId) {
         ValidationResult validation = validateCancelConsultation(consultationId);
         if (!validation.isValid()) {
             throw new IllegalArgumentException("Validation failed: " + validation.getErrorsAsString());
         }
         
         Consultation consultation = getConsultationById(consultationId);
         if (consultation == null) return false;
         
         consultation.setStatus("CANCELLED");
         
         Controller.DoctorController doctorController = new Controller.DoctorController();
         LocalDate date = consultation.getAppointmentDateTime().toLocalDate();
         LocalTime time = consultation.getAppointmentDateTime().toLocalTime();
         doctorController.addTimeSlot(consultation.getDoctorId(), date, time);
         
         return true;
     }
 
     // Create or update consultation record with validation
     public boolean updateConsultationRecord(String consultationId, String symptoms, String diagnosis, String prescription, String notes, double consultationHr) {
         ValidationResult validation = validateUpdateConsultationRecord(consultationId, symptoms, diagnosis, prescription, notes, consultationHr);
         if (!validation.isValid()) {
             throw new IllegalArgumentException("Validation failed: " + validation.getErrorsAsString());
         }
         
         Consultation consultation = getConsultationById(consultationId);
         if (consultation == null) return false;
         
         consultation.setSymptoms(symptoms);
         consultation.setDiagnosis(diagnosis);
         consultation.setPrescription(prescription);
         consultation.setNotes(notes);
         consultation.setConsultationHr(consultationHr);
         consultation.setStatus("COMPLETED");
         
         return true;
     }
 
     public Consultation addConsultation(
         String patientId,
         String doctorId,
         LocalDateTime dateTime,
         String status,
         String symptoms,
         String diagnosis,
         String prescription,
         String notes,
         double consultationHr
     ) {
         String consultationId = generateConsultationId();
         Consultation c = new Consultation(
             consultationId,
             patientId,
             doctorId,
             dateTime,
             status,
             symptoms,
             diagnosis,
             prescription,
             notes,
             consultationHr
         );
 
         consultations.add(c);
         consultationsById.put(consultationId, c);
         addToPatientHashMap(c);
         addToDoctorHashMap(c);
 
         Controller.DoctorController doctorController = new Controller.DoctorController();
         LocalDate date = dateTime.toLocalDate();
         LocalTime time = dateTime.toLocalTime();
         doctorController.bookSlot(doctorId, date, time);
 
         return c;
     }
 
     public Consultation getConsultationById(String consultationId) {
         return consultationsById.get(consultationId);
     }
 
     public ArrayList<Consultation> getConsultationsByPatient(String patientId) {
         ArrayList<Consultation> patientConsultations = consultationsByPatient.get(patientId);
         if (patientConsultations == null) {
             return new ArrayList<>(); 
         }
         return patientConsultations;
     }
 
     public ArrayList<Consultation> getConsultationsByDoctor(String doctorId) {
         ArrayList<Consultation> doctorConsultations = consultationsByDoctor.get(doctorId);
         if (doctorConsultations == null) {
             return new ArrayList<>(); 
         }
         return doctorConsultations;
     }
 
     public ArrayList<Consultation> getConsultationsByDate(LocalDateTime date) {
         ArrayList<Consultation> result = new ArrayList<>();
         LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
         LocalDateTime endOfDay = startOfDay.plusDays(1);
         
         for (int i = 0; i < consultations.size(); i++) {
             Consultation c = consultations.get(i);
             LocalDateTime consultationDate = c.getAppointmentDateTime();
             if (consultationDate.isAfter(startOfDay) && consultationDate.isBefore(endOfDay)) {
                 result.add(c);
             }
         }
         
         return result;
     }
 
     public ArrayList<Consultation> getConsultationsInTimeRange(LocalDateTime start, LocalDateTime end) {
         ArrayList<Consultation> result = new ArrayList<>();
         
         for (int i = 0; i < consultations.size(); i++) {
             Consultation c = consultations.get(i);
             LocalDateTime consultationDate = c.getAppointmentDateTime();
             if (consultationDate.isAfter(start) && consultationDate.isBefore(end)) {
                 result.add(c);
             }
         }
         
         return result;
     }
 
     public ArrayList<Consultation> getUpcomingConsultations() {
         ArrayList<Consultation> result = new ArrayList<>();
         LocalDateTime now = LocalDateTime.now();
         
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
 
     public ArrayList<Consultation> getPastConsultations() {
         ArrayList<Consultation> result = new ArrayList<>();
         LocalDateTime now = LocalDateTime.now();
         
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
 
     public boolean isSlotAvailable(LocalDateTime dateTime) {
         for (int i = 0; i < consultations.size(); i++) {
             Consultation c = consultations.get(i);
             if (c.getAppointmentDateTime().equals(dateTime) && 
                 !c.getStatus().equals("CANCELLED")) {
                 return false;
             }
         }
         return true;
     }
 
     public void printDoctorAvailabilityForDate(java.time.LocalDate date) {
         Controller.DoctorController doctorController = new Controller.DoctorController();
         LinkedList<Entity.Doctor> doctors = doctorController.getAllDoctors();
         System.out.println("Doctor Availability on " + date + ":");
         if (doctors.isEmpty()) {
             System.out.println("No doctors found.");
             return;
         }
         for (int i = 0; i < doctors.size(); i++) {
             Entity.Doctor d = doctors.get(i);
             LinkedList<java.time.LocalTime> slots = doctorController.getSlotsForDate(d.getDoctorId(), date);
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
         LinkedList<Entity.Doctor> doctors = doctorController.getAllDoctors();
         
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
                 System.out.println("    Doctor has existing consultation at this time");
                 return false;
             }
         }
         
         // Check if the doctor has this time slot available in their schedule
         Controller.DoctorController doctorController = new Controller.DoctorController();
         LocalDate date = dateTime.toLocalDate();
         LocalTime time = dateTime.toLocalTime();
         
         // Get all available time slots for this doctor on this date
         LinkedList<java.time.LocalTime> availableSlots = doctorController.getSlotsForDate(doctorId, date);
         
         // Check if the requested time falls within any available slot
         for (int i = 0; i < availableSlots.size(); i++) {
             java.time.LocalTime slotTime = availableSlots.get(i);
             // Each slot represents a 60-minute period starting at slotTime
             // For example, if slotTime is 10:00, it covers 10:00-11:00
             if (time.compareTo(slotTime) >= 0 && time.compareTo(slotTime.plusMinutes(60)) < 0) {
                 return true;
             }
         }
         
         return false;
     }
     
     // Get available time slots for a specific date
     public ArrayList<LocalDateTime> getAvailableTimeSlotsForDate(LocalDateTime date) {
         ArrayList<LocalDateTime> availableSlots = new ArrayList<>();
         LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
         
         // Get all doctors and their available slots for this date
         Controller.DoctorController doctorController = new Controller.DoctorController();
         LinkedList<Entity.Doctor> doctors = doctorController.getAllDoctors();
         java.time.LocalDate localDate = date.toLocalDate();
         
         // Collect all available time slots from all doctors
         for (int i = 0; i < doctors.size(); i++) {
             Entity.Doctor doctor = doctors.get(i);
             if (doctor.isActive()) {
                 LinkedList<java.time.LocalTime> doctorSlots = doctorController.getSlotsForDate(doctor.getDoctorId(), localDate);
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
     
     // Validate patient ID exists
     public boolean validatePatientId(String patientId) {
         return patientId != null && !patientId.trim().isEmpty();
     }
     
     public boolean validateDoctorId(String doctorId) {
         Controller.DoctorController doctorController = new Controller.DoctorController();
         return doctorController.getDoctorById(doctorId) != null;
     }
 
     public ArrayList<Consultation> getAllConsultations() {
         return consultations;
     }
 
     public int getConsultationCount() {
         return consultations.size();
     }
 
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
 
     public static class ValidationResult {
         private ArrayList<String> errors;
         private ArrayList<String> warnings;
         
         public ValidationResult() {
             this.errors = new ArrayList<>();
             this.warnings = new ArrayList<>();
         }
         
         public void addError(String error) {
             errors.add(error);
         }
         
         public void addWarning(String warning) {
             warnings.add(warning);
         }
         
         public boolean isValid() {
             return errors.isEmpty();
         }
         
         public boolean hasWarnings() {
             return !warnings.isEmpty();
         }
         
         public ArrayList<String> getErrors() {
             return errors;
         }
         
         public ArrayList<String> getWarnings() {
             return warnings;
         }
         
         public String getErrorsAsString() {
             StringBuilder sb = new StringBuilder();
             for (int i = 0; i < errors.size(); i++) {
                 sb.append(errors.get(i));
                 if (i < errors.size() - 1) {
                     sb.append("; ");
                 }
             }
             return sb.toString();
         }
         
         public String getWarningsAsString() {
             StringBuilder sb = new StringBuilder();
             for (int i = 0; i < warnings.size(); i++) {
                 sb.append(warnings.get(i));
                 if (i < warnings.size() - 1) {
                     sb.append("; ");
                 }
             }
             return sb.toString();
         }
         
         public void printErrors() {
             if (!errors.isEmpty()) {
                 System.out.println("Validation Errors:");
                 for (int i = 0; i < errors.size(); i++) {
                     System.out.println("  • " + errors.get(i));
                 }
             }
         }
         
         public void printWarnings() {
             if (!warnings.isEmpty()) {
                 System.out.println("Validation Warnings:");
                 for (int i = 0; i < warnings.size(); i++) {
                     System.out.println("  • " + warnings.get(i));
                 }
             }
         }
     }
 } 