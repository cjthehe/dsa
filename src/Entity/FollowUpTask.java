package Entity;

import java.time.LocalDate;

public class FollowUpTask {
    private String taskId;     // e.g., T001
    private String patientId;  // e.g., P0001
    private String doctorId;   // e.g., D001
    private String note;       // description/note (e.g., "Stay Hospital")
    private LocalDate dueDate; // optional due date
    private boolean completed; // legacy status flag
    private String status;     // "Pending" | "Completed"

    public FollowUpTask(String taskId, String patientId, String doctorId, String note) {
        this.taskId = taskId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.note = note;
        this.dueDate = null;
        this.completed = false;
        this.status = "Pending";
    }

    public FollowUpTask(String taskId, String patientId, String doctorId, String note, LocalDate dueDate) {
        this.taskId = taskId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.note = note;
        this.dueDate = dueDate;
        this.completed = false;
        this.status = "Pending";
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
    public String getDescription() {
        return note;
    }
    public boolean isCompleted() { 
        return completed; 
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public String getStatus() {
        return status;
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
    public void setDescription(String description) {
        this.note = description;
    }
    public void setCompleted(boolean completed) { 
        this.completed = completed; 
        this.status = completed ? "Completed" : "Pending";
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public void setStatus(String status) {
        this.status = status;
        this.completed = "Completed".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return String.format("%s | Patient: %s | Doctor: %s | Due: %s | %s | %s",
                taskId,
                patientId,
                doctorId,
                dueDate == null ? "-" : dueDate.toString(),
                status == null ? (completed ? "Completed" : "Pending") : status,
                note == null || note.isEmpty() ? "-" : note);
    }
}



