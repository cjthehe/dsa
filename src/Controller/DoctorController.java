package Controller;

import ADT.AVLTree;
import ADT.LinkedList;
import ADT.HashMap;
import Entity.Doctor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Manages Doctor profiles and schedules.
 */
public class DoctorController {
    private static int doctorCounter = 1;

    // Shared storage across instances
    private static final AVLTree<String, Doctor> doctorIndex = new AVLTree<>();
    private static final LinkedList<Doctor> doctorList = new LinkedList<>();

    // doctorId -> (date -> list of available time slots)
    private static final HashMap<String, HashMap<LocalDate, LinkedList<LocalTime>>> doctorSchedules = new HashMap<>();

    // ===== Doctor Profile Management =====
    public Doctor addDoctor(String name, String specialization, int yearsOfExperience,
                            char gender, String phoneNumber, String email, LocalDate hiredDate) {
        String id = generateDoctorId();
        Doctor doctor = new Doctor(id, name, specialization, yearsOfExperience, gender,
                phoneNumber, email, hiredDate == null ? LocalDate.now() : hiredDate, true);
        doctorIndex.insert(id, doctor);
        doctorList.add(doctor);
        return doctor;
    }

    public Doctor getDoctorById(String doctorId) {
        return doctorIndex.search(doctorId);
    }

    public LinkedList<Doctor> getAllDoctors() {
        return doctorList; // Return the ADT LinkedList containing all doctors
    }

    public boolean updateDoctorField(String doctorId, String field, String newValue) {
        Doctor d = doctorIndex.search(doctorId);
        if (d == null) return false;
        boolean changed = false;
        switch (field.toLowerCase()) {
            case "name":
                d.setName(newValue);
                changed = true;
                break;
            case "specialization":
                d.setSpecialization(newValue);
                changed = true;
                break;
            case "experience":
            case "yearsofexperience":
            case "years_of_experience":
                d.setYearsOfExperience(parseIntSafe(newValue, d.getYearsOfExperience()));
                changed = true;
                break;
            case "gender":
                d.setGender(newValue != null && !newValue.isEmpty() ? newValue.charAt(0) : d.getGender());
                changed = true;
                break;
            case "phone":
            case "phonenumber":
                d.setPhoneNumber(newValue);
                changed = true;
                break;
            case "email":
                d.setEmail(newValue);
                changed = true;
                break;
            case "hireddate":
            case "hired_date":
                d.setHiredDate(parseDateSafe(newValue, d.getHiredDate()));
                changed = true;
                break;
            case "active":
                d.setActive(Boolean.parseBoolean(newValue));
                changed = true;
                break;
            default:
                return false;
        }
        if (changed) {
            // Data updated in memory
        }
        return true;
    }

    public boolean deleteDoctor(String doctorId) {
        Doctor d = doctorIndex.search(doctorId);
        if (d == null) return false;
        doctorIndex.delete(doctorId);
        removeFromDoctorList(d);
        doctorSchedules.remove(doctorId);
        return true;
    }

    // Helper method to remove doctor from LinkedList
    private boolean removeFromDoctorList(Doctor doctor) {
        for (int i = 0; i < doctorList.size(); i++) {
            Doctor d = doctorList.get(i);
            if (d.getDoctorId().equals(doctor.getDoctorId())) {
                doctorList.remove(i);
                return true;
            }
        }
        return false;
    }

    // ===== Schedule Management =====
    public void defineAvailableSlots(String doctorId, LocalDate date, LocalTime startInclusive,
                                     LocalTime endExclusive, int intervalMinutes) {
        LinkedList<LocalTime> slots = generateSlots(startInclusive, endExclusive, intervalMinutes);
        setSlotsForDate(doctorId, date, slots);
    }

    public HashMap<LocalDate, LinkedList<LocalTime>> getSchedule(String doctorId) {
        HashMap<LocalDate, LinkedList<LocalTime>> schedule = doctorSchedules.get(doctorId);
        return schedule != null ? schedule : new HashMap<>();
    }

