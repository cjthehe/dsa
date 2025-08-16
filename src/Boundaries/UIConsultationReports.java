package Boundaries;

import Controller.ConsultationReportController;
import Controller.ConsultationController;
import Entity.Consultation;
import ADT.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * UI for Consultation Reports - Demonstrates the two graph-based reports
 */
public class UIConsultationReports {
    private ConsultationReportController reportController;
    private ConsultationController consultationController;
    private Scanner scanner;
    
    public UIConsultationReports() {
        this.reportController = new ConsultationReportController();
        this.consultationController = new ConsultationController();
        this.scanner = new Scanner(System.in);
    }
    
    public void showMainMenu() {
        while (true) {
            System.out.println("\n=== CONSULTATION REPORTS MENU ===");
            System.out.println("1. Doctor-Patient Relationship Report");
            System.out.println("2. Consultation Dependency Graph Report");
            System.out.println("3. Create Sample Data");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    showDoctorPatientReportMenu();
                    break;
                case 2:
                    showConsultationDependencyReportMenu();
                    break;
                case 3:
                    createSampleData();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void showDoctorPatientReportMenu() {
        System.out.println("\n=== DOCTOR-PATIENT RELATIONSHIP REPORT ===");
        System.out.println("1. Show patients for a specific doctor this month");
        System.out.println("2. Find patients who consulted multiple doctors");
        System.out.println("3. Show referral chains");
        System.out.println("4. Generate complete report for a doctor");
        System.out.println("5. Back to main menu");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch (choice) {
            case 1:
                showPatientsForDoctor();
                break;
            case 2:
                showPatientsWithMultipleDoctors();
                break;
            case 3:
                showReferralChains();
                break;
            case 4:
                generateCompleteDoctorReport();
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private void showConsultationDependencyReportMenu() {
        System.out.println("\n=== CONSULTATION DEPENDENCY GRAPH REPORT ===");
        System.out.println("1. Show follow-up path for a patient");
        System.out.println("2. Show linked consultations for a consultation");
        System.out.println("3. Create follow-up consultation");
        System.out.println("4. Back to main menu");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch (choice) {
            case 1:
                showFollowUpPathForPatient();
                break;
            case 2:
                showLinkedConsultations();
                break;
            case 3:
                createFollowUpConsultation();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private void showPatientsForDoctor() {
        System.out.print("Enter Doctor ID (e.g., D001): ");
        String doctorId = scanner.nextLine();
        
        ArrayList<String> patients = reportController.getPatientsForDoctorThisMonth(doctorId);
        
        System.out.println("\nPatients consulted by " + doctorId + " this month:");
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            for (int i = 0; i < patients.size(); i++) {
                System.out.println("  - " + patients.get(i));
            }
        }
    }
    
    private void showPatientsWithMultipleDoctors() {
        ArrayList<String> patients = reportController.getPatientsWithMultipleDoctors();
        
        System.out.println("\nPatients who consulted multiple doctors:");
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            for (int i = 0; i < patients.size(); i++) {
                System.out.println("  - " + patients.get(i));
            }
        }
    }
    
    private void showReferralChains() {
        ArrayList<ArrayList<String>> chains = reportController.getReferralChains();
        
        System.out.println("\nReferral chains found:");
        if (chains.isEmpty()) {
            System.out.println("No referral chains found.");
        } else {
            for (int i = 0; i < chains.size(); i++) {
                ArrayList<String> chain = chains.get(i);
                System.out.println("  " + (i + 1) + ". " + chain.get(0) + " → " + chain.get(1) + " → " + chain.get(2));
            }
        }
    }
    
    private void generateCompleteDoctorReport() {
        System.out.print("Enter Doctor ID (e.g., D001): ");
        String doctorId = scanner.nextLine();
        
        reportController.printDoctorPatientReport(doctorId);
    }
    
    private void showFollowUpPathForPatient() {
        System.out.print("Enter Patient ID (e.g., P001): ");
        String patientId = scanner.nextLine();
        
        reportController.printFollowUpPathReport(patientId);
    }
    
    private void showLinkedConsultations() {
        System.out.print("Enter Consultation ID (e.g., C010124001): ");
        String consultationId = scanner.nextLine();
        
        ArrayList<String> linked = reportController.getLinkedConsultations(consultationId);
        
        System.out.println("\nLinked consultations for " + consultationId + ":");
        if (linked.isEmpty()) {
            System.out.println("No linked consultations found.");
        } else {
            for (int i = 0; i < linked.size(); i++) {
                Consultation c = consultationController.getConsultationById(linked.get(i));
                if (c != null) {
                    System.out.println("  " + (i + 1) + ". " + c.toString());
                }
            }
        }
    }
    
    private void createFollowUpConsultation() {
        System.out.print("Enter original Consultation ID: ");
        String originalId = scanner.nextLine();
        
        System.out.print("Enter new Doctor ID: ");
        String newDoctorId = scanner.nextLine();
        
        System.out.print("Enter new date and time (yyyy-MM-dd HH:mm): ");
        String dateTimeStr = scanner.nextLine();
        
        try {
            LocalDateTime newDateTime = LocalDateTime.parse(dateTimeStr, 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            
            Consultation followUp = reportController.createFollowUpConsultation(
                originalId, newDoctorId, newDateTime);
            
            if (followUp != null) {
                System.out.println("Follow-up consultation created successfully:");
                System.out.println("  " + followUp.toString());
            } else {
                System.out.println("Failed to create follow-up consultation.");
            }
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm");
        }
    }
    
    private void createSampleData() {
        System.out.println("Creating sample consultation data...");
        
        // Create sample consultations
        LocalDateTime now = LocalDateTime.now();
        
        // Sample consultations for Dr. Ahmad (D001)
        Consultation c1 = consultationController.createConsultation("P001", "D001", 
            now.minusDays(5).withHour(9).withMinute(0));
        Consultation c2 = consultationController.createConsultation("P002", "D001", 
            now.minusDays(3).withHour(10).withMinute(0));
        Consultation c3 = consultationController.createConsultation("P003", "D001", 
            now.minusDays(1).withHour(11).withMinute(0));
        
        // Sample consultations for Dr. Sarah (D002)
        Consultation c4 = consultationController.createConsultation("P001", "D002", 
            now.minusDays(2).withHour(14).withMinute(0));
        Consultation c5 = consultationController.createConsultation("P004", "D002", 
            now.minusDays(1).withHour(15).withMinute(0));
        
        // Sample consultations for Dr. John (D003)
        Consultation c6 = consultationController.createConsultation("P002", "D003", 
            now.minusDays(1).withHour(16).withMinute(0));
        Consultation c7 = consultationController.createConsultation("P005", "D003", 
            now.withHour(9).withMinute(0));
        
        // Create follow-up relationships
        c1.setFollowUpConsultationId(c4.getConsultationId()); // P001: D001 → D002
        c2.setFollowUpConsultationId(c6.getConsultationId()); // P002: D001 → D003
        
        System.out.println("Sample data created successfully!");
        System.out.println("Created " + consultationController.getConsultationCount() + " consultations");
        System.out.println("Created " + consultationController.getConsultationsWithFollowUps().size() + " follow-up relationships");
    }
    
    public static void main(String[] args) {
        UIConsultationReports ui = new UIConsultationReports();
        ui.showMainMenu();
    }
}
