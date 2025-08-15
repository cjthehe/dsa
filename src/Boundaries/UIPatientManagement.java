package Boundaries;

import Controller.PatientController;
import Entity.Patient;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author user
 */
public class UIPatientManagement {
    Scanner scanner = new Scanner(System.in);
    public PatientController controller = new PatientController();
    
    public void showOption(){
        controller.patientRegistration("Alice Tan", "230101145678", "012-3456789", "alice.tan@gmail.com");     // Age ~ 2 (Toddler)
        controller.patientRegistration("Benjamin Lee", "201230087654", "013-9876543", "benjamin.lee@gmail.com"); // Age ~ 24 (Young Adult)
        controller.patientRegistration("Catherine Ng", "150215045678", "017-2223344", "catherine.ng@gmail.com"); // Age ~ 32 (Adult)
        controller.patientRegistration("Daniel Tan", "120512126789", "016-8899776", "daniel.tan@gmail.com");     // Age ~ 35 (Young Adult)
        controller.patientRegistration("Emily Wong", "210430078945", "014-6655443", "emily.wong@gmail.com");     // Age ~ 13 (Teenager)
        controller.patientRegistration("Farhan Ahmad", "051021014567", "019-9988776", "farhan.ahmad@gmail.com"); // Age ~ 70 (Senior)
        controller.patientRegistration("Grace Lim", "220210098765", "011-7766554", "grace.lim@gmail.com");       // Age ~ 3 (Toddler)
        controller.patientRegistration("Henry Lau", "991105067890", "018-4455667", "henry.lau@gmail.com");       // Age ~ 26 (Young Adult)
        controller.patientRegistration("Isabelle Chong", "130701034567", "012-5566778", "isabelle.chong@gmail.com"); // Age ~ 11 (Child)
        controller.patientRegistration("Jason Foo", "000101078954", "013-6677889", "jason.foo@gmail.com");       // Age ~ 25 (Young Adult)

        int choice;
    do{
        System.out.println(" ===== Clinic name =====");
        //patient appointment management CRUD
        System.out.println("1. Make an registration ");
        System.out.println("2. View Patient Profile ");
        System.out.println("3. Update Profile");
        System.out.println("4. View Queue Status"); 
        System.out.println("5. Delete Patient Record");
        System.out.println("6. View report");
        System.out.println("7. Exit ");
        System.out.println("========================\n");
        
        System.out.print("Select your option: ");
        
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number between 1 and 7.");
            scanner.next();
        }
        
        choice = scanner.nextInt();
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
                viewAgeReport();
                break;
            case 7:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Pls try again.");
                break;
            }
        }while(true);
    }
    
    private void makeRegistration(){
        System.out.println(" ================ new appointment ================");
        
        String studName;
        do{
        System.out.print("Enter your name: ");
        studName = scanner.nextLine();
            if(!controller.NameValidation(studName)){
                System.out.println("Invalid name. Please try again.");
            }
        }while(!controller.NameValidation(studName));
        
        // int ic 
        String studIC;
        do{
        System.out.print("Enter your IC number ( without(-) ):");
        studIC = scanner.nextLine();
        
        if(!controller.ICvalidation(studIC)){
            System.out.println("Invalid IC. Please try again.");
        }
        
        }while(!controller.ICvalidation(studIC));
        
        //String PN
        String studPhoneNo;
        do{
        System.out.print("Enter your phone number (with (-)):");
        studPhoneNo = scanner.nextLine();
        if(!controller.PhoneValidation(studPhoneNo)){
            System.out.println("Invalid phone number. Please enter again.");
        }
        }while(!controller.PhoneValidation(studPhoneNo));
        
        String studEmail;
        do{
        System.out.print("Enter your email:");
        studEmail = scanner.nextLine();
            if(!controller.EmailValidation(studEmail)){
                System.out.println("Invalid Email. Please enter again.");
            }
        }while(!controller.EmailValidation(studEmail));
        
        System.out.print("\n\n");
        
        Patient patient = controller.patientRegistration(studName, studIC, studPhoneNo, studEmail);
        
        displayPatientDetails(patient);
        BackToMenu();
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

    BackToMenu();
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
        
            BackToMenu();
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
        BackToMenu();
    }
    
private void updateProfile() {
    System.out.print("Enter Patient ID to update: ");
    String id = scanner.nextLine();

    Patient patient = controller.findPatientByID(id);
    if (patient == null) {
        System.out.println("Patient not found.");
        BackToMenu();
        return;
    }

    System.out.print("Enter IC number for verification: ");
    String ic = scanner.nextLine();
    if (!ic.equals(patient.getIc())) {
        System.out.println("IC number does not match. Access denied.");
        BackToMenu();
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

        BackToMenu();
    }
        

    private void displayPatientDetails(Patient patient){
        System.out.println("\n=============================================================");
            System.out.println("Patient ID         : " + patient.getID());
            System.out.println("Name               : " + patient.getName());
            System.out.println("IC Number          : " + patient.getIc());
            System.out.println("Age                : " + patient.getAge());
            System.out.println("Gender             : " + patient.genderToString());
            System.out.println("State              : " + patient.getState());
            System.out.println("Phone              : " + patient.getPhoneNumber());
            System.out.println("Email              : " + patient.getEmail());
            System.out.println("Registration Date  : " + patient.getRegistrationDate());
        System.out.println("\n=============================================================");

    }
    
    public void viewReport(){
        System.out.println("1. View Medical Report.");
        System.out.println("2. View Age Report.");
        System.out.println("3. View Disease Report.");
        int reportChoice = scanner.nextInt();
        
        
    }
    public void viewAgeReport(){
        int[] ageCount = controller.calAgeGroup();
        String[] labels = {"Infant", "Toddler", "Child", "Teenager", "Young Adult", "Adult", "Senior"};
        
        int maxCount = Arrays.stream(ageCount).max().orElse(1);
        
        
        
        String[] colors = {
            "\u001B[31m", // Red
            "\u001B[33m", // Yellow
            "\u001B[32m", // Green
            "\u001B[36m", // Cyan
            "\u001B[34m", // Blue
            "\u001B[35m", // Magenta
            "\u001B[30m"  // Black
        };
        
        String RESET = "\u001B[0m";
        
        for(int i = 0; i < labels.length;i++){
            System.out.printf(colors[i] + "█" + RESET +  labels[i] + "\n");
        }
        
        System.out.println();
        // Y-axis
        for (int level = maxCount; level >= 1; level--) {
        System.out.printf("%3d |", level);
        for (int i = 0; i < ageCount.length; i++) {
            if(ageCount[i] >= level){
                System.out.print(colors[i] + "  ▍▍  " + RESET);
            }else{
                System.out.print("     ");
            }
        }
        
            System.out.println();
        }

        // X-axis
        System.out.print("    |__________________________________________");
        System.out.println();

        // Labels (first letter)
        System.out.print("   ");
        for (String label : labels) {
            System.out.print("    " + label.charAt(0));
        }
        System.out.println();
        BackToMenu();
    }

    private void BackToMenu(){
        System.out.println("< Press Enter to return to Main Menu >");
        scanner.nextLine();
        showOption();
    }
}
