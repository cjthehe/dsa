package Boundaries;

import ADT.ArrayList;
import java.time.LocalDate;
import Controller.PatientController;
import Entity.Patient;
import java.util.Scanner;

/**
 *
 * @author user
 */
public class UIPatientManagement {
    Scanner scanner = new Scanner(System.in);
    public PatientController controller = new PatientController();
    
    public void showOption(){

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
                viewReport();
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
        
        String symContinue;
        ADT.ArrayList<String> studSymptoms = new ADT.ArrayList<>();
        do{
        System.out.print("Enter your symptoms: ");
        String symptoms = scanner.nextLine();
        studSymptoms.add(symptoms);
        
        System.out.println("Have other symptoms ?");
        symContinue = scanner.nextLine();
        
        }while(symContinue.length() > 0 && Character.toLowerCase(symContinue.charAt(0)) == 'y');
        
        System.out.print("\n\n");
        
        Patient patient = controller.patientRegistration(studName, studIC, studPhoneNo, studEmail,studSymptoms, null);
        
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
            displayPatientDetails(patient);
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
                    System.out.println("Patient " + id + " has been deleted successfully");
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
            System.out.println("Symptoms           : " + patient.getPatientSymtomps());
            System.out.println("Registration Date  : " + patient.getRegistrationDate());
        System.out.println("\n=============================================================");

    }
    
    public void viewReport(){

        System.out.println("1. View Medical Report.");
        System.out.println("2. View Age Report.");
        System.out.println("3. View Disease Report.");
        System.out.println("4. View Registration Report.");
        int reportChoice = scanner.nextInt();
        scanner.nextLine();
        switch(reportChoice){
            case 1:
                viewAgeReport();
                break;
            
            case 2:
                break;
            
            case 3:
                break;
            
            case 4:
                viewRegDateReport();
                break;
            
            default:
                System.out.println("Invalid choice.");
        }
        System.out.println("");
        BackToMenu();
        
    }
    public void viewAgeReport(){
        int[] ageCount = controller.calAgeGroup();
        String[] labels = {"Infant", "Toddler", "Child", "Teenager", "Young Adult", "Adult", "Senior"};
        
        int maxCount = 1;
        for (int i = 0; i < ageCount.length; i++) {
            if (ageCount[i] > maxCount) {
                maxCount = ageCount[i];
            }
        }
        
        
        
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
        System.out.print("    |__________________________________________ Age group");
        System.out.println();

        System.out.print("   ");
        for (String label : labels) {
            System.out.print("    " + label.charAt(0));
        }
        System.out.println();
        BackToMenu();
        clearScreen();

    }

    public void viewRegDateReport(){
        int[] monthCount = controller.calRegGroup();
        String[] monthLabels = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        
        int total = 0;
        for(int i = 0; i < monthCount.length; i++){
            total += monthCount[i];
        }
        
        String[] colors = {
            "\u001B[31m", // Red
            "\u001B[33m", // Yellow
            "\u001B[32m", // Green
            "\u001B[36m", // Cyan
            "\u001B[34m", // Blue
            "\u001B[35m", // Magenta
            "\u001B[91m", // Bright Red
            "\u001B[92m", // Bright Green
            "\u001B[93m", // Bright Yellow
            "\u001B[94m", // Bright Blue
            "\u001B[95m", // Bright Magenta
            "\u001B[96m"  // Bright Cyan
        };

        String RESET = "\u001B[0m";

        System.out.print("            ____________________________________________ number of patient");
        System.out.println();
        for(int i = 0; i <12;i++ ){
            double percentage = (monthCount[i] * 100.00) / total;
            int barCount = (int)percentage;
            System.out.printf("%-10s  |",monthLabels[i]);
            
            for(int j = 0; j < barCount; j++){
                System.out.print("█");
            }
            
            System.out.printf(" %.2f%%", percentage);
            System.out.println("");
        }
        
        System.out.printf("Total patient: %d", total);
    }
    
    
    private void BackToMenu(){
        System.out.println("< Press Enter to return to Main Menu >");
        scanner.nextLine();
        showOption();
    }
    