    public LinkedList<LocalTime> getSlotsForDate(String doctorId, LocalDate date) {
        HashMap<LocalDate, LinkedList<LocalTime>> map = doctorSchedules.get(doctorId);
        if (map == null) return new LinkedList<>();
        LinkedList<LocalTime> slots = map.get(date);
        return slots != null ? slots : new LinkedList<>();
    }

    public void setSlotsForDate(String doctorId, LocalDate date, LinkedList<LocalTime> slots) {
        HashMap<LocalDate, LinkedList<LocalTime>> map = doctorSchedules.get(doctorId);
        if (map == null) {
            map = new HashMap<>();
            doctorSchedules.put(doctorId, map);
        }
        map.put(date, slots);
    }

    public boolean addTimeSlot(String doctorId, LocalDate date, LocalTime slot) {
        HashMap<LocalDate, LinkedList<LocalTime>> map = doctorSchedules.get(doctorId);
        if (map == null) {
            map = new HashMap<>();
            doctorSchedules.put(doctorId, map);
        }
        
        LinkedList<LocalTime> list = map.get(date);
        if (list == null) {
            list = new LinkedList<>();
            map.put(date, list);
        }
        
        // Check if slot already exists
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(slot)) {
                return false; // Slot already exists
            }
        }
        
        list.add(slot);
        return true;
    }

    public boolean removeTimeSlot(String doctorId, LocalDate date, LocalTime slot) {
        HashMap<LocalDate, LinkedList<LocalTime>> map = doctorSchedules.get(doctorId);
        if (map == null) return false;
        
        LinkedList<LocalTime> list = map.get(date);
        if (list == null) return false;
        
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(slot)) {
                list.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean isSlotAvailable(String doctorId, LocalDate date, LocalTime slot) {
        HashMap<LocalDate, LinkedList<LocalTime>> map = doctorSchedules.get(doctorId);
        if (map == null) return false;
        
        LinkedList<LocalTime> list = map.get(date);
        if (list == null) return false;
        
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(slot)) {
                return true;
            }
        }
        return false;
    }

    // Book a slot by removing it from availability
    public boolean bookSlot(String doctorId, LocalDate date, LocalTime slot) {
        return removeTimeSlot(doctorId, date, slot);
    }

    public void updateWorkingHours(String doctorId, LocalDate date, LocalTime newStartInclusive,
                                   LocalTime newEndExclusive, int intervalMinutes) {
        defineAvailableSlots(doctorId, date, newStartInclusive, newEndExclusive, intervalMinutes);
    }

    public int removeTimeSlots(String doctorId, LocalDate date, LinkedList<LocalTime> slotsToRemove) {
        int removed = 0;
        for (int i = 0; i < slotsToRemove.size(); i++) {
            LocalTime t = slotsToRemove.get(i);
            if (removeTimeSlot(doctorId, date, t)) removed++;
        }
        return removed;
    }

    // ===== Helpers =====
    private String generateDoctorId() {
        return "D" + String.format("%03d", doctorCounter++);
    }

    private int parseIntSafe(String s, int fallback) {
        try { 
            return Integer.parseInt(s); 
        } catch (Exception e) { 
            return fallback; 
        }
    }

    private LocalDate parseDateSafe(String s, LocalDate fallback) {
        try { 
            return LocalDate.parse(s); 
        } catch (Exception e) { 
            return fallback; 
        }
    }

    private LinkedList<LocalTime> generateSlots(LocalTime startInclusive, LocalTime endExclusive, int minutes) {
        LinkedList<LocalTime> result = new LinkedList<>();
        if (startInclusive == null || endExclusive == null || minutes <= 0) return result;
        LocalTime t = startInclusive;
        while (!t.isAfter(endExclusive.minusMinutes(minutes))) {
            result.add(t);
            t = t.plusMinutes(minutes);
        }
        return result;
    }
}


