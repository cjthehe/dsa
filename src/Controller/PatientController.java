package Controller;

import ADT.ArrayQueue;
import ADT.AVLTree;
//import 
import Entity.Patient;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PatientController {
    // add static to avoid reset
    private static int patientCounter = 1;
    private final ArrayQueue<Patient> arrayQueue = new ArrayQueue<>(999);;
    private AVLTree<String, Patient> tree = new AVLTree<>();
    
    public void patientRegistration(String name, String icNumber, int age, char gender, String phoneNumber, String email){ 
        //patient ID generation
        String patientID = "P" + String.format("%04d", patientCounter++);
        
        // registration date
        LocalDate registrationDate = LocalDate.now();
        
        //registration time 
        LocalTime registrationTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a" );
        
        Patient patient = new Patient(patientID, name, icNumber, age, gender, phoneNumber, email, null, registrationDate);
        arrayQueue.enqueue(patient);
        tree.insert(patientID, patient);
        
        System.out.println("========================================");
        System.out.printf("%25s\n", "Appointment done");
        System.out.println("========================================");
        System.out.println("Patient ID        : " + patientID);
        System.out.println("Name              : " + patient.getName());
        System.out.println("IC Number         : " + patient.getIc());
        System.out.println("Age               : " + patient.getAge());
        System.out.println("Gender            : " + patient.genderToString());
        System.out.println("Phone Number      : " + patient.getPhoneNumber());
        System.out.println("Email             : " + patient.getEmail());
        System.out.println("Registration Date : " + registrationDate);
        System.out.println("Registration Date : " + registrationTime.format(timeFormatter));
        
        System.out.println("========================================\n");

        System.out.println("< Press Enter Back To Main Page > ");
        new java.util.Scanner(System.in).nextLine();
        new Boundaries.UIPatientManagement().showOption(); 
    }
    
    public Patient findPatientByID(String id) {
        return tree.search(id);
    }
    
    public void deletePatientById(String id){
        tree.delete(id);
    }
    
    
    public Patient viewPatientQueue(){
        return arrayQueue.peek();
    }

    public boolean updatePatient(String id, String ic,String fieldUpdate, String newValue){
        Patient patient = tree.search(id);
        
        if(patient == null){
            System.out.println("Patient not found.");
            return false;
        }
        
        if(!patient.getIc().equals(ic)){
            System.out.println("IC not match.Please try again");
            return false;
        }
        
        switch(fieldUpdate.toLowerCase()){
            case "name":
                patient.setName(newValue);
                break;
            
            case "ic":
                patient.setIc(newValue);
                break;
                
            case "phone":
                patient.setPhoneNumber(newValue);
                break;
            
            case "email":
                patient.setEmail(newValue);
                break;
            
            default:
                System.out.println("Invalid option.");
                return false;
        }
        return true;
    }
}
