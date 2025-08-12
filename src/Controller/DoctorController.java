package Controller;

import ADT.AVLTree;
import Entity.Doctor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages Doctor profiles and schedules.
 */
public class DoctorController {
    private static int doctorCounter = 1;

    // Shared storage across instances
    private static final AVLTree<String, Doctor> doctorIndex = new AVLTree<>();
    private static final List<Doctor> doctorList = new ArrayList<>();

    // doctorId -> (date -> list of available time slots)
    private static final Map<String, Map<LocalDate, List<LocalTime>>> doctorSchedules = new HashMap<>();

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
        doctorList.remove(d);
        doctorSchedules.remove(doctorId);
        return true;
    }

    

    // ===== Schedule Management =====
    public void defineAvailableSlots(String doctorId, LocalDate date, LocalTime startInclusive,
                                     LocalTime endExclusive, int intervalMinutes) {
        List<LocalTime> slots = generateSlots(startInclusive, endExclusive, intervalMinutes);
        setSlotsForDate(doctorId, date, slots);
    }

    public Map<LocalDate, List<LocalTime>> getSchedule(String doctorId) {
        return doctorSchedules.getOrDefault(doctorId, new HashMap<>());
    }

    public List<LocalTime> getSlotsForDate(String doctorId, LocalDate date) {
        Map<LocalDate, List<LocalTime>> map = doctorSchedules.get(doctorId);
        if (map == null) return new ArrayList<>();
        return new ArrayList<>(map.getOrDefault(date, new ArrayList<>()));
    }

    public void setSlotsForDate(String doctorId, LocalDate date, List<LocalTime> slots) {
        doctorSchedules.computeIfAbsent(doctorId, k -> new HashMap<>())
                .put(date, new ArrayList<>(slots));
    }

    public boolean addTimeSlot(String doctorId, LocalDate date, LocalTime slot) {
        Map<LocalDate, List<LocalTime>> map = doctorSchedules.computeIfAbsent(doctorId, k -> new HashMap<>());
        List<LocalTime> list = map.computeIfAbsent(date, k -> new ArrayList<>());
        if (!list.contains(slot)) {
            list.add(slot);
            list.sort(LocalTime::compareTo);
            return true;
        }
        return false;
    }

    public boolean removeTimeSlot(String doctorId, LocalDate date, LocalTime slot) {
        Map<LocalDate, List<LocalTime>> map = doctorSchedules.get(doctorId);
        if (map == null) return false;
        List<LocalTime> list = map.get(date);
        if (list == null) return false;
        return list.remove(slot);
    }

    public boolean isSlotAvailable(String doctorId, LocalDate date, LocalTime slot) {
        Map<LocalDate, List<LocalTime>> map = doctorSchedules.get(doctorId);
        if (map == null) return false;
        List<LocalTime> list = map.get(date);
        return list != null && list.contains(slot);
    }

    // Book a slot by removing it from availability
    public boolean bookSlot(String doctorId, LocalDate date, LocalTime slot) {
        return removeTimeSlot(doctorId, date, slot);
    }

    public void updateWorkingHours(String doctorId, LocalDate date, LocalTime newStartInclusive,
                                   LocalTime newEndExclusive, int intervalMinutes) {
        defineAvailableSlots(doctorId, date, newStartInclusive, newEndExclusive, intervalMinutes);
    }

    public int removeTimeSlots(String doctorId, LocalDate date, List<LocalTime> slotsToRemove) {
        int removed = 0;
        for (LocalTime t : slotsToRemove) {
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

    private List<LocalTime> generateSlots(LocalTime startInclusive, LocalTime endExclusive, int minutes) {
        List<LocalTime> result = new ArrayList<>();
        if (startInclusive == null || endExclusive == null || minutes <= 0) return result;
        LocalTime t = startInclusive;
        while (!t.isAfter(endExclusive.minusMinutes(minutes))) {
            result.add(t);
            t = t.plusMinutes(minutes);
        }
        return result;
    }

}


