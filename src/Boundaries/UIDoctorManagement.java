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
import java.time.format.DateTimeParseException;

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
        // Initialize follow-up sample tasks
        initializeFollowUpDummyData();
        
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

    private void initializeFollowUpDummyData() {
        follow.add("P101", "D001", "Check X-ray result", LocalDate.now().plusDays(7));
        follow.add("P102", "D001", "Blood test review", LocalDate.now().plusDays(14));
        follow.add("P103", "D001", "Confirm medication", LocalDate.now().plusDays(2));
        FollowUpTask t4 = follow.add("P104", "D002", "Physio follow-up", LocalDate.now().plusDays(3));
        follow.add("P105", "D003", "Post-op wound check", LocalDate.now().plusDays(10));
        // Mark one as completed
        follow.markCompleted(t4.getTaskId());
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("|       Doctor Management       |");
            System.out.println("=================================");
            System.out.println("1. Doctor Profile Management");
            System.out.println("2. Doctor Schedule Management");
            System.out.println("3. Follow-up Task");
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
            System.out.println("2. Add Time Slot");
            System.out.println("3. Remove Time Slot(s)");
            System.out.println("4. View Schedule");
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
                    addSlot(); 
                    break;
                case 3: 
                    removeSlots(); 
                    break;
                case 4: 
                    viewSchedule(); 
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
            System.out.println("\n=========================================");
            System.out.println("|            Follow-up Task             |");
            System.out.println("=========================================");
            System.out.println("1. Add follow-up task");
            System.out.println("2. View tasks (All/Pending/Completed)");
            System.out.println("3. Update task (description/due date)");
            System.out.println("4. Mark as Completed");
            System.out.println("5. Delete task");
            System.out.println("6. Back");
            System.out.println("=========================================\n");
            System.out.print("Select(1-6): ");
            int c = readInt();
            switch (c) {
                case 1: 
                    fuAdd(); 
                    break;
                case 2: 
                    fuView(); 
                    break;
                case 3: 
                    fuUpdate();
                    break;
                case 4: 
                    fuMarkCompleted(); 
                    break;
                case 5: 
                    fuDelete(); 
                    break;
                case 6: 
                    return;
                default: 
                    System.out.println("Invalid option. Pls try again.");
            }
        }
    }

    private void createProfile() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Specialization: ");
        String spec = scanner.nextLine();
        int exp;
        while (true) {
            System.out.print("Years of Experience: ");
            String expStr = scanner.nextLine().trim();
            try {
                exp = Integer.parseInt(expStr);
                break;
            } catch (Exception e) {
                System.out.println("Invalid experience. Please enter a number.");
            }
        }
        char gender;
        while (true) {
            System.out.print("Gender (M/F): ");
            String g = scanner.nextLine().trim();
            if (isValidGenderInput(g)) {
                gender = g.charAt(0);
                break;
            }
            System.out.println("Invalid gender. Enter M or F.");
        }
        String phone;
        while (true) {
            System.out.print("Phone: ");
            phone = scanner.nextLine().trim();
            if (isValidPhoneNumber(phone)) break;
            System.out.println("Invalid phone. Use 01x-xxxxxxx or 01x-xxxxxxxx.");
        }
        String email;
        while (true) {
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
            if (isValidGmail(email)) break;
            System.out.println("Invalid email. Use format xxx@gmail.com.");
        }
        LocalDate hired = LocalDate.now();
        Doctor d = controller.addDoctor(name, spec, exp, gender, phone, email, hired);
        System.out.println("\nCreated:");
        printDoctorTableHeader();
        printDoctorRow(d);
    }

    private void viewProfile() {
        System.out.print("Doctor ID: ");
        String id = scanner.nextLine();
        Doctor d = controller.getDoctorById(id);
        if (d == null) {
            System.out.println("Not found");
        } else {
            printDoctorTableHeader();
            printDoctorRow(d);
        }
    }

    private void updateProfile() {
        System.out.print("Doctor ID: ");
        String id = scanner.nextLine();
        System.out.print("Field (name/specialization/experience/gender/phone/email/hiredDate/active): ");
        String field = scanner.nextLine();
        System.out.print("New value: ");
        String val = scanner.nextLine();
        String f = field == null ? "" : field.toLowerCase();
        switch (f) {
            case "experience":
            case "yearsofexperience":
            case "years_of_experience": {
                try {
                    Integer.parseInt(val.trim());
                } catch (Exception e) {
                    System.out.println("Invalid experience. Please enter a number.\n");
                    return;
                }
                break;
            }
            case "gender": {
                if (!isValidGenderInput(val)) {
                    System.out.println("Invalid gender. Enter M or F.\n");
                    return;
                }
                break;
            }
            case "phone":
            case "phonenumber": {
                if (!isValidPhoneNumber(val)) {
                    System.out.println("Invalid phone. Use 01x-xxxxxxx or 01x-xxxxxxxx.\n");
                    return;
                }
                break;
            }
            case "email": {
                if (!isValidGmail(val)) {
                    System.out.println("Invalid email. Use format xxx@gmail.com.\n");
                    return;
                }
                break;
            }
            default:
                break;
        }
        boolean ok = controller.updateDoctorField(id, field, val);
        if (!ok) {
            System.out.println("Failed...\n");
        } else {
            System.out.println("Updated.\n");
            Doctor d = controller.getDoctorById(id);
            if (d != null) {
                printDoctorTableHeader();
                printDoctorRow(d);
            }
        }
    }

    private void deleteProfile() {
        System.out.print("Doctor ID: ");
        String id = scanner.nextLine();
        boolean ok = controller.deleteDoctor(id);
        System.out.println(ok ? "Deleted" : "Not found");
    }

    private void listAllDoctors() {
        System.out.println("\nAll Registered Doctors:");
        System.out.println("_______________________________________________________________________________________________\n");
        try {
            // Get all doctors from the controller to show current state
            LinkedList<Doctor> allDoctors = controller.getAllDoctors();
            
            if (allDoctors.isEmpty()) {
                System.out.println("No doctors found in the system.");
                System.out.println("_______________________________________________________________________________________________\n");
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
            
            System.out.println("_______________________________________________________________________________________________\n");
            
        } catch (Exception e) {
            System.out.println("Error displaying doctors: " + e.getMessage());
        }
    }

    private void defineSlots() {
        String id = ask("Doctor ID: ");
        
        // Check if doctor exists
        Doctor doctor = controller.getDoctorById(id);
        if (doctor == null) {
            System.out.println("Doctor with ID " + id + " not found!");
            return;
        }
        
        System.out.println("\nName: " + doctor.getName());
        System.out.println("Specialization: " + doctor.getSpecialization());
        
        // Display current time slots for the doctor
        System.out.println("\n--------------- Current Time Slots -----------------\n");
        HashMap<LocalDate, LinkedList<LocalTime>> currentSchedule = controller.getSchedule(id);
        if (currentSchedule.size() == 0) {
            System.out.println("No time slots found for this doctor.");
        } else {
            currentSchedule.forEach(new KVConsumer<LocalDate, LinkedList<LocalTime>>() {
                @Override
                public void accept(LocalDate date, LinkedList<LocalTime> times) {
                    System.out.println("Date: " + date);
                    if (times.size() > 0) {
                        LocalTime firstSlot = times.get(0);
                        LocalTime lastSlot = times.get(times.size() - 1);
                        System.out.println("Time slot: " + firstSlot + " to " + lastSlot);
                        System.out.print("Available slots: ");
                        for (int i = 0; i < times.size(); i++) {
                            System.out.print(times.get(i));
                            if (i < times.size() - 1) {
                                System.out.print(", ");
                            }
                        }
                        System.out.println();
                    }
                    System.out.println("...");
                }
            });
        }
        System.out.println("\n----------------------------------------------------\n");
    }

    private void addSlot() {
        String id = ask("Doctor ID: ");
        
        // Check if doctor exists
        Doctor doctor = controller.getDoctorById(id);
        if (doctor == null) {
            System.out.println("Doctor with ID " + id + " not found!");
            return;
        }
        
        System.out.println("\nName: " + doctor.getName());
        System.out.println("Specialization: " + doctor.getSpecialization());
        
        // Display current time slots for the doctor
        System.out.println("\n--------------- Current Time Slots -----------------\n");
        HashMap<LocalDate, LinkedList<LocalTime>> currentSchedule = controller.getSchedule(id);
        if (currentSchedule.size() == 0) {
            System.out.println("No time slots found for this doctor.");
        } else {
            currentSchedule.forEach(new KVConsumer<LocalDate, LinkedList<LocalTime>>() {
                @Override
                public void accept(LocalDate date, LinkedList<LocalTime> times) {
                    System.out.println("Date: " + date);
                    if (times.size() > 0) {
                        LocalTime firstSlot = times.get(0);
                        LocalTime lastSlot = times.get(times.size() - 1);
                        System.out.println("Time slot: " + firstSlot + " to " + lastSlot);
                        System.out.print("Available slots: ");
                        for (int i = 0; i < times.size(); i++) {
                            System.out.print(times.get(i));
                            if (i < times.size() - 1) {
                                System.out.print(", ");
                            }
                        }
                        System.out.println();
                    }
                    System.out.println("...");
                }
            });
        }
        System.out.println("\n----------------------------------------------------\n");
        
        // Ask for new slot details
        LocalDate date;
        while (true) {
            String dateStr = ask("Date (yyyy-MM-dd): ");
            if (isValidDateFormat(dateStr)) {
                try {
                    date = LocalDate.parse(dateStr);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format. Use yyyy-MM-dd.");
                }
            } else {
                System.out.println("Invalid date format. Use yyyy-MM-dd.");
            }
        }
        LocalTime newTime;
        while (true) {
            String timeStr = ask("Time to add (HH:mm): ");
            if (isValidTimeFormat(timeStr)) {
                try {
                    newTime = LocalTime.parse(timeStr);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid time format. Use HH:mm.");
                }
            } else {
                System.out.println("Invalid time format. Use HH:mm (00:00 to 23:59).");
            }
        }
        
        boolean added = controller.addTimeSlot(id, date, newTime);
        if (added) {
            System.out.println("Added: " + newTime + " to " + date);
        } else {
            System.out.println("Time slot " + newTime + " already exists for " + date);
        }
    }

    private void removeSlots() {
        String id = ask("Doctor ID: ");
        
        // Check if doctor exists
        Doctor doctor = controller.getDoctorById(id);
        if (doctor == null) {
            System.out.println("Doctor with ID " + id + " not found!");
            return;
        }
        
        System.out.println("\nName: " + doctor.getName());
        System.out.println("Specialization: " + doctor.getSpecialization());
        
        // Display current time slots for the doctor
        System.out.println("\n--------------- Current Time Slots -----------------\n");
        HashMap<LocalDate, LinkedList<LocalTime>> currentSchedule = controller.getSchedule(id);
        if (currentSchedule.size() == 0) {
            System.out.println("No time slots found for this doctor.");
        } else {
            currentSchedule.forEach(new KVConsumer<LocalDate, LinkedList<LocalTime>>() {
                @Override
                public void accept(LocalDate date, LinkedList<LocalTime> times) {
                    System.out.println("Date: " + date);
                    if (times.size() > 0) {
                        LocalTime firstSlot = times.get(0);
                        LocalTime lastSlot = times.get(times.size() - 1);
                        System.out.println("Time slot: " + firstSlot + " to " + lastSlot);
                        System.out.print("Available slots: ");
                        for (int i = 0; i < times.size(); i++) {
                            System.out.print(times.get(i));
                            if (i < times.size() - 1) {
                                System.out.print(", ");
                            }
                        }
                        System.out.println();
                    }
                    System.out.println("...");
                }
            });
        }
        System.out.println("\n----------------------------------------------------\n");
        
        // Ask for removal details
        LocalDate date;
        while (true) {
            String dateStr = ask("Date (yyyy-MM-dd): ");
            if (isValidDateFormat(dateStr)) {
                try {
                    date = LocalDate.parse(dateStr);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format. Use yyyy-MM-dd.");
                }
            } else {
                System.out.println("Invalid date format. Use yyyy-MM-dd.");
            }
        }
        System.out.print("Times to remove (e.g., 09:00,09:30): ");
        String[] parts = scanner.nextLine().split(",");
        
        // Use ADT LinkedList to store the times
        LinkedList<LocalTime> times = new LinkedList<>();
        for (String p : parts) {
            if (!p.trim().isEmpty()) {
                if (isValidTimeFormat(p.trim())) {
                    try {
                        LocalTime time = LocalTime.parse(p.trim());
                        times.add(time);
                    } catch (Exception e) {
                        System.out.println("Invalid time format (Separate by comma): " + p.trim());
                    }
                } else {
                    System.out.println("Invalid time format. Use HH:mm (00:00 to 23:59): " + p.trim());
                }
            }
        }
        
        // Display the times to be removed
        System.out.println("Time slots to remove: " + times.size());
        if (times.isEmpty()) {
            System.out.println("No valid time slots to remove.");
            return;
        }
        
        // Show what will be removed
        for (int i = 0; i < times.size(); i++) {
            LocalTime time = times.get(i);
            System.out.println("Time " + (i+1) + ": " + time);
        }
        
        // Remove the time slots
        int removed = controller.removeTimeSlots(id, date, times);
        System.out.println("Removed: " + removed + " time slots from " + date);
    }

    private void viewSchedule() {
        String id = ask("Doctor ID: ");
        
        // First, check if doctor exists
        Doctor doctor = controller.getDoctorById(id);
        if (doctor == null) {
            System.out.println("Doctor with ID " + id + " not found!");
            return;
        }
        
        System.out.println("\nName: " + doctor.getName());
        System.out.println("Specialization: " + doctor.getSpecialization());
        
        System.out.println("\n----------------- Time Slots -----------------\n");
        
        try {
            // Get schedule from controller and display using ADT HashMap methods
            HashMap<LocalDate, LinkedList<LocalTime>> schedule = controller.getSchedule(id);
            if (schedule.size() == 0) {
                System.out.println("No schedule found for doctor " + id);
                System.out.println("Please use 'Define Available Slots' to create a schedule.");
            } else {
                System.out.println("Total dates with scheduled slots: " + schedule.size());
                System.out.println();
                
                schedule.forEach(new KVConsumer<LocalDate, LinkedList<LocalTime>>() {
                    @Override
                    public void accept(LocalDate date, LinkedList<LocalTime> times) {
                        System.out.println("Date: " + date + " (" + date.getDayOfWeek() + ")");
                        if (times.size() > 0) {
                            LocalTime firstSlot = times.get(0);
                            LocalTime lastSlot = times.get(times.size() - 1);
                            System.out.println("Time slot: " + firstSlot + " to " + lastSlot + "\n");
                        }
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error displaying schedule: " + e.getMessage());
        }
        System.out.println("----------------------------------------------");
    }

    private void updateWorkingHours() {
        String id = ask("Doctor ID: ");
        
        // Check if doctor exists
        Doctor doctor = controller.getDoctorById(id);
        if (doctor == null) {
            System.out.println("Doctor with ID " + id + " not found!");
            return;
        }
        
        System.out.println("\nName: " + doctor.getName());
        System.out.println("Specialization: " + doctor.getSpecialization());
        
        // Display current working hours for the doctor
        System.out.println("\n--------------- Current Working Hours -----------------\n");
        HashMap<LocalDate, LinkedList<LocalTime>> currentSchedule = controller.getSchedule(id);
        if (currentSchedule.size() == 0) {
            System.out.println("No working hours found for this doctor.");
        } else {
            currentSchedule.forEach(new KVConsumer<LocalDate, LinkedList<LocalTime>>() {
                @Override
                public void accept(LocalDate date, LinkedList<LocalTime> times) {
                    System.out.println("Date: " + date);
                    if (times.size() > 0) {
                        LocalTime firstSlot = times.get(0);
                        LocalTime lastSlot = times.get(times.size() - 1);
                        System.out.println("Working hours: " + firstSlot + " to " + lastSlot);
                        System.out.print("Available slots: ");
                        for (int i = 0; i < times.size(); i++) {
                            System.out.print(times.get(i));
                            if (i < times.size() - 1) {
                                System.out.print(", ");
                            }
                        }
                        System.out.println();
                    }
                    System.out.println("...");
                }
            });
        }
        System.out.println("\n----------------------------------------------------\n");
        
        // Ask for new working hours
        LocalDate date;
        while (true) {
            String dateStr = ask("Date (yyyy-MM-dd): ");
            if (isValidDateFormat(dateStr)) {
                try {
                    date = LocalDate.parse(dateStr);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format. Use yyyy-MM-dd.");
                }
            } else {
                System.out.println("Invalid date format. Use yyyy-MM-dd.");
            }
        }
        LocalTime start;
        while (true) {
            String startStr = ask("New Start (HH:mm): ");
            if (isValidTimeFormat(startStr)) {
                try {
                    start = LocalTime.parse(startStr);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid time format. Use HH:mm.");
                }
            } else {
                System.out.println("Invalid time format. Use HH:mm (00:00 to 23:59).");
            }
        }
        LocalTime end;
        while (true) {
            String endStr = ask("New End (HH:mm): ");
            if (isValidTimeFormat(endStr)) {
                try {
                    end = LocalTime.parse(endStr);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid time format. Use HH:mm.");
                }
            } else {
                System.out.println("Invalid time format. Use HH:mm (00:00 to 23:59).");
            }
        }
        int interval = Integer.parseInt(ask("Interval minutes: "));
        
        controller.updateWorkingHours(id, date, start, end, interval);
        System.out.println("Working hours updated.");
    }

    private void fuAdd() {
        String pid = ask("Patient ID: ");
        String did = ask("Doctor ID: ");
        String desc = ask("Description: ");
        String dueStr = ask("Due Date (yyyy-MM-dd) [optional]: ");
        FollowUpTask t;
        if (dueStr == null || dueStr.trim().isEmpty()) {
            t = follow.add(pid, did, desc);
        } else {
            if (!isValidDateFormat(dueStr.trim())) {
                System.out.println("Invalid date format. Use yyyy-MM-dd. Adding without due date.");
                t = follow.add(pid, did, desc);
            } else {
                try {
                    java.time.LocalDate due = java.time.LocalDate.parse(dueStr.trim());
                    t = follow.add(pid, did, desc, due);
                } catch (DateTimeParseException ex) {
                    System.out.println("Invalid date. Adding without due date.");
                    t = follow.add(pid, did, desc);
                }
            }
        }
        System.out.println("Added: " + t);
    }

    private void fuView() {
        System.out.println("View filter: 1)All");
        System.out.println("             2)Pending");
        System.out.println("             3)Completed");
        System.out.print("Select(1-3): ");
        int opt = readInt();
        LinkedList<FollowUpTask> tasks;
        switch (opt) {
            case 2:
                tasks = follow.listByStatus("Pending");
                break;
            case 3:
                tasks = follow.listByStatus("Completed");
                break;
            case 1:
            default:
                tasks = follow.listAll();
        }

        System.out.println("\nFollow-up tasks:");
        try {
            if (tasks.isEmpty()) {
                System.out.println("(No follow-up tasks found)");
                return;
            }
            System.out.println("Total tasks: " + tasks.size());
            System.out.println("");
            for (int i = 0; i < tasks.size(); i++) {
                FollowUpTask task = tasks.get(i);
                System.out.println((i+1) + ". " + task);
            }
        } catch (Exception e) {
            System.out.println("Error displaying follow-up tasks: " + e.getMessage());
        }
    }

    private void fuUpdate() {
        String id = ask("Task ID: ");
        String newDesc = ask("New description (leave blank to keep): ");
        String dueStr = ask("New Due Date (yyyy-MM-dd) [leave blank to keep]: ");
        java.time.LocalDate due = null;
        if (dueStr != null && !dueStr.trim().isEmpty()) {
            if (!isValidDateFormat(dueStr.trim())) {
                System.out.println("Invalid date format. Use yyyy-MM-dd. Skipping due date update.");
            } else {
                try {
                    due = java.time.LocalDate.parse(dueStr.trim());
                } catch (DateTimeParseException ex) {
                    System.out.println("Invalid date. Skipping due date update.");
                }
            }
        }
        boolean ok = follow.update(id, newDesc, due);
        System.out.println(ok ? "Updated." : "Not found.");
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

    private void printDoctorTableHeader() {
        System.out.println("----------------------------------------------------------------------------------------------");
        System.out.println("Doctor ID |   Name   | Specialization | Experience | Gender |    Phone    |       Email");
    }

    private void printDoctorRow(Doctor doctor) {
        System.out.printf("%-9s | %-8s | %-14s | %-10d | %-6c | %-11s | %-25s%n",
                doctor.getDoctorId(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.getYearsOfExperience(),
                doctor.getGender(),
                doctor.getPhoneNumber(),
                doctor.getEmail());

        System.out.println("----------------------------------------------------------------------------------------------");
    }

    private boolean isValidGenderInput(String input) {
        return input != null && input.matches("[MmFf]");
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("01\\d-\\d{7,8}");
    }

    private boolean isValidGmail(String email) {
        return email != null && email.matches("[A-Za-z0-9._%+-]+@gmail\\.com");
    }

    private boolean isValidDateFormat(String date) {
        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isValidTimeFormat(String time) {
        if (time == null || !time.matches("\\d{2}:\\d{2}")) {
            return false;
        }
        try {
            LocalTime.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}


