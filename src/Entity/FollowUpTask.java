package Entity;

/**
 * Represents a follow-up record linking a patient to a doctor until completed.
 */
public class FollowUpTask {
    private String taskId;     // e.g., F0001
    private String patientId;  // e.g., P0001
    private String doctorId;   // e.g., D001
    private String note;       // optional note (e.g., Stay Hospital)
    private boolean completed; // status

    public FollowUpTask(String taskId, String patientId, String doctorId, String note) {
        this.taskId = taskId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.note = note;
        this.completed = false;
    }

    public String getTaskId() { 
        return taskId; 
    }
    public String getPatientId() { 
        return patientId; 
    }
    public String getDoctorId() { 
        return doctorId; 
    }
    public String getNote() { 
        return note; 
    }
    public boolean isCompleted() { 
        return completed; 
    }

    public void setTaskId(String taskId) { 
        this.taskId = taskId; 
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId; 
    }
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId; 
    }
    public void setNote(String note) { 
        this.note = note; 
    }
    public void setCompleted(boolean completed) { 
        this.completed = completed; 
    }

    @Override
    public String toString() {
        return String.format("%s | Patient: %s | Doctor: %s | %s | %s",
                taskId, patientId, doctorId, completed ? "Completed" : "Pending",
                note == null || note.isEmpty() ? "-" : note);
    }
}



