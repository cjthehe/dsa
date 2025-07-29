/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundaries;

import Controller.PatientController;
import Entity.Patient;
import java.util.Scanner;

/**
 *
 * @author user
 */
public class UIPatientManagement {
    Scanner scanner = new Scanner(System.in);
    private static final PatientController controller = new PatientController();
    public void showOption(){
        
        System.out.println(" ===== Clinic name =====");
        //patient appointment management CRUD
        System.out.println("1. Make an registration ");
        System.out.println("2. View Patient Profile ");
        System.out.println("3. Update Profile");
        System.out.println("4. View Queue Status"); 
        System.out.println("5. Delete Patient Record");
        System.out.println("6. Exit ");
        System.out.println("========================\n");
        
        System.out.print("Select your option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\n");
        
        switch(choice){
            case 1:
                makeRegistration();
                break;
            case 2:
                viewRecord();
                break;
            case 3:
                updateProfile();
                break;
            case 4:
                viewQueue();
                break;
            case 5:
                deleteProfile();
                break;
            case 6:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Pls try again.");
                break;
        }
    }
    
    private void makeRegistration(){
        System.out.println(" ================ new appointment ================");
        
//        System.out.print("Enter your Student ID ( XXABCXXXXX ):");
//        String studID = scanner.nextLine();
        
        System.out.print("Enter your name: ");
        String studName = scanner.nextLine();
        
        // int ic 
        System.out.print("Enter your IC number ( without(-) ):");
        String studIC = scanner.nextLine();
        
        System.out.print("Enter your age:");
        int studAge = scanner.nextInt();
        scanner.nextLine();
        
        //char gender
        System.out.print("Enter your gender (M/F):");
        char studGender = scanner.next().charAt(0);
        scanner.nextLine();

        //String PN
        System.out.print("Enter your phone number (with (-)):");
        String studPhoneNo = scanner.nextLine();
        
        System.out.print("Enter your email:");
        String studEmail = scanner.nextLine();
        
        System.out.print("\n\n");
        
        controller.patientRegistration(studName, studIC, studAge, studGender, studPhoneNo, studEmail);
    }
    
    private void viewRecord(){
        System.out.print("Enter Patient ID to search: ");
    String id = scanner.nextLine();

    Patient patient = controller.findPatientByID(id);
        if (patient != null) {
        System.out.print("Enter IC number for verification: ");
        String ic = scanner.nextLine();

        if (ic.equals(patient.getIc())) {
            System.out.println("\n===== Patient Found =====");
            System.out.println("Patient ID         : " + patient.getID());
            System.out.println("Name               : " + patient.getName());
            System.out.println("IC Number          : " + patient.getIc());
            System.out.println("Age                : " + patient.getAge());
            System.out.println("Gender             : " + patient.genderToString());
            System.out.println("Phone              : " + patient.getPhoneNumber());
            System.out.println("Email              : " + patient.getEmail());
            System.out.println("Registration Date  : " + patient.getRegistrationDate());
            System.out.println("==========================\n");
        } else {
            System.out.println("IC number does not match. Access denied.\n");
        }
    } else {
        System.out.println("Patient not found.\n");
    }

    System.out.println("< Press Enter to return to Main Menu >");
    scanner.nextLine();
    showOption();
    }
    
    private void deleteProfile(){        
        System.out.println("Enter Patient ID to delete: ");
        String id = scanner.nextLine();
        
        Patient patient = controller.findPatientByID(id);
        
        if(patient == null){
            System.out.println("Patient not found.");
        }else{
            System.out.println("Enter your IC number: ");
            String icReqDelete = scanner.nextLine();
            
                if(icReqDelete.equals(patient.getIc())){
                    controller.deletePatientById(id);
                    System.out.println("Patient " + id + "has been deleted successfully");
                }else{
                    System.out.println("IC number does not match. Record delete failed.");
                }
            }
        
            System.out.println("< Press Enter to return to Main Menu >");
            scanner.nextLine();
            showOption();
        }
    
    private void viewQueue(){
        Patient nextPatient = controller.viewPatientQueue();
        
        if(nextPatient != null){
            System.out.println("\n===== Next Patient in Queue =====");
            System.out.println("Patient ID         : " + nextPatient.getID());
            System.out.println("Name               : " + nextPatient.getName());
            System.out.println("Gender             : " + nextPatient.genderToString());
            System.out.println("Phone              : " + nextPatient.getPhoneNumber());
            System.out.println("Email              : " + nextPatient.getEmail());
            System.out.println("===================================\n");
        }else{
            System.out.println("No patients currently in queue.\n");
        }
        System.out.println("< Press Enter to return to Main Menu >");
        scanner.nextLine();
        showOption();
    }
    
private void updateProfile() {
    System.out.print("Enter Patient ID to update: ");
    String id = scanner.nextLine();

    Patient patient = controller.findPatientByID(id);
    if (patient == null) {
        System.out.println("Patient not found.");
        System.out.println("< Press Enter to return to Main Menu >");
        scanner.nextLine();
        showOption();
        return;
    }

    System.out.print("Enter IC number for verification: ");
    String ic = scanner.nextLine();
    if (!ic.equals(patient.getIc())) {
        System.out.println("IC number does not match. Access denied.");
        System.out.println("< Press Enter to return to Main Menu >");
        scanner.nextLine();
        showOption();
        return;
    }

    boolean updating = true;
    while (updating) {
        System.out.println("\nSelect field to update:");
        System.out.println("1. Name");
        System.out.println("2. Age");
        System.out.println("3. Gender");
        System.out.println("4. Phone Number");
        System.out.println("5. Email");
        System.out.println("6. Exit");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        String field = null;
        switch (choice) {
            case 1: 
                field = "name"; 
                break;
            case 2: 
                field = "age"; 
                break;
            case 3: 
                field = "gender"; 
                break;
            case 4: 
                field = "phone"; 
                break;
            case 5: 
                field = "email"; 
                break;
            case 6: 
                updating = false; 
                continue;
            default:
                System.out.println("Invalid choice.");
                continue;
        }

        System.out.print("Enter new value for " + field + ": ");
        String newValue = scanner.nextLine();
        boolean success = controller.updatePatient(id, ic, field, newValue);
        if (success) {
            System.out.println("Field updated successfully.");
        } else {
            System.out.println("Update failed.");
        }
    }

        System.out.println("< Press Enter to return to Main Menu >");
        scanner.nextLine();
        showOption();
    }

}
