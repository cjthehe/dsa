package Controller;

import ADT.ArrayQueue;
import Entity.Patient;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PatientController {
    // add static to avoid reset
    private static int patientCounter = 1;
    private final ArrayQueue<Patient> arrayQueue = new ArrayQueue<>(999);;
    
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
    
}
