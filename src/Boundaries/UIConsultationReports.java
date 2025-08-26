package Boundaries;

import Controller.ConsultationController;
import Controller.ConsultationReportController;
import Entity.Consultation;

import ADT.HashMap;
import ADT.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UIConsultation {
    private Scanner scanner = new Scanner(System.in);
    private ConsultationController controller = new ConsultationController();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void showMenu() {
        while (true) {
            System.out.println("\n===== Consultation Management System =====");
            System.out.println("1. View Doctor Availability (Patient/Doctor)");
            System.out.println("2. Create Consultation Appointment (Patient)");
            System.out.println("3. Reschedule Consultation (Patient)");
            System.out.println("4. Cancel Consultation (Patient)");
            System.out.println("5. Manage Consultation Record (Doctor)");
            System.out.println("6. View Patient Consultation History (Doctor)");
            System.out.println("7. View Report (Doctor)");
            System.out.println("8. Exit");
            System.out.print("Select your option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            
            switch (choice) {
                case 1:
                    viewDoctorAvailability();
                    break;
                case 2:
                    createAppointment();
                    break;
                case 3:
                    rescheduleAppointment();
                    break;
                case 4:
                    cancelAppointment();
                    break;
                case 5:
                    manageConsultationRecord();
                    break;
                case 6:
                    viewConsultationHistory();
                    break;
                case 7:
                    viewReports();
                    break;
                case 8:
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewDoctorAvailability() {
        System.out.println("=== View Doctor Availability ===");
        System.out.println("Purpose: Check doctor schedules before creating or rescheduling consultations");
        System.out.println();
        
        try {
            System.out.print("Enter desired date (yyyy-MM-dd): ");
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);
            
            System.out.println("\nRetrieving doctor list and available time slots...");
            System.out.println();
            
            // Show available time slots for the date
            LocalDateTime startOfDay = date.atStartOfDay();
            ArrayList<LocalDateTime> availableSlots = controller.getAvailableTimeSlotsForDate(startOfDay);
            
            if (availableSlots.isEmpty()) {
                System.out.println("No available slots found for " + date + ".");
                return;
            }
            
            System.out.println("Available time slots on " + date + ":");
            for (int i = 0; i < availableSlots.size(); i++) {
                LocalDateTime slot = availableSlots.get(i);
                System.out.println((i + 1) + ". " + slot.format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            System.out.println();
            
            // Show doctor availability for each time slot
            System.out.println("Doctor availability by time slot:");
            System.out.println("=================================");
            
            for (int i = 0; i < availableSlots.size(); i++) {
                LocalDateTime slot = availableSlots.get(i);
                System.out.println("\nTime: " + slot.format(DateTimeFormatter.ofPattern("HH:mm")));
                ArrayList<String> availableDoctors = controller.getAvailableDoctorsForDateTime(slot);
                
                if (availableDoctors.isEmpty()) {
                    System.out.println("  No doctors available");
                } else {
                    System.out.println("  Available doctors:");
                    for (int j = 0; j < availableDoctors.size(); j++) {
                        String doctor = availableDoctors.get(j);
                        System.out.println("    • " + doctor);
                    }
                }
            }
            
            System.out.println("\nNote: You can now proceed to create a consultation appointment (Option 2)");
            
        } catch (Exception e) {
            System.out.println("Error: Invalid date format. Please use yyyy-MM-dd format.");
        }
    }

    private void createAppointment() {
        System.out.println("=== Create Consultation Appointment ===");
        System.out.println("Pre-condition: Patient should check doctor availability first (Option 1)");
        System.out.println();
        
        try {
            // Step 1: Enter Patient ID
            System.out.print("Enter Patient ID: ");
            String patientId = scanner.nextLine();
            
            if (!controller.validatePatientId(patientId)) {
                System.out.println("Error: Patient ID not found. Please register the patient first.");
                return;
            }
            
            // Step 2: Enter desired date and time
            System.out.print("Enter desired date and time (yyyy-MM-dd HH:mm): ");
            String dateTimeStr = scanner.nextLine();
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, dtf);
            
            // Validate time is in working hours
            int hour = dateTime.getHour();
            if (hour < 9 || hour >= 17) {
                System.out.println("Error: Consultation hours are 9:00 AM to 5:00 PM only.");
                return;
            }
            
            // Step 3: System shows available doctors for that slot
            System.out.println("\nChecking available doctors for " + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "...");
            ArrayList<String> availableDoctors = controller.getAvailableDoctorsForDateTime(dateTime);
            
            if (availableDoctors.isEmpty()) {
                System.out.println("No doctors available at this time. Please choose another time slot.");
                return;
            }
            
            System.out.println("Available doctors for this time slot:");
            for (int i = 0; i < availableDoctors.size(); i++) {
                System.out.println((i + 1) + ". " + availableDoctors.get(i));
            }
            
            // Step 4: Patient selects a doctor
            System.out.print("\nSelect doctor (enter number): ");
            int doctorChoice = scanner.nextInt();
            scanner.nextLine();
            
            if (doctorChoice < 1 || doctorChoice > availableDoctors.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            String selectedDoctor = availableDoctors.get(doctorChoice - 1);
            String doctorId = selectedDoctor.split(" - ")[0];
            
            // Step 5: System generates Consultation ID and saves appointment
            Consultation consultation = controller.createConsultation(patientId, doctorId, dateTime);
            
            System.out.println("\n✓ Appointment created successfully!");
            System.out.println("Consultation ID: " + consultation.getConsultationId());
            System.out.println("Patient ID: " + consultation.getPatientId());
            System.out.println("Doctor ID: " + consultation.getDoctorId());
            System.out.println("Date & Time: " + consultation.getAppointmentDateTime().format(dtf));
            System.out.println("Status: " + consultation.getStatus());
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please ensure all inputs are in the correct format.");
        }
    }

    private void rescheduleAppointment() {
        System.out.println("=== Reschedule Consultation ===");
        System.out.println();
        
        try {
            // Step 1: Enter Consultation ID
            System.out.print("Enter Consultation ID: ");
            String consultationId = scanner.nextLine();
            
            Consultation consultation = controller.getConsultationById(consultationId);
            if (consultation == null) {
                System.out.println("Error: Consultation not found.");
                return;
            }
            
            // Step 2: System retrieves current details
            System.out.println("\nCurrent consultation details:");
            System.out.println("Consultation ID: " + consultation.getConsultationId());
            System.out.println("Patient ID: " + consultation.getPatientId());
            System.out.println("Doctor ID: " + consultation.getDoctorId());
            System.out.println("Current Date & Time: " + consultation.getAppointmentDateTime().format(dtf));
            System.out.println("Status: " + consultation.getStatus());
            System.out.println();
            
            // Step 3: Enter new date and time
            System.out.print("Enter new date and time (yyyy-MM-dd HH:mm): ");
            String dateTimeStr = scanner.nextLine();
            LocalDateTime newDateTime = LocalDateTime.parse(dateTimeStr, dtf);
            
            // Validate time is in working hours
            int hour = newDateTime.getHour();
            if (hour < 9 || hour >= 17) {
                System.out.println("Error: Consultation hours are 9:00 AM to 5:00 PM only.");
                return;
            }
            
            // Step 4: System checks doctor availability
            System.out.println("\nChecking doctor availability for new time...");
            ArrayList<String> availableDoctors = controller.getAvailableDoctorsForDateTime(newDateTime);
            
            if (availableDoctors.isEmpty()) {
                System.out.println("No doctors available at this new time. Please choose another time slot.");
                return;
            }
            
            // Step 5: Confirm and update appointment
            System.out.println("Available doctors for new time slot:");
            for (int i = 0; i < availableDoctors.size(); i++) {
                String doctor = availableDoctors.get(i);
                System.out.println("• " + doctor);
            }
            
            System.out.print("\nConfirm reschedule? (y/n): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("y")) {
                boolean success = controller.rescheduleConsultation(consultationId, newDateTime);
                if (success) {
                    System.out.println("✓ Consultation rescheduled successfully!");
                    System.out.println("New date & time: " + newDateTime.format(dtf));
                } else {
                    System.out.println("Error: Failed to reschedule consultation.");
                }
            } else {
                System.out.println("Rescheduling cancelled.");
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please ensure all inputs are in the correct format.");
        }
    }

    private void cancelAppointment() {
        System.out.println("=== Cancel Consultation ===");
        System.out.println();
        
        try {
            // Step 1: Enter Consultation ID
            System.out.print("Enter Consultation ID: ");
            String consultationId = scanner.nextLine();
            
            Consultation consultation = controller.getConsultationById(consultationId);
            if (consultation == null) {
                System.out.println("Error: Consultation not found.");
                return;
            }
            
            // Show consultation details
            System.out.println("\nConsultation details:");
            System.out.println("Consultation ID: " + consultation.getConsultationId());
            System.out.println("Patient ID: " + consultation.getPatientId());
            System.out.println("Doctor ID: " + consultation.getDoctorId());
            System.out.println("Date & Time: " + consultation.getAppointmentDateTime().format(dtf));
            System.out.println("Status: " + consultation.getStatus());
            System.out.println();
            
            // Step 2: Optional cancellation reason
            System.out.print("Enter cancellation reason (optional): ");
            String reason = scanner.nextLine();
            
            // Step 3: Confirm cancellation
            System.out.print("Confirm cancellation? (y/n): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("y")) {
                boolean success = controller.cancelConsultation(consultationId);
                if (success) {
                    System.out.println("✓ Consultation cancelled successfully!");
                    if (!reason.isEmpty()) {
                        System.out.println("Reason: " + reason);
                        System.out.println("(Note: Reason stored for statistics)");
                    }
                } else {
                    System.out.println("Error: Failed to cancel consultation.");
                }
            } else {
                System.out.println("Cancellation cancelled.");
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void manageConsultationRecord() {
        System.out.println("=== Manage Consultation Record (Doctor) ===");
        System.out.println("Purpose: For doctors to update patient consultation details after it occurs");
        System.out.println();
        
        try {
            // Step 1: Enter Consultation ID
            System.out.print("Enter Consultation ID: ");
            String consultationId = scanner.nextLine();
            
            Consultation consultation = controller.getConsultationById(consultationId);
            if (consultation == null) {
                System.out.println("Error: Consultation not found.");
                return;
            }
            
            // Show current consultation details
            System.out.println("\nCurrent consultation details:");
            System.out.println("Consultation ID: " + consultation.getConsultationId());
            System.out.println("Patient ID: " + consultation.getPatientId());
            System.out.println("Doctor ID: " + consultation.getDoctorId());
            System.out.println("Date & Time: " + consultation.getAppointmentDateTime().format(dtf));
            System.out.println("Status: " + consultation.getStatus());
            System.out.println();
            
            // Step 2-5: Enter medical details
            System.out.print("Enter Symptoms: ");
            String symptoms = scanner.nextLine();
            System.out.print("Enter Diagnosis: ");
            String diagnosis = scanner.nextLine();
            System.out.print("Enter Prescription: ");
            String prescription = scanner.nextLine();
            System.out.print("Enter Notes (optional): ");
            String notes = scanner.nextLine();
            
            // Save updates to consultation record
            boolean success = controller.updateConsultationRecord(consultationId, symptoms, diagnosis, prescription, notes);
            if (success) {
                System.out.println("\n✓ Consultation record updated successfully!");
                System.out.println("Status changed to: COMPLETED");
            } else {
                System.out.println("Error: Failed to update consultation record.");
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewConsultationHistory() {
        System.out.println("=== View Patient Consultation History (Doctor) ===");
        System.out.println();
        
        try {
            // Step 1: Enter Patient ID
            System.out.print("Enter Patient ID: ");
            String patientId = scanner.nextLine();
            
            if (!controller.validatePatientId(patientId)) {
                System.out.println("Error: Patient ID not found.");
                return;
            }
            
            // Step 2: Display all past consultations
            System.out.println("\nRetrieving consultation history...");
            ArrayList<Consultation> consultations = controller.getConsultationsByPatient(patientId);
            
            if (consultations.isEmpty()) {
                System.out.println("No consultation history found for this patient.");
                return;
            }
            
            System.out.println("\n=== Consultation History for Patient " + patientId + " ===");
            System.out.println("Total consultations: " + consultations.size());
            System.out.println();
            
            for (int i = 0; i < consultations.size(); i++) {
                Consultation c = consultations.get(i);
                System.out.println("Consultation " + (i + 1) + ":");
                System.out.println("  ID: " + c.getConsultationId());
                System.out.println("  Date: " + c.getAppointmentDateTime().format(dtf));
                System.out.println("  Doctor: " + c.getDoctorId());
                System.out.println("  Status: " + c.getStatus());
                
                if (c.getSymptoms() != null && !c.getSymptoms().isEmpty()) {
                    System.out.println("  Symptoms: " + c.getSymptoms());
                }
                if (c.getDiagnosis() != null && !c.getDiagnosis().isEmpty()) {
                    System.out.println("  Diagnosis: " + c.getDiagnosis());
                }
                if (c.getPrescription() != null && !c.getPrescription().isEmpty()) {
                    System.out.println("  Prescription: " + c.getPrescription());
                }
                if (c.getNotes() != null && !c.getNotes().isEmpty()) {
                    System.out.println("  Notes: " + c.getNotes());
                }
                System.out.println();
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void viewReports() {
        System.out.println("=== View Reports ===");
        System.out.println();
        
        while (true) {
            System.out.println("Choose a report type:");
            System.out.println("1. Doctor-Patient Relationship Report (Bar Chart & Pie Chart)");
            System.out.println("2. Follow-up Path Report (Timeline & Flowchart)");
            System.out.println("3. Create Sample Data for Reports");
            System.out.println("4. Back to main menu");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    showDoctorPatientRelationshipReport();
                    break;
                case 2:
                    showFollowUpPathReport();
                    break;
                case 3:
                    createSampleDataForReports();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void showDoctorPatientRelationshipReport() {
        System.out.println("\n=== DOCTOR-PATIENT RELATIONSHIP REPORT ===");
        System.out.println("Bar Chart: X-axis = Doctors, Y-axis = Number of Patients consulted");
        System.out.println("Pie Chart: Each slice = proportion of patients per doctor");
        System.out.println();
        
        ConsultationReportController reportController = new ConsultationReportController();
        
        // Get all consultations to build the report
        ArrayList<Consultation> allConsultations = controller.getAllConsultations();
        
        if (allConsultations.isEmpty()) {
            System.out.println("No consultations found. Please create some sample data first.");
            return;
        }
        
        // Count patients per doctor
        HashMap<String, Integer> doctorPatientCount = new HashMap<>();
        HashMap<String, ArrayList<String>> doctorPatients = new HashMap<>();
        
        for (int i = 0; i < allConsultations.size(); i++) {
            Consultation c = allConsultations.get(i);
            String doctorId = c.getDoctorId();
            String patientId = c.getPatientId();
            
            // Count unique patients per doctor
            if (!doctorPatients.containsKey(doctorId)) {
                doctorPatients.put(doctorId, new ArrayList<>());
            }
            
            ArrayList<String> patients = doctorPatients.get(doctorId);
            if (!patients.contains(patientId)) {
                patients.add(patientId);
                doctorPatientCount.put(doctorId, patients.size());
            }
        }
        
        // Display Bar Chart
        System.out.println("📊 BAR CHART - Patients per Doctor");
        System.out.println("==================================");
        System.out.println("Doctor ID    | Patients | Bar Chart");
        System.out.println("-------------|----------|----------");
        
        int totalPatients = 0;
        // Get all doctor IDs from the HashMap
        ArrayList<String> doctorIds = new ArrayList<>();
        // Since we can't iterate by index through HashMap, we'll collect the doctor IDs as we process consultations
        for (int i = 0; i < allConsultations.size(); i++) {
            Consultation c = allConsultations.get(i);
            String doctorId = c.getDoctorId();
            if (!doctorIds.contains(doctorId)) {
                doctorIds.add(doctorId);
            }
        }
        
        for (int i = 0; i < doctorIds.size(); i++) {
            String doctorId = doctorIds.get(i);
            int patientCount = doctorPatientCount.get(doctorId);
            totalPatients += patientCount;
            
            // Create bar chart using ASCII
            StringBuilder bar = new StringBuilder();
            for (int j = 0; j < patientCount; j++) {
                bar.append("█");
            }
            
            System.out.printf("%-12s | %-8d | %s%n", doctorId, patientCount, bar.toString());
        }
        
        System.out.println();
        
        // Display Pie Chart
        System.out.println("🥧 PIE CHART - Patient Distribution");
        System.out.println("===================================");
        
        for (int i = 0; i < doctorIds.size(); i++) {
            String doctorId = doctorIds.get(i);
            int patientCount = doctorPatientCount.get(doctorId);
            double percentage = (double) patientCount / totalPatients * 100;
            
            // Create pie slice using ASCII
            int sliceSize = (int) (percentage / 10);
            StringBuilder slice = new StringBuilder();
            for (int j = 0; j < sliceSize; j++) {
                slice.append("█");
            }
            
            System.out.printf("%-12s | %-8d | %-6.1f%% | %s%n", 
                doctorId, patientCount, percentage, slice.toString());
        }
        
        System.out.println();
        System.out.println("📈 SUMMARY:");
        System.out.println("Total Doctors: " + doctorPatientCount.size());
        System.out.println("Total Patients: " + totalPatients);
        
        // Find doctor with most consultations
        String busiestDoctor = "";
        int maxPatients = 0;
        for (int i = 0; i < doctorIds.size(); i++) {
            String doctorId = doctorIds.get(i);
            int patientCount = doctorPatientCount.get(doctorId);
            if (patientCount > maxPatients) {
                maxPatients = patientCount;
                busiestDoctor = doctorId;
            }
        }
        
        System.out.println("Busiest Doctor: " + busiestDoctor + " (" + maxPatients + " patients)");
    }
    
    private void showFollowUpPathReport() {
        System.out.println("\n=== FOLLOW-UP PATH REPORT ===");
        System.out.println("Timeline: X-axis = Consultation dates, Y-axis = Number of follow-ups");
        System.out.println("Flowchart: Nodes = consultations, Edges = follow-up links");
        System.out.println();
        
        System.out.print("Enter Patient ID to view follow-up path (e.g., P001): ");
        String patientId = scanner.nextLine();
        
        ConsultationReportController reportController = new ConsultationReportController();
        ArrayList<String> followUpPath = reportController.getFollowUpPathForPatient(patientId);
        
        if (followUpPath.isEmpty()) {
            System.out.println("No follow-up consultations found for patient " + patientId);
            System.out.println("Try creating sample data first or check the patient ID.");
            return;
        }
        
        // Display Timeline
        System.out.println("📅 TIMELINE - Consultation Journey");
        System.out.println("==================================");
        
        for (int i = 0; i < followUpPath.size(); i++) {
            String consultationId = followUpPath.get(i);
            Consultation consultation = controller.getConsultationById(consultationId);
            
            if (consultation != null) {
                String date = consultation.getAppointmentDateTime().format(dtf);
                String doctorId = consultation.getDoctorId();
                String status = consultation.getStatus();
                
                // Create timeline visualization
                StringBuilder timeline = new StringBuilder();
                for (int j = 0; j < i; j++) {
                    timeline.append("    ");
                }
                timeline.append("├── ");
                
                System.out.printf("%s %s | %s | %s | %s%n", 
                    timeline.toString(), date, consultationId, doctorId, status);
            }
        }
        
        System.out.println();
        
        // Display Flowchart
        System.out.println("🔄 FLOWCHART - Follow-up Chain");
        System.out.println("===============================");
        
        for (int i = 0; i < followUpPath.size(); i++) {
            String consultationId = followUpPath.get(i);
            Consultation consultation = controller.getConsultationById(consultationId);
            
            if (consultation != null) {
                String date = consultation.getAppointmentDateTime().format(dtf);
                String doctorId = consultation.getDoctorId();
                
                // Create flowchart node
                System.out.println("┌─────────────────────────────────────┐");
                System.out.printf("│ Consultation: %s%n", consultationId);
                System.out.printf("│ Date: %s%n", date);
                System.out.printf("│ Doctor: %s%n", doctorId);
                System.out.println("└─────────────────────────────────────┘");
                
                // Add arrow to next consultation
                if (i < followUpPath.size() - 1) {
                    System.out.println("                    ↓");
                    System.out.println("              Follow-up");
                    System.out.println("                    ↓");
                }
            }
        }
        
        System.out.println();
        System.out.println("📊 SUMMARY:");
        System.out.println("Total Consultations in Chain: " + followUpPath.size());
        System.out.println("Patient ID: " + patientId);
        
        // Show consultation details
        System.out.println("\n📋 CONSULTATION DETAILS:");
        for (int i = 0; i < followUpPath.size(); i++) {
            String consultationId = followUpPath.get(i);
            Consultation consultation = controller.getConsultationById(consultationId);
            
            if (consultation != null) {
                System.out.printf("%d. %s%n", i + 1, consultation.toString());
            }
        }
    }
    

    
    private void createSampleDataForReports() {
        System.out.println("Creating sample consultation data for reports...");
        
        // Create sample consultations
        LocalDateTime now = LocalDateTime.now();
        
        // Sample consultations for Dr. Ahmad (D001)
        Consultation c1 = controller.createConsultation("P001", "D001", 
            now.minusDays(5).withHour(9).withMinute(0));
        Consultation c2 = controller.createConsultation("P002", "D001", 
            now.minusDays(3).withHour(10).withMinute(0));
        Consultation c3 = controller.createConsultation("P003", "D001", 
            now.minusDays(1).withHour(11).withMinute(0));
        
        // Sample consultations for Dr. Sarah (D002)
        Consultation c4 = controller.createConsultation("P001", "D002", 
            now.minusDays(2).withHour(14).withMinute(0));
        Consultation c5 = controller.createConsultation("P004", "D002", 
            now.minusDays(1).withHour(15).withMinute(0));
        
        // Sample consultations for Dr. John (D003)
        Consultation c6 = controller.createConsultation("P002", "D003", 
            now.minusDays(1).withHour(16).withMinute(0));
        Consultation c7 = controller.createConsultation("P005", "D003", 
            now.withHour(9).withMinute(0));
        
        // Create follow-up relationships
        c1.setFollowUpConsultationId(c4.getConsultationId()); // P001: D001 → D002
        c2.setFollowUpConsultationId(c6.getConsultationId()); // P002: D001 → D003
        
        System.out.println("Sample data created successfully!");
        System.out.println("Created " + controller.getConsultationCount() + " consultations");
        System.out.println("Created " + controller.getConsultationsWithFollowUps().size() + " follow-up relationships");
    }
}
