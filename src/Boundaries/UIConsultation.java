package Boundaries;

import Controller.ConsultationController;
import Controller.PatientController;
import Entity.Consultation;
import Entity.Patient;
import Main.Asgm;

import ADT.HashMap;
import ADT.ArrayList;
import ADT.LinkedList;
import ADT.Graph;
import ADT.ListInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UIConsultation {
    private Asgm asgm = new Asgm();
    private Scanner scanner = new Scanner(System.in);
    private ConsultationController controller = new ConsultationController();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private static final String[] COLORS = new String[]{
        "\u001B[31m", // Red
        "\u001B[33m", // Yellow
        "\u001B[32m", // Green
        "\u001B[36m", // Cyan
        "\u001B[34m", // Blue
        "\u001B[35m", // Magenta
        "\u001B[91m", // Bright Red
        "\u001B[92m", // Bright Green
        "\u001B[93m", // Bright Yellow
        "\u001B[94m"  // Bright Blue
    };
    private static final String RESET = "\u001B[0m";

   
    private String getValidPatientId() {
        String patientId;
        do {
            System.out.print("Enter Patient ID: ");
            patientId = scanner.nextLine().trim();
            
            if (patientId.isEmpty()) {
                System.out.println("Error: Patient ID cannot be empty. Please try again.");
                continue;
            }
            
            if (!Consultation.isValidPatientId(patientId)) {
                System.out.println("Error: Invalid patient ID format. Expected: P + 4 digits (e.g., P0001)");
                continue;
            }
            
            if (!controller.validatePatientId(patientId)) {
                System.out.println("Error: Patient ID not found. Please register the patient first.");
                continue;
            }
            
            // Check if patient is the first in the queue (only first patient can create consultation)
            PatientController patientController = PatientController.getInstance();
            if (!patientController.isFirstPatientInQueue(patientId)) {
                if (!patientController.isPatientInQueue(patientId)) {
                    System.out.println("Error: Patient " + patientId + " is not in the queue.");
                    System.out.println("Only patients in the queue can create consultation appointments.");
                    System.out.println("Please register the patient first or ensure they are added to the queue.");
                } else {
                    System.out.println("Error: Patient " + patientId + " is not the first in queue.");
                    System.out.println("Only the first patient in the queue can create consultation appointments.");
                    System.out.println("Please wait for your turn or check your position in the queue.");
                }
                continue;
            }
            
            break; 
        } while (true);
        
        return patientId;
    }

    private String getValidPatientIdForHistory() {
        String patientId;
        do {
            System.out.print("Enter Patient ID: ");
            patientId = scanner.nextLine().trim();
            
            if (patientId.isEmpty()) {
                System.out.println("Error: Patient ID cannot be empty. Please try again.");
                continue;
            }
            
            if (!Consultation.isValidPatientId(patientId)) {
                System.out.println("Error: Invalid patient ID format. Expected: P + 4 digits (e.g., P0001)");
                continue;
            }
            
            // Check if patient exists in the system (either in queue or has consultation history)
            PatientController patientController = PatientController.getInstance();
            boolean patientExists = controller.validatePatientId(patientId);
            boolean hasHistory = !controller.getConsultationsByPatient(patientId).isEmpty();
            
            if (!patientExists && !hasHistory) {
                System.out.println("Error: Patient ID " + patientId + " not found in the system.");
                System.out.println("This patient has never been registered or has no consultation history.");
                continue;
            }
            
            
            if (hasHistory) {
                System.out.println("Patient " + patientId + " has consultation history available");
            }
            
            break; 
        } while (true);
        
        return patientId;
    }

    private LocalDateTime getValidDateTime() {
        LocalDateTime dateTime;
        do {
            System.out.print("Enter desired date and time (yyyy-MM-dd HH:mm): ");
            String dateTimeStr = scanner.nextLine().trim();
            
            if (dateTimeStr.isEmpty()) {
                System.out.println("Error: Date and time cannot be empty. Please try again.");
                continue;
            }
            
            try {
                dateTime = LocalDateTime.parse(dateTimeStr, dtf);
                break; 
            } catch (Exception e) {
                System.out.println("Error: Invalid date/time format. Please use yyyy-MM-dd HH:mm format.");
            }
        } while (true);
        
        return dateTime;
    }

    private String getValidConsultationId() {
        String consultationId;
        do {
            System.out.print("Enter Consultation ID: ");
            consultationId = scanner.nextLine().trim();
            
            if (consultationId.isEmpty()) {
                System.out.println("Error: Consultation ID cannot be empty. Please try again.");
                continue;
            }
            
            if (!Consultation.isValidConsultationId(consultationId)) {
                System.out.println("Error: Invalid consultation ID format. Expected: C + 9 digits");
                continue;
            }
            
            Consultation consultation = controller.getConsultationById(consultationId);
            if (consultation == null) {
                System.out.println("Error: Consultation not found. Please try again.");
                continue;
            }
            
            break; 
        } while (true);
        
        return consultationId;
    }

    private int getValidDoctorChoice(int maxDoctors) {
        int doctorChoice;
        do {
            System.out.print("Select doctor (enter number): ");
            String choiceStr = scanner.nextLine().trim();
            
            if (choiceStr.isEmpty()) {
                System.out.println("Error: Please enter a number. Please try again.");
                continue;
            }
            
            try {
                doctorChoice = Integer.parseInt(choiceStr);
                if (doctorChoice < 1 || doctorChoice > maxDoctors) {
                    System.out.println("Error: Invalid selection. Please enter a number between 1 and " + maxDoctors);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        } while (true);
        
        return doctorChoice;
    }

    private boolean getValidConfirmation(String message) {
        String confirm;
        do {
            System.out.print(message + " (y/n): ");
            confirm = scanner.nextLine().trim().toLowerCase();
            
            if (confirm.isEmpty()) {
                System.out.println("Error: Please enter 'y' for yes or 'n' for no.");
                continue;
            }
            
            if (confirm.equals("y") || confirm.equals("yes")) {
                return true;
            } else if (confirm.equals("n") || confirm.equals("no")) {
                return false;
            } else {
                System.out.println("Error: Please enter 'y' for yes or 'n' for no.");
            }
        } while (true);
    }

    private String getValidConsultationRecordInput(String fieldName, int minLength, int maxLength, boolean required) {
        String input;
        do {
            System.out.print("Enter " + fieldName + " (" + minLength + "-" + maxLength + " characters" + (required ? "" : ", optional") + "): ");
            input = scanner.nextLine().trim();
            
            if (!required && input.isEmpty()) {
                return input;           
            }
            
            if (input.isEmpty()) {
                System.out.println("Error: " + fieldName + " cannot be empty. Please try again.");
                continue;
            }
            
            if (input.length() < minLength || input.length() > maxLength) {
                System.out.println("Error: " + fieldName + " must be between " + minLength + " and " + maxLength + " characters.");
                continue;
            }
            
            break; 
        } while (true);
        
        return input;
    }

    private double getValidConsultationHours() {
        double consultationHr;
        do {
            System.out.print("Enter Consultation Hour (0-8 hours, e.g. 0.5): ");
            String hoursStr = scanner.nextLine().trim();
            
            if (hoursStr.isEmpty()) {
                System.out.println("Error: Consultation hours cannot be empty. Please try again.");
                continue;
            }
            
            try {
                consultationHr = Double.parseDouble(hoursStr);
                if (consultationHr < 0 || consultationHr > 8) {
                    System.out.println("Error: Consultation hours must be between 0 and 8 hours.");
                    continue;
                }
                break; 
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        } while (true);
        
        return consultationHr;
    }

    private void initializeDoctorsIfNeeded() {
        Controller.DoctorController doctorController = new Controller.DoctorController();
        LinkedList<Entity.Doctor> doctors = doctorController.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("Setting up doctors and time slots...");
            doctorController.addDoctor("Dr. Cjt", "Basic Cardiology", 15, 'F', "012-3456789", "cjt@clinic.com", java.time.LocalDate.now());
            doctorController.addDoctor("Dr. QN", "General Practice", 9, 'F', "012-3456790", "qn@clinic.com", java.time.LocalDate.now());
            doctorController.addDoctor("Dr. WN", "Orthopedics", 12, 'F', "012-3456791", "wn@clinic.com", java.time.LocalDate.now());
            doctorController.addDoctor("Dr. JW", "Orthopedics", 30, 'M', "012-3456792", "jw@clinic.com", java.time.LocalDate.now());
            doctorController.addDoctor("Dr. CGZ", "Dermatology", 11, 'M', "012-3456793", "cgz@clinic.com", java.time.LocalDate.now());
        }
    }

    private void setupTimeSlotsForDate(LocalDate date) {
        Controller.DoctorController doctorController = new Controller.DoctorController();
        LinkedList<Entity.Doctor> doctors = doctorController.getAllDoctors();
        
        boolean anySlots = false;
        for (int i = 0; i < doctors.size(); i++) {
            String did = doctors.get(i).getDoctorId();
            if (!doctorController.getSlotsForDate(did, date).isEmpty()) {
                anySlots = true;
                break;
            }
        }
        
        if (!anySlots) {
            if (doctors.size() >= 1) {
                String did1 = doctors.get(0).getDoctorId();
                doctorController.defineAvailableSlots(did1, date, java.time.LocalTime.of(8,0), java.time.LocalTime.of(10,0), 60);
            }
            if (doctors.size() >= 2) {
                String did2 = doctors.get(1).getDoctorId();
                doctorController.defineAvailableSlots(did2, date, java.time.LocalTime.of(10,0), java.time.LocalTime.of(12,0), 60);
            }
            if (doctors.size() >= 3) {
                String did3 = doctors.get(2).getDoctorId();
                doctorController.defineAvailableSlots(did3, date, java.time.LocalTime.of(12,0), java.time.LocalTime.of(15,0), 60);
            }
            if (doctors.size() >= 4) {
                String did4 = doctors.get(3).getDoctorId();
                doctorController.defineAvailableSlots(did4, date, java.time.LocalTime.of(15,0), java.time.LocalTime.of(17,0), 60);
            }
            if (doctors.size() >= 5) {
                String did5 = doctors.get(4).getDoctorId();
                doctorController.defineAvailableSlots(did5, date, java.time.LocalTime.of(17,0), java.time.LocalTime.of(19,0), 60);
            }
        }
    }

    public void showMenu() {
        while (true) {
            asgm.clearScreen();
            
            System.out.println(" +--------------------------- Consultation Management System ---------------------------+ ");
            System.out.printf(" |%23s%-40s%23s|\n", "", "1. View Doctor Availability", "");
            System.out.printf(" |%23s%-40s%23s|\n", "", "2. Create Consultation Appointment", "");
            System.out.printf(" |%23s%-40s%23s|\n", "", "3. Reschedule Consultation", "");
            System.out.printf(" |%23s%-40s%23s|\n", "", "4. Cancel Consultation", "");
            System.out.printf(" |%23s%-40s%23s|\n", "", "5. Manage Consultation Record", "");
            System.out.printf(" |%23s%-40s%23s|\n", "", "6. View Patient Consultation History", "");
            System.out.printf(" |%23s%-40s%23s|\n", "", "7. View Report", "");
            System.out.printf(" |%23s%-40s%23s|\n", "", "8. Exit", "");
            System.out.println(" +--------------------------------------------------------------------------------------+ ");
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
                    asgm.startMenu();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewDoctorAvailability() {
        System.out.println("\n +------------------------- View Doctor Availability -------------------------+ ");
        System.out.println();
        
        try {
            System.out.print("Enter desired date (yyyy-MM-dd): ");
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);
            
            // Initialize doctors and setup time slots
            initializeDoctorsIfNeeded();
            setupTimeSlotsForDate(date);
            
            // Get doctors for display
            Controller.DoctorController doctorController = new Controller.DoctorController();
            LinkedList<Entity.Doctor> doctors = doctorController.getAllDoctors();

            System.out.println();
            System.out.println("\nRetrieving doctor list and available time slots...");
            System.out.println();
            System.out.println("Available time slots on " + date + ":");
            if (doctors.size() >= 1) {
                System.out.println("8am-10am, " + doctors.get(0).getDoctorId() + " " + doctors.get(0).getName());
            }
            if (doctors.size() >= 2) {
                System.out.println("10am-12pm, " + doctors.get(1).getDoctorId() + " " + doctors.get(1).getName());
            }
            if (doctors.size() >= 3) {
                System.out.println("12pm-3pm, " + doctors.get(2).getDoctorId() + " " + doctors.get(2).getName());
            }
            if (doctors.size() >= 4) {
                System.out.println("3pm-5pm, " + doctors.get(3).getDoctorId() + " " + doctors.get(3).getName());
            }
            if (doctors.size() >= 5) {
                System.out.println("5pm-7pm, " + doctors.get(4).getDoctorId() + " " + doctors.get(4).getName());
            }
            
            // Show available time slots for the date
            LocalDateTime startOfDay = date.atStartOfDay();
            ArrayList<LocalDateTime> availableSlots = controller.getAvailableTimeSlotsForDate(startOfDay);
            
            if (availableSlots.isEmpty()) {
                System.out.println("No available slots found for " + date + ".");
                return;
            }
            
            System.out.println("\nDoctor availability displayed successfully!");
            System.out.println("Note: You can now proceed to create a consultation appointment (Option 2)");
            System.out.println("\nPress Enter to return to main menu...");
            scanner.nextLine();
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("Error: Invalid date format. Please use yyyy-MM-dd format.");
        }
    }

    private void displayPatientQueue() {
        PatientController patientController = PatientController.getInstance();
        ArrayList<Entity.Patient> patientsInQueue = patientController.getAllPatientsInQueue();
        
        System.out.println("\n +------------------------- Current Patient Queue -------------------------+ ");
        
        if (patientsInQueue.isEmpty()) {
            System.out.println("No patients currently in queue.");
            System.out.println("Patients must be registered and added to queue before they can create consultations.");System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            showMenu();
        } else {
            System.out.println("Patients currently in queue:");
            System.out.println("+--------+----------------------+------------------+------------------+--------+");
            System.out.println("| ID     | Name                 | Faculty          | Registration Date| Position|");
            System.out.println("+--------+----------------------+------------------+------------------+--------+");
            
            for (int i = 0; i < patientsInQueue.size(); i++) {
                Entity.Patient patient = patientsInQueue.get(i);
                String position = (i == 0) ? "1st (NEXT)" : String.valueOf(i + 1);
                System.out.printf("| %-6s | %-20s | %-16s | %-16s | %-6s |\n",
                    patient.getID(),
                    patient.getName(),
                    patient.getFaculty(),
                    patient.getRegistrationDate().format(dateFormatter),
                    position);
            }
            System.out.println("+--------+----------------------+------------------+------------------+--------+");
            System.out.println("Note: Only the FIRST patient in queue (marked as '1st (NEXT)') can create consultation appointments.");
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void createAppointment() {
        System.out.println("\n +------------------------- Create Consultation Appointment -------------------------+ ");
        System.out.println("Pre-condition: Patient should check doctor availability first (Option 1)");
        System.out.println();
        
        displayPatientQueue();
        
        // Initialize doctors if needed
        initializeDoctorsIfNeeded();
        
        // Get doctors for later use
        Controller.DoctorController doctorController = new Controller.DoctorController();
        LinkedList<Entity.Doctor> doctors = doctorController.getAllDoctors();
        
        try {
            String patientId = getValidPatientId();
            
            LocalDateTime dateTime = getValidDateTime();
            
            // Validate the appointment creation
            ConsultationController.ValidationResult validation = controller.validateCreateConsultation(patientId, "", dateTime);
            if (!validation.isValid()) {
                System.out.println("Validation Errors:");
                validation.printErrors();
                System.out.println("\nPress Enter to return to main menu...");
                scanner.nextLine();
                return;
            }
            
            // Setup time slots for the requested date
            LocalDate requestedDate = dateTime.toLocalDate();
            setupTimeSlotsForDate(requestedDate);
            
            System.out.println("\nChecking available doctors for " + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "...");
            ArrayList<String> availableDoctors = controller.getAvailableDoctorsForDateTime(dateTime);
            
            if (availableDoctors.isEmpty()) {
                System.out.println("No doctors available at this time. Please choose another time slot.");
                System.out.println("\nPress Enter to return to main menu...");
                scanner.nextLine();
                return;
            }
            
            System.out.println("Available doctors for this time slot:");
            for (int i = 0; i < availableDoctors.size(); i++) {
                System.out.println((i + 1) + ". " + availableDoctors.get(i));
            }
            
            int doctorChoice = getValidDoctorChoice(availableDoctors.size());
            
            String selectedDoctor = availableDoctors.get(doctorChoice - 1);
            String doctorId = selectedDoctor.split(" - ")[0];
            
            validation = controller.validateCreateConsultation(patientId, doctorId, dateTime);
            if (!validation.isValid()) {
                System.out.println("Validation Errors:");
                validation.printErrors();
                System.out.println("\nPress Enter to return to main menu...");
                scanner.nextLine();
                return;
            }
            
            // Generates Consultation ID and saves appointment
            try {
                Consultation consultation = controller.createConsultation(patientId, doctorId, dateTime);
                
                System.out.println("\nAppointment created successfully!");
                System.out.println("Consultation ID: " + consultation.getConsultationId());
                System.out.println("Patient ID: " + consultation.getPatientId());
                System.out.println("Doctor ID: " + consultation.getDoctorId());
                System.out.println("Date & Time: " + consultation.getAppointmentDateTime().format(dtf));
                System.out.println("Status: " + consultation.getStatus());
                
                // Dequeue the patient from the queue after consultation creation
                PatientController patientController = PatientController.getInstance();
                Patient dequeuedPatient = patientController.dequeueFirstPatient();
                
                if (dequeuedPatient != null) {
                    System.out.println("\n" + dequeuedPatient.getName() + " (ID: " + dequeuedPatient.getID() + ") has been removed from the queue.");
                    System.out.println("The next patient in queue can now create a consultation appointment.");
                    
                    // Show updated queue status
                    ArrayList<Entity.Patient> remainingPatients = patientController.getAllPatientsInQueue();
                    if (!remainingPatients.isEmpty()) {
                        System.out.println("\nUpdated Queue Status:");
                        System.out.println("+--------+----------------------+------------------+--------+");
                        System.out.println("| ID     | Name                 | Faculty          | Position|");
                        System.out.println("+--------+----------------------+------------------+--------+");
                        
                        for (int i = 0; i < remainingPatients.size(); i++) {
                            Entity.Patient nextPatient = remainingPatients.get(i);
                            String position = (i == 0) ? "1st (NEXT)" : String.valueOf(i + 1);
                            System.out.printf("| %-6s | %-20s | %-16s | %-6s |\n",
                                nextPatient.getID(),
                                nextPatient.getName(),
                                nextPatient.getFaculty(),
                                position);
                        }
                        System.out.println("+--------+----------------------+------------------+--------+");
                        System.out.println("Note: " + remainingPatients.get(0).getName() + " (ID: " + remainingPatients.get(0).getID() + ") is now NEXT in line.");
                    } else {
                        System.out.println("\nQueue is now empty. No patients waiting for consultation.");
                    }
                }
                
                System.out.println("\nPress Enter to return to main menu...");
                scanner.nextLine();
                
            } catch (IllegalArgumentException e) {
                System.out.println("Error creating appointment: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please ensure all inputs are in the correct format.");
        }
    }

    private void rescheduleAppointment() {
        System.out.println("\n +------------------------- Reschedule Consultation -------------------------+ ");
        System.out.println();
        
        try {
            String consultationId = getValidConsultationId();
            Consultation consultation = controller.getConsultationById(consultationId);
            
            System.out.println("\nCurrent consultation details:");
            System.out.println("Consultation ID: " + consultation.getConsultationId());
            System.out.println("Patient ID: " + consultation.getPatientId());
            System.out.println("Doctor ID: " + consultation.getDoctorId());
            System.out.println("Current Date & Time: " + consultation.getAppointmentDateTime().format(dtf));
            System.out.println("Status: " + consultation.getStatus());
            System.out.println();
            
            LocalDateTime newDateTime = getValidDateTime();
            
            // Validate rescheduling
            ConsultationController.ValidationResult validation = controller.validateRescheduleConsultation(consultationId, newDateTime);
            if (!validation.isValid()) {
                System.out.println("Validation Errors:");
                validation.printErrors();
                System.out.println("\nPress Enter to return to main menu...");
                scanner.nextLine();
                return;
            }
            
            if (validation.hasWarnings()) {
                validation.printWarnings();
            }
            
            // Checks doctor availability
            System.out.println("\nChecking doctor availability for new time...");
            ArrayList<String> availableDoctors = controller.getAvailableDoctorsForDateTime(newDateTime);
            
            if (availableDoctors.isEmpty()) {
                System.out.println("No doctors available at this new time. Please choose another time slot.");
                System.out.println("\nPress Enter to return to main menu...");
                scanner.nextLine();
                return;
            }
            
            System.out.println("Available doctors for new time slot:");
            for (int i = 0; i < availableDoctors.size(); i++) {
                String doctor = availableDoctors.get(i);
                System.out.println("• " + doctor);
            }
            
            boolean confirmReschedule = getValidConfirmation("Confirm reschedule");
            
            if (confirmReschedule) {
                try {
                    boolean success = controller.rescheduleConsultation(consultationId, newDateTime);
                    if (success) {
                        System.out.println();
                        System.out.println("Consultation rescheduled successfully!");
                        System.out.println("New date & time: " + newDateTime.format(dtf));
                        System.out.println("\nPress Enter to return to main menu...");
                        scanner.nextLine();
                    } else {
                        System.out.println("Error: Failed to reschedule consultation.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error rescheduling appointment: " + e.getMessage());
                }
            } else {
                System.out.println("Rescheduling cancelled.");
                System.out.println("\nPress Enter to return to main menu...");
                scanner.nextLine();
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please ensure all inputs are in the correct format.");
        }
    }

    private void cancelAppointment() {
        System.out.println("\n +------------------------- Cancel Consultation -------------------------+ ");
        System.out.println();
        
        try {
            String consultationId = getValidConsultationId();
            Consultation consultation = controller.getConsultationById(consultationId);
            
            System.out.println("\nConsultation details:");
            System.out.println("Consultation ID: " + consultation.getConsultationId());
            System.out.println("Patient ID: " + consultation.getPatientId());
            System.out.println("Doctor ID: " + consultation.getDoctorId());
            System.out.println("Date & Time: " + consultation.getAppointmentDateTime().format(dtf));
            System.out.println("Status: " + consultation.getStatus());
            System.out.println();
            
            // Validate cancellation
            ConsultationController.ValidationResult validation = controller.validateCancelConsultation(consultationId);
            if (!validation.isValid()) {
                System.out.println("Validation Errors:");
                validation.printErrors();
                System.out.println("\nPress Enter to return to main menu...");
                scanner.nextLine();
                return;
            }
            
            if (validation.hasWarnings()) {
                validation.printWarnings();
            }
            
            System.out.print("Enter cancellation reason (optional): ");
            String reason = scanner.nextLine();
            
            boolean confirmCancellation = getValidConfirmation("Confirm cancellation");
            
            if (confirmCancellation) {
                try {
                    boolean success = controller.cancelConsultation(consultationId);
                    if (success) {
                        System.out.println("Consultation cancelled successfully!");
                        if (!reason.isEmpty()) {
                            System.out.println("Reason: " + reason);
                            System.out.println();
                        }
                    } else {
                        System.out.println("Error: Failed to cancel consultation.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error cancelling appointment: " + e.getMessage());
                }
            } else {
                System.out.println("Cancellation cancelled.");
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void manageConsultationRecord() {
        System.out.println("\n +------------------------- Manage Consultation Record (Doctor) -------------------------+ ");
        System.out.println();
        
        try {
            String consultationId = getValidConsultationId();
            Consultation consultation = controller.getConsultationById(consultationId);
            
            System.out.println("\nCurrent consultation details:");
            System.out.println("Consultation ID: " + consultation.getConsultationId());
            System.out.println("Patient ID: " + consultation.getPatientId());
            System.out.println("Doctor ID: " + consultation.getDoctorId());
            System.out.println("Date & Time: " + consultation.getAppointmentDateTime().format(dtf));
            System.out.println("Status: " + consultation.getStatus());
            System.out.println();
            
            String symptoms = getValidConsultationRecordInput("Symptoms", 3, 500, true);
            String diagnosis = getValidConsultationRecordInput("Diagnosis", 3, 200, true);
            String prescription = getValidConsultationRecordInput("Prescription", 3, 300, true);
            String notes = getValidConsultationRecordInput("Notes", 0, 1000, false);
            double consultationHr = getValidConsultationHours();
            
            // Validate all inputs
            ConsultationController.ValidationResult validation = controller.validateUpdateConsultationRecord(
                consultationId, symptoms, diagnosis, prescription, notes, consultationHr);
            
            if (!validation.isValid()) {
                System.out.println("Validation Errors:");
                validation.printErrors();
                System.out.println("\nPress Enter to return to main menu...");
                scanner.nextLine();
                return;
            }
            
            // Save updates to consultation record
            try {
                boolean success = controller.updateConsultationRecord(consultationId, symptoms, diagnosis, prescription, notes, consultationHr);
                if (success) {
                    System.out.println("\nConsultation record updated successfully!");
                    System.out.println("Status changed to: COMPLETED");
                } else {
                    System.out.println("Error: Failed to update consultation record.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error updating consultation record: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewConsultationHistory() {
        System.out.println("\n +------------------------- View Patient Consultation History -------------------------+ ");
        System.out.println();
        
        try {
            String patientId = getValidPatientIdForHistory();
            
            System.out.println("\nRetrieving consultation history...");
            ArrayList<Consultation> consultations = controller.getConsultationsByPatient(patientId);
            
            if (consultations.isEmpty()) {
                System.out.println("No consultation history found for this patient.");
                System.out.println("\nPress Enter to return to main menu...");
                scanner.nextLine();
                return;
            }
            
            // Get patient information and current status
            PatientController patientController = PatientController.getInstance();
            Entity.Patient patient = patientController.findPatientByID(patientId);
            
            System.out.println("\n=== Consultation History for Patient " + patientId + " ===");
            if (patient != null) {
                System.out.println("Patient Name: " + patient.getName());
                System.out.println("Faculty: " + patient.getFaculty());
                System.out.println("Registration Date: " + patient.getRegistrationDate().format(dateFormatter));
            }
            
            // Show current queue status
            if (patientController.isPatientInQueue(patientId)) {
                int position = patientController.getPatientQueuePosition(patientId);
                if (position == 1) {
                    System.out.println("Current Status: 1st in queue (NEXT to be seen)");
                } else {
                    System.out.println("Current Status: #" + position + " in queue");
                }
            } else {
                System.out.println("Current Status: Not in queue (may have completed consultation)");
            }
            
            System.out.println("Total consultations: " + consultations.size());
            System.out.println();
            
            // Group consultations by status
            ArrayList<Consultation> completedConsultations = new ArrayList<>();
            ArrayList<Consultation> scheduledConsultations = new ArrayList<>();
            ArrayList<Consultation> cancelledConsultations = new ArrayList<>();
            
            for (int i = 0; i < consultations.size(); i++) {
                Consultation c = consultations.get(i);
                switch (c.getStatus().toUpperCase()) {
                    case "COMPLETED":
                        completedConsultations.add(c);
                        break;
                    case "SCHEDULED":
                        scheduledConsultations.add(c);
                        break;
                    case "CANCELLED":
                        cancelledConsultations.add(c);
                        break;
                    default:
                        scheduledConsultations.add(c);
                        break;
                }
            }
            
            // Display completed consultations first (most recent first)
            if (!completedConsultations.isEmpty()) {
                System.out.println("=== COMPLETED CONSULTATIONS ===");
                for (int i = completedConsultations.size() - 1; i >= 0; i--) {
                    Consultation c = completedConsultations.get(i);
                    displayConsultationDetails(c, "Completed", i + 1);
                }
                System.out.println();
            }
            
            // Display scheduled consultations
            if (!scheduledConsultations.isEmpty()) {
                System.out.println("=== SCHEDULED CONSULTATIONS ===");
                for (int i = 0; i < scheduledConsultations.size(); i++) {
                    Consultation c = scheduledConsultations.get(i);
                    displayConsultationDetails(c, "Scheduled", i + 1);
                }
                System.out.println();
            }
            
            // Display cancelled consultations
            if (!cancelledConsultations.isEmpty()) {
                System.out.println("=== CANCELLED CONSULTATIONS ===");
                for (int i = 0; i < cancelledConsultations.size(); i++) {
                    Consultation c = cancelledConsultations.get(i);
                    displayConsultationDetails(c, "Cancelled", i + 1);
                }
                System.out.println();
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println("\nPress Enter to return to main menu...");
        scanner.nextLine();
    }
    
    private void displayConsultationDetails(Consultation c, String status, int index) {
        System.out.println(status + " Consultation " + index + ":");
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
        if (c.getConsultationHr() > 0) {
            System.out.println("  Duration: " + c.getConsultationHr() + " hours");
        }
        if (c.getFollowUpConsultationId() != null) {
            System.out.println("  Follow-up: " + c.getFollowUpConsultationId());
        }
        System.out.println();
    }
    
    private boolean seededReports = false;
    
    private void viewReports() {
        System.out.println("\n +------------------------- View Reports -------------------------+ ");
        System.out.println();
        while (true) {
            if (!seededReports) {
                createSampleDataForReports();
                seededReports = true;
            }
            System.out.println("\n +------------------------- Consultation Reports Menu -------------------------+ ");
            System.out.printf(" |%20s%-25s%20s|\n", "", "1. Doctor-Patient Relationship Report", "");
            System.out.printf(" |%20s%-34s%23s|\n", "", "2. Consultation Hour Report", "");
            System.out.printf(" |%20s%-34s%23s|\n", "", "3. Exit", "");
            System.out.println(" +----------------------------------------------------------------------------+ ");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1: {
                    showDoctorPatientBarChart();
                    break;
                }
                case 2: {
                    showDoctorHoursBarChart();
                    break;
                }
                case 3:
                    System.out.println("\nPress Enter to return to main menu...");
                    scanner.nextLine();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showDoctorPatientBarChart() {
        Graph<String> graph = buildDoctorPatientGraphForCurrentMonth();
        // Collect doctor vertices
        ArrayList<String> doctorIds = new ArrayList<>();
        ListInterface<String> vertices = graph.getAllVertices();
        for (int i = 0; i < vertices.size(); i++) {
            String v = vertices.get(i);
            if (v != null && v.startsWith("D") && !containsString(doctorIds, v)) {
                doctorIds.add(v);
            }
        }
        if (doctorIds.isEmpty()) {
            System.out.println("No patients found for this month.");
            System.out.println("\nPress Enter to return to main menu...");
            scanner.nextLine();
            return;
        }
        // Determine max for scaling Y-axis
        int maxCount = 1;
        HashMap<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < doctorIds.size(); i++) {
            String id = doctorIds.get(i);
            ListInterface<String> neighbors = graph.neighborsOf(id);
            int cnt = neighbors.size();
            counts.put(id, cnt);
            if (cnt > maxCount) maxCount = cnt;
        }

        System.out.println("\nDoctor-Patient Count (This Month)");
        for (int i = 0; i < doctorIds.size(); i++) {
            String color = COLORS[i % COLORS.length];
            System.out.printf(" %s█%s %s\n", color, RESET, doctorIds.get(i));
        }
        System.out.println();
        System.out.println("Patients");
        // Y-axis
        for (int level = maxCount; level >= 1; level--) {
            System.out.printf(" %3d |", level);
            for (int i = 0; i < doctorIds.size(); i++) {
                String id = doctorIds.get(i);
                Integer boxed = counts.get(id);
                int val = boxed == null ? 0 : boxed.intValue();
                if (val >= level) {
                    String color = COLORS[i % COLORS.length];
                    System.out.print(color + "  ██  " + RESET);
                } else {
                    System.out.print("      ");
                }
            }
            System.out.println();
        }
        // X-axis
        System.out.print("     |__________________________________ Doctors\n");
        System.out.print("      ");
        for (int i = 0; i < doctorIds.size(); i++) {
            System.out.print("   " + (i + 1));
        }
        System.out.println();
        System.out.println();
        for (int i = 0; i < doctorIds.size(); i++) {
            String id = doctorIds.get(i);
            Integer boxed = counts.get(id);
            int val = boxed == null ? 0 : boxed.intValue();
            System.out.printf(" %s: %d\n", id, val);
        }
    }

    private Graph<String> buildDoctorPatientGraphForCurrentMonth() {
        Graph<String> graph = new Graph<>();
        ArrayList<Consultation> all = controller.getAllConsultations();
        if (all.isEmpty()) return graph;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusDays(30); // last 30 days

        for (int i = 0; i < all.size(); i++) {
            Consultation c = all.get(i);
            LocalDateTime appt = c.getAppointmentDateTime();

            // Include consultations within last 30 days
            if (!appt.isBefore(start) && !appt.isAfter(now)) {
                String doctorId = c.getDoctorId();
                String patientId = c.getPatientId();

                if (!graph.hasVertex(doctorId)) {
                    graph.addVertex(doctorId);
                }
                if (!graph.hasVertex(patientId)) {
                    graph.addVertex(patientId);
                }
                if (!graph.hasEdge(doctorId, patientId)) {
                    graph.addEdge(doctorId, patientId);
                }
            }
        }
        return graph;
    }

    private void showDoctorHoursBarChart() {
        ArrayList<Consultation> all = controller.getAllConsultations();
        if (all.isEmpty()) {
            System.out.println("No consultations found.");
            System.out.println("\nPress Enter to return to main menu...");
            scanner.nextLine();
            return;
        }
        HashMap<String, Double> hoursByDoctor = new HashMap<>();
        ArrayList<String> doctorIds = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            Consultation c = all.get(i);
            String doctorId = c.getDoctorId();
            Double current = hoursByDoctor.get(doctorId);
            if (current == null) current = 0.0;
            hoursByDoctor.put(doctorId, current + c.getConsultationHr());
            if (!containsString(doctorIds, doctorId)) doctorIds.add(doctorId);
        }
        if (doctorIds.isEmpty()) {
            System.out.println("No doctor data.");
            System.out.println("\nPress Enter to return to main menu...");
            scanner.nextLine();
            return;
        }
        double max = 1.0;
        for (int i = 0; i < doctorIds.size(); i++) {
            Double vBox = hoursByDoctor.get(doctorIds.get(i));
            double v = vBox == null ? 0.0 : vBox.doubleValue();
            if (v > max) max = v;
        }
        
        System.out.println("\nConsultation Hours by Doctor");
        for (int i = 0; i < doctorIds.size(); i++) {
            String color = COLORS[i % COLORS.length];
            System.out.printf(" %s█%s %s\n", color, RESET, doctorIds.get(i));
        }
        System.out.println();
        System.out.println(" Consultation Hours");
        int maxLevels = (int)Math.max(1, Math.ceil(max / 0.25));
        for (int level = maxLevels; level >= 1; level--) {
            double tick = level * 0.25;
            System.out.printf(" %4.2f |", tick);
            for (int i = 0; i < doctorIds.size(); i++) {
                String id = doctorIds.get(i);
                Double vBox2 = hoursByDoctor.get(id);
                double val = vBox2 == null ? 0.0 : vBox2.doubleValue();
                if (val + 1e-9 >= tick) {
                    String color = COLORS[i % COLORS.length];
                    System.out.print(color + " ██  " + RESET);
                } else {
                    System.out.print("      ");
                }
            }
            System.out.println();
        }
        System.out.print("      |__________________________________ Doctors\n");
        System.out.print("      ");
        for (int i = 0; i < doctorIds.size(); i++) {
            System.out.print("   " + (i + 1));
        }
        System.out.println();
        System.out.println();
        for (int i = 0; i < doctorIds.size(); i++) {
            String id = doctorIds.get(i);
            Double total = hoursByDoctor.get(id);
            if (total == null) total = 0.0;
            System.out.printf(" %s: %.2f h\n", id, total);
        }
    }

    private boolean containsString(ArrayList<String> list, String value) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(value)) return true;
        }
        return false;
    }
    
    private void createSampleDataForReports() {
        System.out.println("Creating sample consultation data...");
        
        LocalDateTime now = LocalDateTime.now();
        
        Consultation c1 = controller.addConsultation("P0001", "D001", 
            now.minusDays(5).withHour(9).withMinute(0),
            "SCHEDULED", null, null, null, null, 0.0);
        Consultation c2 = controller.addConsultation("P0002", "D001", 
            now.minusDays(3).withHour(10).withMinute(0),
            "SCHEDULED", null, null, null, null, 0.0);
        Consultation c3 = controller.addConsultation("P0003", "D001", 
            now.minusDays(1).withHour(11).withMinute(0),
            "SCHEDULED", null, null, null, null, 0.0);
        
        Consultation c4 = controller.addConsultation("P0001", "D002", 
            now.minusDays(2).withHour(14).withMinute(0),
            "SCHEDULED", null, null, null, null, 0.0);
        Consultation c5 = controller.addConsultation("P0004", "D002", 
            now.minusDays(1).withHour(15).withMinute(0),
            "SCHEDULED", null, null, null, null, 0.0);
        
        Consultation c6 = controller.addConsultation("P0002", "D003", 
            now.minusDays(1).withHour(16).withMinute(0),
            "SCHEDULED", null, null, null, null, 0.0);
        Consultation c7 = controller.addConsultation("P0005", "D003", 
            now.withHour(9).withMinute(0),
            "SCHEDULED", null, null, null, null, 0.0);
        
        controller.updateConsultationRecord(c1.getConsultationId(), "Fever, cough", "Common cold", "Paracetamol (M0001)", "Rest and fluids", 0.5);
        controller.updateConsultationRecord(c2.getConsultationId(), "Sore throat", "Tonsillitis", "Antibiotics (M0002)", "Gargle salt water", 1.0);
        controller.updateConsultationRecord(c3.getConsultationId(), "Back pain", "Muscle strain", "Ibuprofen (M0003)", "Stretching advised", 0.75);
        controller.updateConsultationRecord(c4.getConsultationId(), "Cough", "Throat infection", "Cough syrup (M0005)", "Avoid cold drinks", 0.5);
        controller.updateConsultationRecord(c5.getConsultationId(), "Allergy", "Allergic rhinitis", "Antihistamine (M0006)", "Keep windows closed", 0.25);
        controller.updateConsultationRecord(c6.getConsultationId(), "Fatigue", "Vitamin deficiency", "Vitamin C (M0004)", "Eat more fruits", 0.5);

        c1.setFollowUpConsultationId(c4.getConsultationId()); // P0001: D001 → D002
        c2.setFollowUpConsultationId(c6.getConsultationId()); // P0002: D001 → D003
        
    }
}
