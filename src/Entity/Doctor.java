package Entity;

import java.time.LocalDate;
import java.util.Objects;

public class Doctor {
    private String doctorId;          // e.g. D001
    private String name;              // e.g. Dr. Lim
    private String specialization;    // e.g. General Practitioner, Pediatrics
    private int yearsOfExperience;    // e.g. 10
    private char gender;              // 'M', 'F', or other char
    private String phoneNumber;       // contact number
    private String email;             // contact email
    private LocalDate hiredDate;      // date joined the clinic
    private boolean active;           // employment/availability status

    public Doctor(String doctorId, String name, String specialization) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.gender = 'U';
        this.active = true;
        this.hiredDate = LocalDate.now();
    }

    public Doctor(String doctorId, String name, String specialization, int yearsOfExperience, 
            char gender, String phoneNumber, String email, LocalDate hiredDate, boolean active) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.hiredDate = hiredDate;
        this.active = active;
    }

    // Getters
    public String getDoctorId() { 
        return doctorId; 
    }
    public String getName() { 
        return name; 
    }
    public String getSpecialization() { 
        return specialization; 
    }
    public int getYearsOfExperience() { 
        return yearsOfExperience; 
    }
    public char getGender() { 
        return gender;
    }
    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    public String getEmail() { 
        return email; 
    }
    public LocalDate getHiredDate() { 
        return hiredDate; 
    }
    public boolean isActive() { 
        return active; 
    }

    // Setters
    public void setDoctorId(String doctorId) { 
        this.doctorId = doctorId; 
    }
    public void setName(String name) { 
        this.name = name; 
    }
    public void setSpecialization(String specialization) { 
        this.specialization = specialization; 
    }
    public void setYearsOfExperience(int yearsOfExperience) { 
        this.yearsOfExperience = yearsOfExperience; 
    }
    public void setGender(char gender) { 
        this.gender = gender; 
    }
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
    public void setHiredDate(LocalDate hiredDate) { 
        this.hiredDate = hiredDate; 
    }
    public void setActive(boolean active) { 
        this.active = active; 
    }

    // Utility
    public String genderToString() {
        switch (Character.toUpperCase(gender)) {
            case 'M':
                return "Male";
            case 'F':
                return "Female";
            default:
                return "Other";
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Doctor ID: %s | Name: %s | Spec: %s | Exp: %dyears | Gender: %s | Phone: %s | Email: %s | Hired: %s | Active: %s",
                doctorId,
                name,
                specialization,
                yearsOfExperience,
                genderToString(),
                phoneNumber == null ? "-" : phoneNumber,
                email == null ? "-" : email,
                hiredDate == null ? "-" : hiredDate.toString(),
                active ? "Yes" : "No");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(doctorId, doctor.doctorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId);
    }
}