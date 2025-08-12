package Boundaries;

import Controller.DoctorController;
import Controller.FollowUpController;
import Entity.Doctor;
import Entity.FollowUpTask;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UIDoctorManagement {
    private final Scanner scanner = new Scanner(System.in);
    private final DoctorController controller = new DoctorController();

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
            System.out.println("5. Back");
            System.out.println("=================================\n");
            System.out.print("Select(1-5): ");
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
        List<FollowUpTask> list = follow.listAll();
        if (list.isEmpty()) {
            System.out.println("(empty)");
            return;
        }
        for (FollowUpTask t : list) System.out.println(t);
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
        Map<LocalDate, List<LocalTime>> schedule = controller.getSchedule(id);
        if (schedule.isEmpty()) {
            System.out.println("No schedule.");
            return;
        }
        for (Map.Entry<LocalDate, List<LocalTime>> e : schedule.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
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
        List<LocalTime> times = new ArrayList<>();
        for (String p : parts) {
            if (!p.trim().isEmpty()) times.add(LocalTime.parse(p.trim()));
        }
        int removed = controller.removeTimeSlots(id, date, times);
        System.out.println("Removed " + removed + " slots.");
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


