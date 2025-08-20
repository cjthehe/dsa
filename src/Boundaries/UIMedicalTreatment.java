package Boundaries;

import Controller.MedicalTreatmentController;
import Controller.DoctorController;
import ADT.LinkedList;
import Entity.MedicalTreatment;
import Entity.Pharmacy;
import Entity.Patient;
import Entity.Doctor;
import ADT.ArrayList;
import java.time.LocalDate;
import java.util.Scanner;

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
            System.out.println("6. Back");
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
                    return;
                default:
                    System.out.println("Invalid option. Please enter 1-6.");
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

        MedicalTreatment mt = controller.createTreatment(patient, doctor);
        System.out.println("Created Treatment:");
        printHeader(mt);

        // Add medicines using dropdown of available medicines
        while (true) {
            System.out.println("Add medicine?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            int add = readInt();
            if (add != 1) break;

            ArrayList<Pharmacy.Medicine> meds = controller.getAvailableMedicines();
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

                ArrayList<Pharmacy.Medicine> meds = controller.getAvailableMedicines();
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
        if (dummyPatients.size() == 0) {
            dummyPatients.add(new Patient("P0001", "Alice Tan", "000101010001", 24, 'F', "0123456789", "alice@example.com", "Selangor", "D001", "Flu", LocalDate.now()));
            dummyPatients.add(new Patient("P0002", "Bob Lee", "990202020002", 25, 'M', "0198765432", "bob@example.com", "Johor", "D002", "Fever", LocalDate.now()));
            dummyPatients.add(new Patient("P0003", "Chong Wei", "980303030003", 26, 'M', "0171122334", "chong@example.com", "Penang", "D003", "Cough", LocalDate.now()));
        }
        ArrayList<MedicalTreatment> current = controller.getTreatmentsByPatientId("P0001");
        if (current.size() == 0) {
            ArrayList<Doctor> docs = getDoctorsFromModule();
            Doctor anyDoctor = docs.size() > 0 ? docs.get(0) : new Doctor("D001", "Dr. Lim", "General");
            MedicalTreatment mt = controller.createTreatment(dummyPatients.get(0), anyDoctor);
            controller.addMedicine(mt.getTreatmentId(), "Paracetamol", 3, 10);
            controller.addNote(mt.getTreatmentId(), "Take after meals.");
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
}



