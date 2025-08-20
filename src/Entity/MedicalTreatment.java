package Entity;

import java.time.LocalDate;
import ADT.ArrayList;

public class MedicalTreatment {
    private String treatmentId;
    private String patientId;
    private String doctorId;
    private String patientName;
    private LocalDate createdDate;
    private String status; // Active | Inactive
    private ArrayList<MedicineInstruction> medicines;
    private ArrayList<String> notes;

    public static class MedicineInstruction {
        private String medicineName;
        private int timesPerDay;
        private int dosageMl;

        public MedicineInstruction(String medicineName, int timesPerDay, int dosageMl) {
            this.medicineName = medicineName;
            this.timesPerDay = timesPerDay;
            this.dosageMl = dosageMl;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public int getTimesPerDay() {
            return timesPerDay;
        }

        public int getDosageMl() {
            return dosageMl;
        }

        public void setMedicineName(String name) {
            this.medicineName = name;
        }

        public void setTimesPerDay(int tpd) {
            this.timesPerDay = tpd;
        }

        public void setDosageMl(int ml) {
            this.dosageMl = ml;
        }

        @Override
        public String toString() {
            return medicineName + " | " + timesPerDay + "x/day | " + dosageMl + "ml";
        }
    }

    public MedicalTreatment(String treatmentId, String patientId, String doctorId, String patientName, LocalDate createdDate) {
        this.treatmentId = treatmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.patientName = patientName;
        this.createdDate = createdDate == null ? LocalDate.now() : createdDate;
        this.status = "Active";
        this.medicines = new ArrayList<>();
        this.notes = new ArrayList<>();
    }

    public String getTreatmentId() {
        return treatmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getPatientName() {
        return patientName;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<MedicineInstruction> getMedicines() {
        return medicines;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addMedicine(MedicineInstruction mi) {
        medicines.add(mi);
    }

    public boolean removeMedicine(int index) {
        if (index < 0 || index >= medicines.size()) return false;
        medicines.remove(index);
        return true;
    }

    public void addNote(String note) {
        notes.add(note);
    }

    public boolean removeNote(int index) {
        if (index < 0 || index >= notes.size()) return false;
        notes.remove(index);
        return true;
    }

    public void clearAllNotes() {
        notes.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Treatment ID: ").append(treatmentId)
          .append(" | Patient ID: ").append(patientId)
          .append(" | Doctor ID: ").append(doctorId)
          .append(" | Patient Name: ").append(patientName)
          .append(" | Status: ").append(status)
          .append(" | Date: ").append(createdDate);
        sb.append("\nMedicines:\n");
        for (int i = 0; i < medicines.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(medicines.get(i)).append("\n");
        }
        if (medicines.size() == 0) {
            sb.append("  (none)\n");
        }
        sb.append("Notes:\n");
        for (int i = 0; i < notes.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(notes.get(i)).append("\n");
        }
        if (notes.size() == 0) {
            sb.append("  (none)\n");
        }
        return sb.toString();
    }
}




