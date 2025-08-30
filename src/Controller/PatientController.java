/**
 * PatientController class
 * Author: Chan Guo Zhe
 */
package Controller;

import ADT.QueueADT;
import ADT.AVLTree;
import Entity.Patient;
import java.time.LocalDate;
import java.time.Year;
import ADT.ArrayList;
import java.util.Iterator;

public class PatientController {
    private static PatientController instance = null;
    
    private static int patientCounter = 1;
    private final QueueADT<Patient> arrayQueue = new QueueADT<>(50);
    private final ArrayList<Patient> patientSymptoms = new ArrayList<>();
    private AVLTree<String, Patient> tree = new AVLTree<>();
    private Patient patient;
    
    private PatientController() {}
    
    public static PatientController getInstance() {
        if (instance == null) {
            instance = new PatientController();
        }
        return instance;
    }
    
    public Patient patientRegistration(String name, String icNumber, 
                                   String phoneNumber, String email,String patientFaculty,ArrayList<String> patientSymptom, LocalDate registrationDate) {

        String patientID = "P" + String.format("%04d", patientCounter++);
        if(registrationDate == null){
            registrationDate = LocalDate.now();
        }
        
        int age = Integer.parseInt(calForAge(icNumber));

        char gender = calForGender(icNumber);

        String state = calFarState(icNumber);
        
        
        patient = new Patient(patientID, name, icNumber, age, gender,
                              phoneNumber, email,patientFaculty ,state, null, null,
                  patientSymptom, registrationDate);

        arrayQueue.enqueue(patient); 
        tree.insert(patientID, patient);

        return patient;
    }

    private String calForAge(String ic){
        int birthYear = Integer.parseInt(ic.substring(0, 2));
        int currentYear = Year.now().getValue();
        if (birthYear > currentYear % 100) {
            birthYear += 1900;
        } else {
            birthYear += 2000;
        }
        return String.valueOf(currentYear - birthYear);
    }
    
    
    private char calForGender(String ic){
        int lastIndexOfIc = Integer.parseInt(ic.substring(ic.length()-1));
        if(lastIndexOfIc % 2 == 0){
            return 'F';
        }else{
            return 'M';
        }
    }

    private String calFarState(String ic){
        String middleIndex = ic.substring(6,8);
        switch(middleIndex){
            case "01":
                return "Johor";
            case "02":
                return "Kedah";
            case "03":
                return "Kelantan";
            case "04":
                return "Melaka";
            case "05":
                return "Negeri Sembilan";
            case "06":
                return "Pahang";
            case "07":
                return "Pulau Pinang";
            case "08":
                return "Perak";
            case "09":
                return "Perlis";
            case "10":
                return "Selangor";
            case "11":
                return "Terrenganu";
            case "12":
                return "Sabah";
            case "13":
                return "Sarawak";
            default:
                return "Invalid state";
        }
    }
    
    public boolean NameValidation(String name){
        if(name != null && !name.trim().isEmpty() && !name.matches("\\d+")){
            return true;
        }
        return false;
    }
    
    public boolean ICvalidation(String ic){
        if(ic.length() == 12 && ic != null && ic.matches("\\d+")){
            return true;
        }
            return false;
    }
    
    public boolean PhoneValidation(String phoneNumber){
        String formattedPhoneNumber = phoneNumber.replace("-", "");
        if(formattedPhoneNumber.matches("\\d{10,11}")){
            return true;
        }
        return false;
    }
    
    public boolean EmailValidation(String email){
        if(email.contains("@") && email.contains(".")){
            return true;
        }
        return false;
    }

    public String mapFaculty(String option) {
        switch (option) {
            case "1": return "FAFB";
            case "2": return "FOCS";
            case "3": return "FOBE";
            case "4": return "FCCI";
            case "5": return "FOAS";
            case "6": return "FOET";
            case "7": return "FSSH";
            default:  return null;
        }
    }


    
    public Patient findPatientByID(String id) {
        return tree.search(id);
    }
    
    
    public boolean deletePatientById(String id) {
        patient = tree.search(id);
        if (patient != null) {
            tree.delete(id);
            
            Iterator<Patient> iter = arrayQueue.iterator();
            while (iter.hasNext()) {
                Patient p = iter.next();
                if (p.equals(patient)) {
                    arrayQueue.RemoveSpecificElement(p);
                    return true;
                }
            }
        }
        return false;
    }
    
    public Patient viewPatientQueue() {
        return arrayQueue.getFront();
    }

