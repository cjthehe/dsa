package Boundaries;

import Controller.MedicalTreatmentController;
import Controller.DoctorController;
import ADT.LinkedList;
import ADT.HashMap;
import ADT.KVConsumer;
import Entity.MedicalTreatment;
import Entity.Pharmacy;
import Entity.Patient;
import Entity.Doctor;
import ADT.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.Random;

public class UIMedicalTreatment {
    private final Scanner scanner = new Scanner(System.in);
    private final MedicalTreatmentController controller = new MedicalTreatmentController();
    private final ArrayList<Patient> dummyPatients = new ArrayList<>();

    public void showMenu() {
        seedDummyData();
        while (true) {
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("           Medical Treatment         ");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("1. Medical Treatment Management");
            System.out.println("2. Manage note for Treatment");
            System.out.println("3. View treatment details");
            System.out.println("4. Mark Status as Inactive");
            System.out.println("5. Patient view own treatment details");
            System.out.println("6. Medicine usage report");
            System.out.println("7. Monthly treatment summary");
            System.out.println("8. Back");
            System.out.print("Select your option: ");
            int choice = readInt();
            System.out.println();

            switch (choice) {
                case 1:
                    managementMenu();
                    break;
                case 2:
                    notesMenu();
                    break;
                case 3:
                    viewAllDetails();
                    break;
                case 4:
                    markComplete();
                    break;
                case 5:
                    viewByPatient();
                    break;
                case 6:
                    showMedicineUsageReport();
                    break;
                case 7:
                    showMonthlyTreatmentReport();
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid option. Please enter 1-8.");
            }
        }
    }

    private void managementMenu() {
        while (true) {
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("       Medical Treatment Management        ");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("1. Add Medical Treatment");
            System.out.println("2. Update Medical Treatment Details");
            System.out.println("3. Delete Medical Treatment");
            System.out.println("4. Back");
            System.out.print("Choose: ");
            int choice = readInt();

            switch (choice) {
                case 1:
                    addTreatment();
                    break;
                case 2:
                    updateTreatment();
                    break;
                case 3:
                    deleteTreatment();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Please enter 1-4.");
            }
        }
    }

    private void notesMenu() {
        while (true) {
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("      Medical Treatment Note Management         ");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("1. Add note");
            System.out.println("2. Delete note");
            System.out.println("3. Back");
            System.out.print("Choose: ");
            int choice = readInt();
            if (choice == 1) {
                System.out.print("Enter Treatment ID: ");
                String tid = scanner.nextLine().trim();
                if (tid.isEmpty()) {
                    System.out.println("Error: Treatment ID cannot be empty.");
                    continue;
                }
                System.out.print("Note: ");
                String note = scanner.nextLine().trim();
                if (note.isEmpty()) {
                    System.out.println("Error: Note cannot be empty.");
                    continue;
                }
                boolean ok = controller.addNote(tid, note);
                System.out.println(ok ? "Note added successfully." : "Error: Treatment not found.");
            } else if (choice == 2) {
                System.out.print("Enter Treatment ID: ");
                String tid = scanner.nextLine().trim();
                if (tid.isEmpty()) {
                    System.out.println("Error: Treatment ID cannot be empty.");
                    continue;
                }
                boolean ok = controller.deleteAllNotes(tid);
                System.out.println(ok ? "All notes deleted successfully." : "Error: Treatment not found.");
            } else if (choice == 3) {
                return;
            } else {
                System.out.println("Invalid option. Please enter 1-3.");
            }
        }
    }

