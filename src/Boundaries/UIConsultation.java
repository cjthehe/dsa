package Boundaries;

import Controller.ConsultationController;
import Entity.Consultation;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class UIConsultation {
    private Scanner scanner = new Scanner(System.in);
    private ConsultationController controller = new ConsultationController();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void showMenu() {
        while (true) {
            System.out.println("\n===== Consultation Module =====");
            System.out.println("1. Create Consultation Appointment");
            System.out.println("2. Reschedule Consultation");
            System.out.println("3. Cancel Consultation");
            System.out.println("4. Manage Consultation Record (Doctor)");
            System.out.println("5. View Patient Consultation History");
            System.out.println("6. View Doctor Availability");
            System.out.println("7. Exit");
            System.out.print("Select your option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createAppointment();
                    break;
                case 2:
                    rescheduleAppointment();
                    break;
                case 3:
                    cancelAppointment();
                    break;
                case 4:
                    manageConsultationRecord();
                    break;
                case 5:
                    viewConsultationHistory();
                    break;
                case 6:
                    controller.printDoctorAvailability();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createAppointment() {
        System.out.println("\n=== Create Consultation Appointment ===");
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        controller.printDoctorAvailability();
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();
        System.out.print("Enter Appointment Date and Time (yyyy-MM-dd HH:mm): ");
        String dateTimeStr = scanner.nextLine();
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, dtf);
        Consultation c = controller.createConsultation(patientId, doctorId, dateTime);
        System.out.println("Appointment created: " + c);
    }

    private void rescheduleAppointment() {
        System.out.println("\n=== Reschedule Consultation ===");
        System.out.print("Enter Consultation ID: ");
        String consultationId = scanner.nextLine();
        System.out.print("Enter New Date and Time (yyyy-MM-dd HH:mm): ");
        String dateTimeStr = scanner.nextLine();
        LocalDateTime newDateTime = LocalDateTime.parse(dateTimeStr, dtf);
        boolean success = controller.rescheduleConsultation(consultationId, newDateTime);
        if (success) {
            System.out.println("Consultation rescheduled successfully.");
        } else {
            System.out.println("Consultation not found.");
        }
    }

    private void cancelAppointment() {
        System.out.println("\n=== Cancel Consultation ===");
        System.out.print("Enter Consultation ID: ");
        String consultationId = scanner.nextLine();
        boolean success = controller.cancelConsultation(consultationId);
        if (success) {
            System.out.println("Consultation cancelled successfully.");
        } else {
            System.out.println("Consultation not found.");
        }
    }

    private void manageConsultationRecord() {
        System.out.println("\n=== Manage Consultation Record (Doctor) ===");
        System.out.print("Enter Consultation ID: ");
        String consultationId = scanner.nextLine();
        System.out.print("Enter Symptoms: ");
        String symptoms = scanner.nextLine();
        System.out.print("Enter Diagnosis: ");
        String diagnosis = scanner.nextLine();
        System.out.print("Enter Prescription: ");
        String prescription = scanner.nextLine();
        System.out.print("Enter Notes: ");
        String notes = scanner.nextLine();
        boolean success = controller.updateConsultationRecord(consultationId, symptoms, diagnosis, prescription, notes);
        if (success) {
            System.out.println("Consultation record updated.");
        } else {
            System.out.println("Consultation not found.");
        }
    }

    private void viewConsultationHistory() {
        System.out.println("\n=== View Patient Consultation History ===");
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        List<Consultation> list = controller.getConsultationsByPatient(patientId);
        if (list.isEmpty()) {
            System.out.println("No consultation history found for this patient.");
        } else {
            for (Consultation c : list) {
                System.out.println(c);
            }
        }
    }
}
