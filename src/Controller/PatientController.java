package Controller;

import ADT.QueueADT;
import ADT.AVLTree;
import Entity.Patient;
import java.time.LocalDate;
import java.time.Year;
import java.util.Iterator;

public class PatientController {
    private static int patientCounter = 1;
    private final QueueADT<Patient> arrayQueue = new QueueADT<>(30);
    private AVLTree<String, Patient> tree = new AVLTree<>();
    private Patient patient;
    
    public Patient patientRegistration(String name, String icNumber, 
                                   String phoneNumber, String email) {

        String patientID = "P" + String.format("%04d", patientCounter++);
        LocalDate registrationDate = LocalDate.now();

        int age = Integer.parseInt(calForAge(icNumber));

        char gender = calForGender(icNumber);

        String state = calFarState(icNumber);
        
        patient = new Patient(patientID, name, icNumber, age, gender,
                              phoneNumber, email,state, null, null, registrationDate);

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
        if(name != null && !name.matches("\\d+")){
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
//        if(formattedPhoneNumber.matches("\\d+")){
//            return true;
//        }
        return false;
    }
    
    public boolean EmailValidation(String email){
        if(email.contains("@") && email.contains(".")){
            return true;
        }
        return false;
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

    
    public boolean updatePatient(String id, String ic, String fieldUpdate, String newValue) {
        patient = tree.search(id);
        
        if (patient == null || !patient.getIc().equals(ic)) {
            return false;
        }
        
        switch (fieldUpdate.toLowerCase()) {
            case "name":
                patient.setName(newValue);
                break;
            case "age":
                patient.setAge(Integer.parseInt(newValue));
                break;
            case "gender":
                patient.setGender(newValue.charAt(0));
                break;
            case "phone":
                patient.setPhoneNumber(newValue);
                break;
            case "email":
                patient.setEmail(newValue);
                break;
            default:
                return false;
        }
        return true;
    }
    
    public int[] calAgeGroup() {
        int[] counts = new int[7];
        // Infant, Toddler, Child, Teenager, Young Adult, Adult, Senior
        
        
        // for each patient in arrayQueue. do the operation in the for loop
        for (Patient patient : arrayQueue) {
            int age = patient.getAge();
            
             if (age <= 1) {
                counts[0]++;
            } else if (age <= 3) { 
                counts[1]++;
            } else if (age <= 12) {
                counts[2]++;
            } else if (age <= 19) {
                counts[3]++;
            } else if (age <= 35) {
                counts[4]++;
            } else if (age <= 60) {
                counts[5]++;
            } else {
                counts[6]++;
            }
        }
        return counts;
    }

}