    private void viewAllDetails() {
        ArrayList<MedicalTreatment> list = controller.getAllTreatments();
        if (list.size() == 0) {
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("           NO TREATMENTS FOUND            ");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            return;
        }
        
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("         ALL TREATMENT RECORDS             ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        
        for (int i = 0; i < list.size(); i++) {
            System.out.println("\n══════════════════════════════════════════");
            System.out.println("           TREATMENT #" + (i + 1) + "                ");
            System.out.println("══════════════════════════════════════════");
            printTreatmentDetails(list.get(i));
        }
    }

    private void viewByPatient() {
        if (dummyPatients.size() == 0) {
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("           NO PATIENTS AVAILABLE          ");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            return;
        }
        
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("         PATIENT TREATMENT VIEW            ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Select Patient:");
        for (int i = 0; i < dummyPatients.size(); i++) {
            Patient p = dummyPatients.get(i);
            System.out.println((i + 1) + ". " + p.getID() + " - " + p.getName());
        }
        System.out.print("Choose (1-" + dummyPatients.size() + "): ");
        int idx = readInt();
        if (idx < 1 || idx > dummyPatients.size()) {
            System.out.println("Error: Invalid choice. Please enter 1-" + dummyPatients.size() + ".");
            return;
        }
        
        Patient selectedPatient = dummyPatients.get(idx - 1);
        String pid = selectedPatient.getID();
        ArrayList<MedicalTreatment> list = controller.getTreatmentsByPatientId(pid);
        
        if (list.size() == 0) {
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("      NO TREATMENTS FOR THIS PATIENT      ");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            return;
        }
        
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("    TREATMENTS FOR " + selectedPatient.getName().toUpperCase() + "    ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        
        for (int i = 0; i < list.size(); i++) {
            System.out.println("\n══════════════════════════════════════════");
            System.out.println("           TREATMENT #" + (i + 1) + "                ");
            System.out.println("══════════════════════════════════════════");
            printTreatmentDetails(list.get(i));
        }
    }

    private void addTreatment() {
        if (dummyPatients.size() == 0) {
            System.out.println("No dummy patients available.");
            return;
        }
        System.out.println("Select Patient:");
        for (int i = 0; i < dummyPatients.size(); i++) {
            Patient p = dummyPatients.get(i);
            System.out.println((i + 1) + ". " + p.getID() + " - " + p.getName());
        }
        System.out.print("Choose (1-" + dummyPatients.size() + "): ");
        int pIndex = readInt();
        if (pIndex < 1 || pIndex > dummyPatients.size()) {
            System.out.println("Error: Invalid choice. Please enter 1-" + dummyPatients.size() + ".");
            return;
        }
        Patient patient = dummyPatients.get(pIndex - 1);

        ArrayList<Doctor> doctorList = getDoctorsFromModule();
        if (doctorList.size() == 0) {
            System.out.println("No doctors available.");
            return;
        }
        System.out.println("Select Doctor:");
        for (int i = 0; i < doctorList.size(); i++) {
            Doctor d = doctorList.get(i);
            System.out.println((i + 1) + ". " + d.getDoctorId() + " - " + d.getName());
        }
        System.out.print("Choose (1-" + doctorList.size() + "): ");
        int dIndex = readInt();
        if (dIndex < 1 || dIndex > doctorList.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        Doctor doctor = doctorList.get(dIndex - 1);

        // Show patient's symptoms before creating treatment and capture diagnosis
        System.out.println("\nPatient Symptoms:");
        ADT.ArrayList<String> syms = patient.getPatientSymtomps();
        if (syms == null || syms.size() == 0) {
            System.out.println("(none recorded)");
        } else {
            for (int i = 0; i < syms.size(); i++) {
                System.out.println("- " + syms.get(i));
            }
        }
        System.out.print("Enter diagnosis: ");
        String diagnosis = scanner.nextLine().trim();

        MedicalTreatment mt = controller.createTreatment(patient, doctor);
        if (!diagnosis.isEmpty()) {
            // Persist diagnosis on patient entity and as a treatment note for visibility
            patient.setPatientDisease(diagnosis);
            controller.addNote(mt.getTreatmentId(), "Diagnosis: " + diagnosis);
        }
        System.out.println("Created Treatment:");
        printHeader(mt);

        // Add medicines using dropdown of available medicines
        while (true) {
            System.out.println("Add medicine?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            int add = readInt();
            if (add != 1) break;

            ArrayList<Pharmacy.Medicine> meds = getMedicinesFromPharmacy();
            System.out.println("Select Medicine:");
            for (int i = 0; i < meds.size(); i++) {
                Pharmacy.Medicine m = meds.get(i);
                System.out.println((i + 1) + ". " + m.getMedicineName() + " (" + m.getMedicineID() + ")");
            }
            System.out.print("Choose (1-" + meds.size() + "): ");
            int mIndex = readInt();
            if (mIndex < 1 || mIndex > meds.size()) {
                System.out.println("Error: Invalid choice. Please enter 1-" + meds.size() + ".");
                continue;
            }
            Pharmacy.Medicine chosen = meds.get(mIndex - 1);

            System.out.print("Times per day: ");
            int times = readInt();
            if (times <= 0 || times > 10) {
                System.out.println("Error: Times per day must be between 1 and 10.");
                continue;
            }
            System.out.print("Dosage (ml): ");
            int ml = readInt();
            if (ml <= 0 || ml > 1000) {
                System.out.println("Error: Dosage must be between 1 and 1000 ml.");
                continue;
            }

            controller.addMedicine(mt.getTreatmentId(), chosen.getMedicineName(), times, ml);
            System.out.println("Added: " + chosen.getMedicineName());
        }

        System.out.println("Treatment saved.");
    }

    private void updateTreatment() {
        System.out.print("Enter Treatment ID: ");
        String tid = scanner.nextLine().trim();
        if (tid.isEmpty()) {
            System.out.println("Error: Treatment ID cannot be empty.");
            return;
        }
        MedicalTreatment mt = controller.getTreatment(tid);
        if (mt == null) {
            System.out.println("Error: Treatment not found.");
            return;
        }
        System.out.println("Update options:");
        System.out.println("1. Change Doctor");
        System.out.println("2. Replace Medicines");
        System.out.println("3. Back");
        int ch = readInt();
        if (ch == 1) {
            ArrayList<Doctor> doctorList = getDoctorsFromModule();
            if (doctorList.size() == 0) {
                System.out.println("No doctors available.");
                return;
            }
            for (int i = 0; i < doctorList.size(); i++) {
                Doctor d = doctorList.get(i);
                System.out.println((i + 1) + ". " + d.getDoctorId() + " - " + d.getName());
            }
            System.out.print("Choose: ");
            int di = readInt();
            if (di < 1 || di > doctorList.size()) {
                System.out.println("Error: Invalid choice. Please enter 1-" + doctorList.size() + ".");
                return;
            }
            controller.updateDoctor(tid, doctorList.get(di - 1).getDoctorId());
            System.out.println("Doctor updated successfully.");
        } else if (ch == 2) {
            ArrayList<MedicalTreatment.MedicineInstruction> newList = new ArrayList<>();
            while (true) {
                System.out.println("Add a medicine instruction?");
                System.out.println("1. Yes");
                System.out.println("2. No (finish)");
                int add = readInt();
                if (add != 1) break;

                ArrayList<Pharmacy.Medicine> meds = getMedicinesFromPharmacy();
                for (int i = 0; i < meds.size(); i++) {
                    Pharmacy.Medicine m = meds.get(i);
                    System.out.println((i + 1) + ". " + m.getMedicineName() + " (" + m.getMedicineID() + ")");
                }
                System.out.print("Choose medicine: ");
                int mIndex = readInt();
                if (mIndex < 1 || mIndex > meds.size()) {
                    System.out.println("Error: Invalid choice. Please enter 1-" + meds.size() + ".");
                    continue;
                }
                Pharmacy.Medicine chosen = meds.get(mIndex - 1);
                System.out.print("Times per day: ");
                int times = readInt();
                if (times <= 0 || times > 10) {
                    System.out.println("Error: Times per day must be between 1 and 10.");
                    continue;
                }
                System.out.print("Dosage (ml): ");
                int ml = readInt();
                if (ml <= 0 || ml > 1000) {
                    System.out.println("Error: Dosage must be between 1 and 1000 ml.");
                    continue;
                }
                newList.add(new MedicalTreatment.MedicineInstruction(chosen.getMedicineName(), times, ml));
            }
            boolean ok = controller.replaceMedicines(tid, newList);
            System.out.println(ok ? "Medicines updated successfully." : "Error: Failed to update medicines.");
        }
    }

    private void deleteTreatment() {
        System.out.print("Enter Treatment ID: ");
        String tid = scanner.nextLine().trim();
        if (tid.isEmpty()) {
            System.out.println("Error: Treatment ID cannot be empty.");
            return;
        }
        boolean ok = controller.deleteTreatment(tid);
        System.out.println(ok ? "Treatment deleted successfully." : "Error: Treatment not found.");
    }

    private void markComplete() {
        System.out.print("Enter Treatment ID: ");
        String tid = scanner.nextLine().trim();
        if (tid.isEmpty()) {
            System.out.println("Error: Treatment ID cannot be empty.");
            return;
        }
        boolean ok = controller.markComplete(tid);
        System.out.println(ok ? "Status set to Inactive successfully." : "Error: Treatment not found.");
    }

    private void printHeader(MedicalTreatment mt) {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("           TREATMENT DETAILS             ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("┌──────────────────────────────────────┐");
        System.out.println("│  Treatment ID: " + String.format("%-20s", mt.getTreatmentId()) );
        System.out.println("│  Patient ID:   " + String.format("%-20s", mt.getPatientId()) );
        System.out.println("│  Doctor ID:    " + String.format("%-20s", mt.getDoctorId()) );
        System.out.println("│  Patient Name: " + String.format("%-20s", mt.getPatientName()) );
        System.out.println("│  Status:       " + String.format("%-20s", mt.getStatus()) );
        System.out.println("│  Date:         " + String.format("%-20s", mt.getCreatedDate().toString()) );
        System.out.println("└──────────────────────────────────────┘");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private void printTreatmentDetails(MedicalTreatment mt) {
        // Print header information
        System.out.println("┌──────────────────────────────────────┐");
        System.out.println("│  Treatment ID: " + String.format("%-20s", mt.getTreatmentId()) + "");
        System.out.println("│  Patient ID:   " + String.format("%-20s", mt.getPatientId()) + "");
        System.out.println("│  Doctor ID:    " + String.format("%-20s", mt.getDoctorId()) + "");
        System.out.println("│  Patient Name: " + String.format("%-20s", mt.getPatientName()) + "");
        System.out.println("│  Status:       " + String.format("%-20s", mt.getStatus()) + "");
        System.out.println("│  Date:         " + String.format("%-20s", mt.getCreatedDate().toString()) + "");
        System.out.println("└──────────────────────────────────────┘");

        // Print symptoms and diagnosis from Patient module
        Patient matched = null;
        for (int i = 0; i < dummyPatients.size(); i++) {
            if (dummyPatients.get(i).getID().equals(mt.getPatientId())) {
                matched = dummyPatients.get(i);
                break;
            }
        }
        System.out.println("\nSYMPTOMS & DIAGNOSIS:");
        System.out.println("┌──────────────────────────────────────┐");
        if (matched == null) {
            System.out.println("│  (patient record not found in UI cache)");
        } else {
            ADT.ArrayList<String> syms = matched.getPatientSymtomps();
            if (syms == null || syms.size() == 0) {
                System.out.println("│  Symptoms: (none recorded)");
            } else {
                System.out.println("│  Symptoms:");
                for (int i = 0; i < syms.size(); i++) {
                    System.out.println("│   - " + syms.get(i));
                }
            }
            String dx = matched.getPatientDisease();
            System.out.println("│  Diagnosis: " + (dx == null || dx.isEmpty() ? "(none)" : dx));
        }
        System.out.println("└──────────────────────────────────────┘");
        
        // Print medicines
        System.out.println("\nMEDICINES:");
        System.out.println("┌──────────────────────────────────────┐");
        if (mt.getMedicines().size() == 0) {
            System.out.println("│  No medicines prescribed              ");
        } else {
            for (int i = 0; i < mt.getMedicines().size(); i++) {
                MedicalTreatment.MedicineInstruction med = mt.getMedicines().get(i);
                System.out.println("│  " + (i + 1) + ". " + String.format("%-30s", med.getMedicineName()));
                System.out.println("│     " + String.format("%-30s", "   " + med.getTimesPerDay() + "x/day  " + med.getDosageMl() + "ml") );
            }
        }
        System.out.println("└──────────────────────────────────────┘");
        
        // Print notes
        System.out.println("\n NOTES:");
        System.out.println("┌──────────────────────────────────────┐");
        if (mt.getNotes().size() == 0) {
            System.out.println("│  No notes available                   ");
        } else {
            for (int i = 0; i < mt.getNotes().size(); i++) {
                String note = mt.getNotes().get(i);
                System.out.println("│  " + (i + 1) + ". " + String.format("%-30s", note));
            }
        }
        System.out.println("└──────────────────────────────────────┘");
    }

    private int readInt() {
        while (true) {
            String s = scanner.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (Exception e) {
                System.out.print("Error: Please enter a valid number: ");
            }
        }
    }

    private void seedDummyData() {
        // Initialize patients from Patient module's dummy data
        if (dummyPatients.size() == 0) {
            try {
                Boundaries.UIPatientManagement pui = new Boundaries.UIPatientManagement();
                pui.DummyData();
                int i = 1;
                while (true) {
                    String id = "P" + String.format("%04d", i);
                    Patient p = pui.controller.findPatientByID(id);
                    if (p == null) break;
                    dummyPatients.add(p);
                    i++;
                }
            } catch (Exception e) {
                // fallback: no patients available
            }
        }

        // Ensure Pharmacy module dummy data is initialized (medicines)
        try {
            new Boundaries.UIPharmacy();
        } catch (Exception e) {
            // ignore
        }
        // Seed 10 dummy treatments if none exist yet
        if (controller.getAllTreatments().size() == 0 && dummyPatients.size() > 0) {
            ArrayList<Doctor> docs = getDoctorsFromModule();
            if (docs.size() == 0) {
                // cannot proceed without doctors
                return;
            }
            ArrayList<Pharmacy.Medicine> meds = getMedicinesFromPharmacy();
            int totalToCreate = 10;
            for (int i = 0; i < totalToCreate; i++) {
                Patient p = dummyPatients.get(i % dummyPatients.size());
                Doctor d = docs.get(i % docs.size());
                MedicalTreatment mt = controller.createTreatment(p, d);
                // assign varying created dates across recent days/months
                try {
                    LocalDate dt = LocalDate.now().minusDays((i * 3) % 60).minusWeeks(i % 3);
                    java.lang.reflect.Field cdf = Entity.MedicalTreatment.class.getDeclaredField("createdDate");
                    cdf.setAccessible(true);
                    cdf.set(mt, dt);
                } catch (Exception ignore) {}
                // add 1-2 medicines if available
                if (meds.size() > 0) {
                    Pharmacy.Medicine m1 = meds.get(i % meds.size());
                    controller.addMedicine(mt.getTreatmentId(), m1.getMedicineName(), 2 + (i % 3), 5 * (1 + (i % 4)));
                }
                if (meds.size() > 1 && i % 2 == 0) {
                    Pharmacy.Medicine m2 = meds.get((i + 1) % meds.size());
                    controller.addMedicine(mt.getTreatmentId(), m2.getMedicineName(), 1 + (i % 2), 10);
                }
                // attach diagnosis from patient if any symptoms exist, else generic note
                ADT.ArrayList<String> syms = p.getPatientSymtomps();
                if (syms != null && syms.size() > 0) {
                    String inferred = syms.get(0);
                    p.setPatientDisease(inferred);
                    controller.addNote(mt.getTreatmentId(), "Diagnosis: " + inferred);
                } else {
                    controller.addNote(mt.getTreatmentId(), "Auto-seeded record.");
                }
            }
        }

        // Ensure there are 20 total treatments by creating 10 more with random dates/details
        if (dummyPatients.size() > 0) {
            ArrayList<MedicalTreatment> existing = controller.getAllTreatments();
            if (existing.size() < 20) {
                ArrayList<Doctor> docs = getDoctorsFromModule();
                if (docs.size() == 0) return;
                ArrayList<Pharmacy.Medicine> meds = getMedicinesFromPharmacy();
                Random rng = new Random();
                int target = 20;
                while (controller.getAllTreatments().size() < target) {
                    Patient p = dummyPatients.get(rng.nextInt(dummyPatients.size()));
                    Doctor d = docs.get(rng.nextInt(docs.size()));
                    MedicalTreatment mt = controller.createTreatment(p, d);
                    // random created date within last ~180 days
                    try {
                        int backDays = 1 + rng.nextInt(180);
                        LocalDate dt = LocalDate.now().minusDays(backDays);
                        java.lang.reflect.Field cdf = Entity.MedicalTreatment.class.getDeclaredField("createdDate");
                        cdf.setAccessible(true);
                        cdf.set(mt, dt);
                    } catch (Exception ignore) {}
                    // random 1-3 medicines
                    int medCount = meds.size() == 0 ? 0 : 1 + rng.nextInt(Math.min(3, Math.max(1, meds.size())));
                    for (int k = 0; k < medCount; k++) {
                        Pharmacy.Medicine m = meds.get(rng.nextInt(meds.size()));
                        int times = 1 + rng.nextInt(4);
                        int ml = (rng.nextInt(6) + 1) * 5; // multiples of 5ml
                        controller.addMedicine(mt.getTreatmentId(), m.getMedicineName(), times, ml);
                    }
                    // random diagnosis based on symptom if available, else generic
                    ADT.ArrayList<String> syms = p.getPatientSymtomps();
                    String dx = (syms != null && syms.size() > 0) ? syms.get(rng.nextInt(syms.size())) : null;
                    if (dx == null || dx.trim().isEmpty()) {
                        String[] generic = {"Flu", "Cough", "Fever", "Headache", "Sore Throat", "Allergy"};
                        dx = generic[rng.nextInt(generic.length)];
                    }
                    p.setPatientDisease(dx);
                    controller.addNote(mt.getTreatmentId(), "Diagnosis: " + dx);
                    // occasional extra note
                    if (rng.nextBoolean()) {
                        String[] notes = {"Take after meals.", "Avoid driving.", "Stay hydrated.", "Rest well.", "Follow up in 1 week."};
                        controller.addNote(mt.getTreatmentId(), notes[rng.nextInt(notes.length)]);
                    }
                }
            }
        }
    }

    private ArrayList<Doctor> getDoctorsFromModule() {
        DoctorController dc = new DoctorController();
        LinkedList<Doctor> ll = dc.getAllDoctors();
        ArrayList<Doctor> list = new ArrayList<>();
        if (ll.size() == 0) {
            // Seed via Doctor UI if empty
            try {
                new UIDoctorManagement();
                // constructor seeds dummy data
                ll = dc.getAllDoctors();
            } catch (Exception e) {
                // ignore
            }
        }
        for (int i = 0; i < ll.size(); i++) {
            list.add(ll.get(i));
        }
        return list;
    }

    
    private ArrayList<Pharmacy.Medicine> getMedicinesFromPharmacy() {
        ArrayList<Pharmacy.Medicine> list = new ArrayList<>();
        try {
            Controller.PharmacyController pc = new Controller.PharmacyController();
            // Reflectively access the private field 'medicines' to read dummy data
            java.lang.reflect.Field f = Controller.PharmacyController.class.getDeclaredField("medicines");
            f.setAccessible(true);
            ADT.HashMap<String, Pharmacy.Medicine> meds = (ADT.HashMap<String, Pharmacy.Medicine>) f.get(pc);
            meds.forEach((id, med) -> list.add(med));
        } catch (Exception e) {
            // fallback to controller's local list if reflection fails
            ArrayList<Pharmacy.Medicine> fallback = controller.getAvailableMedicines();
            for (int i = 0; i < fallback.size(); i++) list.add(fallback.get(i));
        }
        return list;
    }

    // Report: percentage usage of each medicine across all treatments
    private void showMedicineUsageReport() {
        ArrayList<MedicalTreatment> all = controller.getAllTreatments();
        if (all.size() == 0) {
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("      NO TREATMENTS TO REPORT USAGE       ");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            return;
        }

        HashMap<String, Integer> counts = new HashMap<>(64);
        int totalPrescriptions = 0;
        for (int i = 0; i < all.size(); i++) {
            MedicalTreatment mt = all.get(i);
            for (int j = 0; j < mt.getMedicines().size(); j++) {
                String name = mt.getMedicines().get(j).getMedicineName();
                Integer prev = counts.get(name);
                counts.put(name, (prev == null ? 0 : prev) + 1);
                totalPrescriptions++;
            }
        }

        if (totalPrescriptions == 0) {
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("       NO MEDICINES PRESCRIBED YET        ");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            return;
        }

        System.out.println("\n================================ MEDICINE USAGE REPORT ================================");
        System.out.printf("%-30s | %-12s | %-10s | %s\n", "Medicine", "Count", "Percent", "Chart");
        System.out.println("--------------------------------------------------------------------------------------");
        final int tp = totalPrescriptions;
        counts.forEach(new KVConsumer<String, Integer>() {
            @Override
            public void accept(String medName, Integer count) {
                double pct = (count * 100.0) / tp;
                int bars = (int) Math.round(pct);
                StringBuilder bar = new StringBuilder();
                for (int i = 0; i < bars; i++) bar.append("█");
                System.out.printf("%-30s | %-12d | %8.2f%% | %s\n", medName, count, pct, bar.toString());
            }
        });
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.printf("Total prescriptions: %d\n", totalPrescriptions);
    }

    // Report: number and percentage of treatments per month (yyyy-MM)
    private void showMonthlyTreatmentReport() {
        ArrayList<MedicalTreatment> all = controller.getAllTreatments();
        if (all.size() == 0) {
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("      NO TREATMENTS TO REPORT MONTHLY     ");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            return;
        }

        HashMap<String, Integer> byMonth = new HashMap<>(32);
        int total = 0;
        for (int i = 0; i < all.size(); i++) {
            String key = all.get(i).getCreatedDate().toString().substring(0, 7); // yyyy-MM
            Integer prev = byMonth.get(key);
            byMonth.put(key, (prev == null ? 0 : prev) + 1);
            total++;
        }

        System.out.println("\n============================= MONTHLY TREATMENT SUMMARY ============================");
        System.out.printf("%-10s | %-8s | %-9s | %s\n", "Month", "Count", "Percent", "Chart");
        System.out.println("-----------------------------------------------------------------------------------");

        // Collect keys to sort chronologically (yyyy-MM sorts lexicographically correctly)
        ADT.ArrayList<String> months = new ADT.ArrayList<>();
        byMonth.forEach(new KVConsumer<String, Integer>() {
            @Override
            public void accept(String k, Integer v) { months.add(k); }
        });
        // simple insertion sort for our ADT.ArrayList
        for (int i = 1; i < months.size(); i++) {
            String key = months.get(i);
            int j = i - 1;
            while (j >= 0 && months.get(j).compareTo(key) > 0) {
                String tmp = months.get(j);
                months.remove(j);
                months.add(j + 1, tmp);
                j--;
            }
        }

        for (int i = 0; i < months.size(); i++) {
            String m = months.get(i);
            int count = byMonth.get(m);
            double pct = (count * 100.0) / total;
            int bars = (int) Math.round(pct);
            StringBuilder bar = new StringBuilder();
            for (int b = 0; b < bars; b++) bar.append("█");
            System.out.printf("%-10s | %-8d | %7.2f%% | %s\n", m, count, pct, bar.toString());
        }
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.printf("Total treatments: %d\n", total);
    }
}