    /**
     * Check if a patient is currently in the queue
     * @param patientId the patient ID to check
     * @return true if patient is in queue, false otherwise
     */
    public boolean isPatientInQueue(String patientId) {
        if (patientId == null || patientId.trim().isEmpty()) {
            return false;
        }
        
        for (Patient patient : arrayQueue) {
            if (patient.getID().equals(patientId)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if a patient is the first in the queue (only first patient can create consultation)
     * @param patientId the patient ID to check
     * @return true if patient is first in queue, false otherwise
     */
    public boolean isFirstPatientInQueue(String patientId) {
        if (patientId == null || patientId.trim().isEmpty()) {
            return false;
        }
        
        Patient firstPatient = arrayQueue.getFront();
        return firstPatient != null && firstPatient.getID().equals(patientId);
    }

    /**
     * Get all patients currently in the queue
     * @return ArrayList of patients in queue
     */
    public ArrayList<Patient> getAllPatientsInQueue() {
        ArrayList<Patient> patientsInQueue = new ArrayList<>();
        
        // Use iterator to traverse the queue
        Iterator<Patient> iterator = arrayQueue.iterator();
        while (iterator.hasNext()) {
            Patient patient = iterator.next();
            if (patient != null) {
                patientsInQueue.add(patient);
            }
        }
        
        return patientsInQueue;
    }
    
    /**
     * Dequeue the first patient from the queue after consultation completion
     * @return the dequeued patient, or null if queue is empty
     */
    public Patient dequeueFirstPatient() {
        if (arrayQueue.isEmpty()) {
            return null;
        }
        
        Patient dequeuedPatient = arrayQueue.dequeue();
        System.out.println("Patient " + dequeuedPatient.getID() + " (" + dequeuedPatient.getName() + ") has been removed from the queue after consultation completion.");
        return dequeuedPatient;
    }
    
    /**
     * Get the position of a patient in the queue
     * @param patientId the patient ID to check
     * @return the position (1-based), or -1 if not found
     */
    public int getPatientQueuePosition(String patientId) {
        if (patientId == null || patientId.trim().isEmpty()) {
            return -1;
        }
        
        int position = 1;
        Iterator<Patient> iterator = arrayQueue.iterator();
        while (iterator.hasNext()) {
            Patient patient = iterator.next();
            if (patient != null && patient.getID().equals(patientId)) {
                return position;
            }
            position++;
        }
        return -1; // Patient not found in queue
    }

    
    public boolean updatePatient(String id, String ic, String fieldUpdate, String newValue) {
        patient = tree.search(id);
        
        if (patient == null || !patient.getIc().equals(ic)) {
            return false;
        }
        
        switch (fieldUpdate.toLowerCase()) {
            case "name":
                if(NameValidation(newValue)){
                patient.setName(newValue);
                }else{
                return false;
                }
                break;
            case "phone":
                if (PhoneValidation(newValue)) {
                patient.setPhoneNumber(newValue);
                }else{
                return false; 
                }
                break;
            case "email":
                if (EmailValidation(newValue)) {
                    patient.setEmail(newValue);
                }else{
                    return false;
                }
                break;
            case "faculty":
                String facultyName = mapFaculty(newValue);
                    if (facultyName != null) {
                        patient.setFaculty(facultyName);
                    } else {
                        return false;
                    }
                break;
            default:
                return false;
        }
        return true;
    }
    
    public int[] calFacultyGroup() {
        int[] counts = new int[7];
        // FAFB, FOCS, FOBE, FOCI, FOAS, FOET, FSSH
        
        
        // for each patient in arrayQueue. do the operation in the for loop
        for (Patient patient : arrayQueue) {
            String studFaculty = patient.getFaculty();
            
             if (studFaculty.equals("FAFB")) {
                counts[0]++;
            } else if (studFaculty.equals("FOCS")) { 
                counts[1]++;
            } else if (studFaculty.equals("FOBE")) {
                counts[2]++;
            } else if (studFaculty.equals("FOCI")) {
                counts[3]++;
            } else if (studFaculty.equals("FOAS")) {
                counts[4]++;
            } else if (studFaculty.equals("FOET")) {
                counts[5]++;
            } else {
                counts[6]++;
            }
        }
        return counts;
    }
    
    public int[] calRegGroup(){
        int[] monthCount = new int[12];
        
        for(Patient patient : arrayQueue){
            int patientRegMonth = patient.getRegistrationDate().getMonthValue() - 1;
            monthCount[patientRegMonth]++;
        }
        return monthCount;
    }
    
}