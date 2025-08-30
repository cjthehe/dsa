package Controller;

import ADT.HashMap;
import ADT.ArrayList;
import Entity.MedicalTreatment;
import Entity.Pharmacy;
import Entity.Patient;
import Entity.Doctor;
import java.time.LocalDate;

public class MedicalTreatmentController {
    private final HashMap<String, MedicalTreatment> treatments = new HashMap<>(256);
    private final HashMap<String, ArrayList<MedicalTreatment>> treatmentsByPatient = new HashMap<>(256);
    private final ArrayList<Pharmacy.Medicine> availableMedicines = new ArrayList<>();
    private int treatmentCounter = 1;

    public ArrayList<Pharmacy.Medicine> getAvailableMedicines() {
        return availableMedicines;
    }

    private String generateTreatmentId() {
        return "T" + String.format("%04d", treatmentCounter++);
    }

    public MedicalTreatment createTreatment(Patient patient, Doctor doctor) {
        String id = generateTreatmentId();
        MedicalTreatment mt = new MedicalTreatment(id, patient.getID(), doctor.getDoctorId(), patient.getName(), LocalDate.now());
        treatments.put(id, mt);

        ArrayList<MedicalTreatment> list = treatmentsByPatient.get(patient.getID());
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(mt);
        treatmentsByPatient.put(patient.getID(), list);
        return mt;
    }

    public boolean addMedicine(String treatmentId, String medicineName, int timesPerDay, int dosageMl) {
        MedicalTreatment mt = treatments.get(treatmentId);
        if (mt == null) return false;
        mt.addMedicine(new MedicalTreatment.MedicineInstruction(medicineName, timesPerDay, dosageMl));
        treatments.put(treatmentId, mt);
        return true;
    }

    public boolean replaceMedicines(String treatmentId, ArrayList<MedicalTreatment.MedicineInstruction> newList) {
        MedicalTreatment mt = treatments.get(treatmentId);
        if (mt == null) return false;
        // clear by removing all existing entries
        while (mt.getMedicines().size() > 0) {
            mt.getMedicines().remove(0);
        }
        for (int i = 0; i < newList.size(); i++) {
            mt.addMedicine(newList.get(i));
        }
        treatments.put(treatmentId, mt);
        return true;
    }

    public boolean updateDoctor(String treatmentId, String newDoctorId) {
        MedicalTreatment mt = treatments.get(treatmentId);
        if (mt == null) return false;
        mt.setDoctorId(newDoctorId);
        treatments.put(treatmentId, mt);
        return true;
    }

    public boolean deleteTreatment(String treatmentId) {
        MedicalTreatment removed = treatments.remove(treatmentId);
        if (removed == null) return false;
        ArrayList<MedicalTreatment> list = treatmentsByPatient.get(removed.getPatientId());
        if (list != null) {
            // remove by reference
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == removed) {
                    list.remove(i);
                    break;
                }
            }
            treatmentsByPatient.put(removed.getPatientId(), list);
        }
        return true;
    }

    public boolean markComplete(String treatmentId) {
        MedicalTreatment mt = treatments.get(treatmentId);
        if (mt == null) return false;
        mt.setStatus("Inactive");
        treatments.put(treatmentId, mt);
        return true;
    }

    public boolean addNote(String treatmentId, String note) {
        MedicalTreatment mt = treatments.get(treatmentId);
        if (mt == null) return false;
        mt.addNote(note);
        treatments.put(treatmentId, mt);
        return true;
    }

    public boolean deleteNote(String treatmentId, int noteIndex) {
        MedicalTreatment mt = treatments.get(treatmentId);
        if (mt == null) return false;
        boolean ok = mt.removeNote(noteIndex);
        if (ok) treatments.put(treatmentId, mt);
        return ok;
    }

    public boolean deleteAllNotes(String treatmentId) {
        MedicalTreatment mt = treatments.get(treatmentId);
        if (mt == null) return false;
        mt.clearAllNotes();
        treatments.put(treatmentId, mt);
        return true;
    }

    public MedicalTreatment getTreatment(String id) {
        return treatments.get(id);
    }

    public ArrayList<MedicalTreatment> getAllTreatments() {
        ArrayList<MedicalTreatment> list = new ArrayList<>();
        treatments.forEach((k, v) -> list.add(v));
        return list;
    }

    public ArrayList<MedicalTreatment> getTreatmentsByPatientId(String patientId) {
        ArrayList<MedicalTreatment> list = treatmentsByPatient.get(patientId);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
}




