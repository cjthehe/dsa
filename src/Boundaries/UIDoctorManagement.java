package Boundaries;

import Controller.DoctorController;
import Controller.FollowUpController;
import Entity.Doctor;
import Entity.FollowUpTask;
import ADT.LinkedList;
import ADT.HashMap;
import ADT.KVConsumer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class UIDoctorManagement {
    private final Scanner scanner = new Scanner(System.in);
    private final DoctorController controller = new DoctorController();

    public UIDoctorManagement() {
        initializeDummyData();
    }

    private void initializeDummyData() {
        
        controller.addDoctor("Dr. CJT", "Cardiology", 10, 'F', "012-3456789", "cjt@hospital.com", LocalDate.of(2015, 3, 15));
        controller.addDoctor("Dr. QN", "Neurology", 9, 'F', "012-3456790", "qn@hospital.com", LocalDate.of(2016, 7, 22));
        controller.addDoctor("Dr. WN", "Pediatrics", 10, 'F', "012-3456791", "wn@hospital.com", LocalDate.of(2015, 1, 10));
        controller.addDoctor("Dr. JW", "Orthopedics", 70, 'M', "012-3456792", "jw@hospital.com", LocalDate.of(1955, 7, 18));
        controller.addDoctor("Dr. CGZ", "Dermatology", 11, 'M', "012-3456793", "cgz@hospital.com", LocalDate.of(2014, 5, 30));
        
        // Initialize time slots for each doctor with different schedules
        initializeTimeSlots();
        
    }

    private void initializeTimeSlots() {
        // Dr. CJT - Monday 9-5, Tuesday 11-3
        controller.defineAvailableSlots("D001", LocalDate.now().plusDays(1), LocalTime.of(13, 0), LocalTime.of(19, 0), 30);
        controller.defineAvailableSlots("D001", LocalDate.now().plusDays(2), LocalTime.of(11, 0), LocalTime.of(16, 0), 30);
        
        // Dr. QN - Wednesday 2-5, Thursday 9-3
        controller.defineAvailableSlots("D002", LocalDate.now().plusDays(3), LocalTime.of(12, 0), LocalTime.of(17, 0), 30);
        controller.defineAvailableSlots("D002", LocalDate.now().plusDays(4), LocalTime.of(9, 0), LocalTime.of(15, 0), 30);
        
        // Dr. WN - Friday 8-12, Saturday 10-2
        controller.defineAvailableSlots("D003", LocalDate.now().plusDays(5), LocalTime.of(8, 0), LocalTime.of(13, 0), 30);
        controller.defineAvailableSlots("D003", LocalDate.now().plusDays(6), LocalTime.of(10, 0), LocalTime.of(14, 0), 30);
        
        // Dr. JW - Monday 10-4, Wednesday 9-1
        controller.defineAvailableSlots("D004", LocalDate.now().plusDays(1), LocalTime.of(2, 0), LocalTime.of(8, 0), 45);
        controller.defineAvailableSlots("D004", LocalDate.now().plusDays(3), LocalTime.of(13, 0), LocalTime.of(18, 0), 45);
        
        // Dr. CGZ - Tuesday 1-6, Friday 9-5
        controller.defineAvailableSlots("D005", LocalDate.now().plusDays(2), LocalTime.of(13, 0), LocalTime.of(18, 0), 30);
        controller.defineAvailableSlots("D005", LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(15, 0), 30);
        
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("|       Doctor Management       |");
            System.out.println("=================================");
            System.out.println("1. Doctor Profile Management");
            System.out.println("2. Doctor Schedule Management");
            System.out.println("3. Follow-up Tracker");
            System.out.println("4. Exit");
            System.out.println("=================================\n");
            System.out.print("Select your option: ");
            int choice = readInt();

            switch (choice) {
                case 1:
                    showProfileMenu();
                    break;
                case 2:
                    showScheduleMenu();
                    break;
                case 3:
                    showFollowUpMenu();
                    break;
                case 4:
                    System.exit(0);
                    break;
                default: System.out.println("Invalid option. Pls try again.");
            }
        }
    }

    // ===== Module 1: Profile Management =====
    private void showProfileMenu() {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("|   Doctor Profile Management   |");
            System.out.println("=================================");
            System.out.println("1. Create Doctor Profile");
            System.out.println("2. View Doctor Profile");
            System.out.println("3. Update Doctor Profile");
            System.out.println("4. Delete Doctor Profile");
            System.out.println("5. List Doctor Profile");
            System.out.println("6. Back");
            System.out.println("=================================\n");
            System.out.print("Select(1-6): ");
            int c = readInt();
            switch (c) {
                case 1: 
                    createProfile(); 
                    break;
                case 2: 
                    viewProfile(); 
                    break;
                case 3: 
                    updateProfile(); 
                    break;
                case 4: 
                    deleteProfile(); 
                    break;
                case 5: 
                    listAllDoctors(); 
                    break;
                case 6: 
                    return;
                default: 
                    System.out.println("Invalid option. Pls try again.");
            }
        }
    }

    // ===== Module 2: Schedule Management =====
    private void showScheduleMenu() {
        while (true) {
            System.out.println("\n==================================");
            System.out.println("|   Doctor Schedule Management   |");
            System.out.println("==================================");
            System.out.println("1. Define Available Slots");
            System.out.println("2. View Schedule");
            System.out.println("3. Add Time Slot");
            System.out.println("4. Remove Time Slot(s)");
            System.out.println("5. Update Working Hours");
            System.out.println("6. Back");
            System.out.println("==================================\n");
            System.out.print("Select(1-6): ");
            int c = readInt();
            switch (c) {
                case 1: 
                    defineSlots(); 
                    break;
                case 2: 
                    viewSchedule(); 
                    break;
                case 3: 
                    addSlot(); 
                    break;
                case 4: 
                    removeSlots(); 
                    break;
                case 5: 
                    updateWorkingHours(); 
                    break;
                case 6: 
                    return;
                default: 
                    System.out.println("Invalid option. Pls try again.");
            }
        }
    }

    // ===== Module 3: Follow-up Tracker =====
    private final FollowUpController follow = new FollowUpController();

    private void showFollowUpMenu() {
        while (true) {
            System.out.println("\n===============================");
            System.out.println("|       Follow-up Tracker       |");
            System.out.println("=================================");
            System.out.println("1. Add Patient");
            System.out.println("2. View List");
            System.out.println("3. Mark as Completed");
            System.out.println("4. Delete");
            System.out.println("5. Back");
            System.out.println("=================================\n");
            System.out.print("Select(1-5): ");
            int c = readInt();
            switch (c) {
                case 1: 
                    fuAdd(); 
                    break;
                case 2: 
                    fuList(); 
                    break;
                case 3: 
                    fuMarkCompleted(); 
                    break;
                case 4: 
                    fuDelete(); 
                    break;
                case 5: 
                    return;
                default: 
                    System.out.println("Invalid option. Pls try again.");
            }
        }
    }

    private void fuAdd() {
        String pid = ask("Patient ID: ");
        String did = ask("Doctor ID: ");
        String note = ask("Note (e.g., Stay Hospital): ");
        FollowUpTask t = follow.add(pid, did, note);
        System.out.println("Added: " + t);
    }

    private void fuList() {
        // Use ADT methods to display the follow-up list
        System.out.println("Follow-up tasks:");
        
        // Get the list from follow-up controller and display using ADT methods
        try {
            LinkedList<FollowUpTask> tasks = follow.listAll();
            if (tasks.isEmpty()) {
                System.out.println("(No follow-up tasks found)");
                return;
            }
            
            System.out.println("Total tasks: " + tasks.size());
            for (int i = 0; i < tasks.size(); i++) {
                FollowUpTask task = tasks.get(i);
                System.out.println((i+1) + ". " + task);
            }
        } catch (Exception e) {
            System.out.println("Error displaying follow-up tasks: " + e.getMessage());
        }
    }

    private void fuMarkCompleted() {
        String id = ask("Task ID: ");
        boolean ok = follow.markCompleted(id);
        System.out.println(ok ? "Marked." : "Not found.");
    }

    private void fuDelete() {
        String id = ask("Task ID: ");
        boolean ok = follow.delete(id);
        System.out.println(ok ? "Deleted." : "Not found.");
    }

    private void createProfile() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Specialization: ");
        String spec = scanner.nextLine();
        System.out.print("Years of Experience: ");
        int exp = readInt();
        System.out.print("Gender (M/F): ");
        String g = scanner.nextLine().trim();
        char gender = g.isEmpty() ? 'U' : g.charAt(0);
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        LocalDate hired = LocalDate.now();
        Doctor d = controller.addDoctor(name, spec, exp, gender, phone, email, hired);
        System.out.println("\nCreated: " + d);
    }

    private void viewProfile() {
        System.out.print("Doctor ID: ");
        String id = scanner.nextLine();
        Doctor d = controller.getDoctorById(id);
        System.out.println(d == null ? "Not found" : d.toString());
    }

    private void updateProfile() {
        System.out.print("Doctor ID: ");
        String id = scanner.nextLine();
        System.out.print("Field (name/specialization/experience/gender/phone/email/hiredDate/active): ");
        String field = scanner.nextLine();
        System.out.print("New value: ");
        String val = scanner.nextLine();
        boolean ok = controller.updateDoctorField(id, field, val);
        System.out.println(ok ? "Updated" : "Failed");
    }

    private void deleteProfile() {
        System.out.print("Doctor ID: ");
        String id = scanner.nextLine();
        boolean ok = controller.deleteDoctor(id);
        System.out.println(ok ? "Deleted" : "Not found");
    }

    private void defineSlots() {
        String id = ask("Doctor ID: ");
        LocalDate date = LocalDate.parse(ask("Date (yyyy-MM-dd): "));
        LocalTime start = LocalTime.parse(ask("Start (HH:mm): "));
        LocalTime end = LocalTime.parse(ask("End (HH:mm): "));
        int interval = Integer.parseInt(ask("Interval minutes: "));
        controller.defineAvailableSlots(id, date, start, end, interval);
        System.out.println("Slots defined.");
    }

    private void viewSchedule() {
        String id = ask("Doctor ID: ");
        System.out.println("Schedule for Doctor " + id + ":");
        
        try {
            // Get schedule from controller and display using ADT HashMap methods
            HashMap<LocalDate, LinkedList<LocalTime>> schedule = controller.getSchedule(id);
            if (schedule.size() == 0) {
                System.out.println("No schedule found for doctor " + id);
                return;
            }
            
            System.out.println("Schedule retrieved and displayed using ADT methods:");
            schedule.forEach(new KVConsumer<LocalDate, LinkedList<LocalTime>>() {
                @Override
                public void accept(LocalDate date, LinkedList<LocalTime> times) {
                    System.out.println("Date: " + date);
                    System.out.print("Times: ");
                    for (int i = 0; i < times.size(); i++) {
                        System.out.print(times.get(i));
                        if (i < times.size() - 1) {
                            System.out.print(", ");
                        }
                    }
                    System.out.println();
                    System.out.println("---");
                }
            });
        } catch (Exception e) {
            System.out.println("Error displaying schedule: " + e.getMessage());
        }
    }

    private void addSlot() {
        String id = ask("Doctor ID: ");
        LocalDate date = LocalDate.parse(ask("Date (yyyy-MM-dd): "));
        LocalTime t = LocalTime.parse(ask("Time (HH:mm): "));
        boolean ok = controller.addTimeSlot(id, date, t);
        System.out.println(ok ? "Added" : "Already exists");
    }

    private void removeSlots() {
        String id = ask("Doctor ID: ");
        LocalDate date = LocalDate.parse(ask("Date (yyyy-MM-dd): "));
        System.out.println("Enter times to remove separated by comma (e.g., 09:00,09:30): ");
        String[] parts = scanner.nextLine().split(",");
        
        // Use ADT LinkedList to store the times
        LinkedList<LocalTime> times = new LinkedList<>();
        for (String p : parts) {
            if (!p.trim().isEmpty()) {
                try {
                    LocalTime time = LocalTime.parse(p.trim());
                    times.add(time);
                } catch (Exception e) {
                    System.out.println("Invalid time format: " + p.trim());
                }
            }
        }
        
        // Display the times stored in ADT LinkedList
        System.out.println("Time slots to remove: " + times.size());
        if (times.isEmpty()) {
            System.out.println("No valid time slots to remove.");
            return;
        }
        
        // Iterate through ADT LinkedList using index-based access
        for (int i = 0; i < times.size(); i++) {
            LocalTime time = times.get(i);
            System.out.println("Time " + (i+1) + ": " + time);
        }
        
        // Now use the ADT LinkedList with the controller
        int removed = controller.removeTimeSlots(id, date, times);
        System.out.println("Successfully removed " + removed + " time slots from doctor " + id + " on " + date);
    }

    private void updateWorkingHours() {
        String id = ask("Doctor ID: ");
        LocalDate date = LocalDate.parse(ask("Date (yyyy-MM-dd): "));
        LocalTime start = LocalTime.parse(ask("New Start (HH:mm): "));
        LocalTime end = LocalTime.parse(ask("New End (HH:mm): "));
        int interval = Integer.parseInt(ask("Interval minutes: "));
        controller.updateWorkingHours(id, date, start, end, interval);
        System.out.println("Working hours updated.");
    }

    private void listAllDoctors() {
        System.out.println("\n==============================================================================================");
        System.out.println("|                                  All Registered Doctors                                    |");
        System.out.println("==============================================================================================");
        try {
            // Get all doctors from the controller to show current state
            LinkedList<Doctor> allDoctors = controller.getAllDoctors();
            
            if (allDoctors.isEmpty()) {
                System.out.println("No doctors found in the system.");
                System.out.println("==============================================================================================\n");
                return;
            }
            
            System.out.println("Doctor ID |   Name   | Specialization | Experience | Gender |    Phone    |       Email");
            
            // Display all doctors from the controller
            for (int i = 0; i < allDoctors.size(); i++) {
                Doctor doctor = allDoctors.get(i);
                System.out.printf("%-9s | %-8s | %-14s | %-10d | %-6c | %-11s | %-25s%n",
                    doctor.getDoctorId(),
                    doctor.getName(),
                    doctor.getSpecialization(),
                    doctor.getYearsOfExperience(),
                    doctor.getGender(),
                    doctor.getPhoneNumber(),
                    doctor.getEmail());
            }
            
            System.out.println("==============================================================================================\n");
            
        } catch (Exception e) {
            System.out.println("Error displaying doctors: " + e.getMessage());
        }
    }

    private int readInt() {
        try { 
            return Integer.parseInt(scanner.nextLine()); 
        } catch (Exception e) { 
            return -1; 
        }
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

}