   public void DummyData() {
        // ================= January (2) =================
        ADT.ArrayList<String> symptoms1 = new ADT.ArrayList<>();
        symptoms1.add("Fever");
        symptoms1.add("Cough");
        controller.patientRegistration("Alice Tan", "250105123456", "012-3456789", "alice.tan@gmail.com", symptoms1, LocalDate.of(2025, 1, 5));   // Infant

        ADT.ArrayList<String> symptoms2 = new ADT.ArrayList<>();
        symptoms2.add("Runny Nose");
        controller.patientRegistration("Benjamin Lee", "210120567890", "013-5678901", "ben.lee@yahoo.com", symptoms2, LocalDate.of(2025, 1, 20)); // Toddler

        // ================= February (3) =================
        ADT.ArrayList<String> symptoms3 = new ADT.ArrayList<>();
        symptoms3.add("Headache");
        controller.patientRegistration("Carmen Wong", "150202234567", "014-6789012", "carmen.wong@hotmail.com", symptoms3, LocalDate.of(2025, 2, 2)); // Child

        ADT.ArrayList<String> symptoms4 = new ADT.ArrayList<>();
        symptoms4.add("Stomach Pain");
        controller.patientRegistration("Daniel Lim", "080214876543", "016-7890123", "daniel.lim@gmail.com", symptoms4, LocalDate.of(2025, 2, 14));   // Teenager

        ADT.ArrayList<String> symptoms5 = new ADT.ArrayList<>();
        symptoms5.add("Fatigue");
        controller.patientRegistration("Elaine Ng", "000223112233", "017-8901234", "elaine.ng@yahoo.com", symptoms5, LocalDate.of(2025, 2, 23));    // Young Adult

        // ================= March (4) =================
        ADT.ArrayList<String> symptoms6 = new ADT.ArrayList<>();
        symptoms6.add("Back Pain");
        controller.patientRegistration("Felix Tan", "850305998877", "018-9012345", "felix.tan@gmail.com", symptoms6, LocalDate.of(2025, 3, 5));     // Adult

        ADT.ArrayList<String> symptoms7 = new ADT.ArrayList<>();
        symptoms7.add("Joint Pain");
        controller.patientRegistration("Grace Ho", "550312445566", "019-1234567", "grace.ho@hotmail.com", symptoms7, LocalDate.of(2025, 3, 12));    // Senior

        ADT.ArrayList<String> symptoms8 = new ADT.ArrayList<>();
        symptoms8.add("Cough");
        controller.patientRegistration("Henry Ong", "230320556677", "011-2345678", "henry.ong@yahoo.com", symptoms8, LocalDate.of(2025, 3, 20));    // Toddler

        ADT.ArrayList<String> symptoms9 = new ADT.ArrayList<>();
        symptoms9.add("Flu");
        controller.patientRegistration("Ivy Goh", "120328223344", "012-4567890", "ivy.goh@gmail.com", symptoms9, LocalDate.of(2025, 3, 28));        // Child

        // ================= April (1) =================
        ADT.ArrayList<String> symptoms10 = new ADT.ArrayList<>();
        symptoms10.add("Sore Throat");
        controller.patientRegistration("Jason Lim", "070404667788", "013-5671234", "jason.lim@hotmail.com", symptoms10, LocalDate.of(2025, 4, 4));   // Teenager

        // ================= May (5) =================
        ADT.ArrayList<String> symptoms11 = new ADT.ArrayList<>();
        symptoms11.add("Headache");
        controller.patientRegistration("Kelly Tan", "950506334455", "014-6782345", "kelly.tan@gmail.com", symptoms11, LocalDate.of(2025, 5, 6));     // Young Adult

        ADT.ArrayList<String> symptoms12 = new ADT.ArrayList<>();
        symptoms12.add("Back Pain");
        controller.patientRegistration("Leonard Chua", "740514778899", "016-7893456", "leonard.chua@yahoo.com", symptoms12, LocalDate.of(2025, 5, 14)); // Adult

        ADT.ArrayList<String> symptoms13 = new ADT.ArrayList<>();
        symptoms13.add("Chest Pain");
        controller.patientRegistration("Michelle Lee", "620521112244", "017-8904567", "michelle.lee@hotmail.com", symptoms13, LocalDate.of(2025, 5, 21)); // Senior

        ADT.ArrayList<String> symptoms14 = new ADT.ArrayList<>();
        symptoms14.add("Fever");
        controller.patientRegistration("Nicholas Yap", "250528889900", "018-9015678", "nicholas.yap@gmail.com", symptoms14, LocalDate.of(2025, 5, 28)); // Infant

        ADT.ArrayList<String> symptoms15 = new ADT.ArrayList<>();
        symptoms15.add("Cough");
        controller.patientRegistration("Olivia Chan", "210530667799", "019-1236789", "olivia.chan@yahoo.com", symptoms15, LocalDate.of(2025, 5, 30)); // Toddler

        // ================= June (3) =================
        ADT.ArrayList<String> symptoms16 = new ADT.ArrayList<>();
        symptoms16.add("Allergy");
        controller.patientRegistration("Patrick Wong", "140608223355", "011-2347890", "patrick.wong@gmail.com", symptoms16, LocalDate.of(2025, 6, 8)); // Child

        ADT.ArrayList<String> symptoms17 = new ADT.ArrayList<>();
        symptoms17.add("Flu");
        controller.patientRegistration("Queenie Lau", "060616445577", "012-3458901", "queenie.lau@hotmail.com", symptoms17, LocalDate.of(2025, 6, 16)); // Teenager

        ADT.ArrayList<String> symptoms18 = new ADT.ArrayList<>();
        symptoms18.add("Headache");
        controller.patientRegistration("Ryan Tan", "990624998800", "013-5679012", "ryan.tan@yahoo.com", symptoms18, LocalDate.of(2025, 6, 24));      // Young Adult

        // ================= July (6) =================
        ADT.ArrayList<String> symptoms19 = new ADT.ArrayList<>();
        symptoms19.add("Back Pain");
        controller.patientRegistration("Samantha Ng", "840703556677", "014-6780123", "samantha.ng@gmail.com", symptoms19, LocalDate.of(2025, 7, 3)); // Adult

        ADT.ArrayList<String> symptoms20 = new ADT.ArrayList<>();
        symptoms20.add("Fatigue");
        controller.patientRegistration("Thomas Lee", "540710889922", "016-7891234", "thomas.lee@hotmail.com", symptoms20, LocalDate.of(2025, 7, 10)); // Senior

        ADT.ArrayList<String> symptoms21 = new ADT.ArrayList<>();
        symptoms21.add("Cough");
        controller.patientRegistration("Uma Devi", "220717334466", "017-8902345", "uma.devi@yahoo.com", symptoms21, LocalDate.of(2025, 7, 17));      // Toddler

        ADT.ArrayList<String> symptoms22 = new ADT.ArrayList<>();
        symptoms22.add("Flu");
        controller.patientRegistration("Victor Tan", "120724667788", "018-9013456", "victor.tan@gmail.com", symptoms22, LocalDate.of(2025, 7, 24));  // Child

        ADT.ArrayList<String> symptoms23 = new ADT.ArrayList<>();
        symptoms23.add("Stomach Pain");
        controller.patientRegistration("Wendy Ho", "090728223344", "019-1234568", "wendy.ho@hotmail.com", symptoms23, LocalDate.of(2025, 7, 28));    // Teenager

        ADT.ArrayList<String> symptoms24 = new ADT.ArrayList<>();
        symptoms24.add("Headache");
        controller.patientRegistration("Xavier Lim", "970731445599", "011-2345679", "xavier.lim@yahoo.com", symptoms24, LocalDate.of(2025, 7, 31));  // Young Adult
    }



    
    public static void clearScreen() {
        // ANSI escape code to clear the screen and move cursor to top-left
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
