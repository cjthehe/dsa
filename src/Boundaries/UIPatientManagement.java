package Boundaries;

import ADT.ArrayList;
import java.time.LocalDate;
import Controller.PatientController;
import Entity.Patient;
import java.util.Scanner;
import Main.Asgm;
/**
 *
 * @author user
 */
public class UIPatientManagement {
    private Asgm asgm = new Asgm();
    Scanner scanner = new Scanner(System.in);
    public PatientController controller = new PatientController();
    
    public void showOption(){

        int choice;
    
        asgm.clearScreen();
        
        System.out.println(" +-------------------------------- Clinic name --------------------------------+ ");
        System.out.printf(" |%23s%-30s%23s |\n", "", "1. Make a registration", "");
        System.out.printf(" |%23s%-30s%23s |\n", "", "2. View Patient Profile", "");
        System.out.printf(" |%23s%-30s%23s |\n", "", "3. Update Profile", "");
        System.out.printf(" |%23s%-30s%23s |\n", "", "4. View Queue Status", "");
        System.out.printf(" |%23s%-30s%23s |\n", "", "5. Delete Patient Record", "");
        System.out.printf(" |%23s%-30s%23s |\n", "", "6. View Report", "");
        System.out.printf(" |%23s%-30s%23s |\n", "", "7. Back to menu", "");
        System.out.println(" +-----------------------------------------------------------------------------+ ");

        System.out.printf("%24s Select your option: ", "");

    do{
        while (!scanner.hasNextInt()) {
            System.out.printf("%8s Invalid input! Please enter a number between 1 and 7.\n", "");
            System.out.printf("%24s Select your option: ", "");

            scanner.next();
        }
        
        choice = scanner.nextInt();
        scanner.nextLine();
                
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
                asgm.startMenu();
                break;
            default:
                System.out.printf("%21s Invalid option. Pls try again.\n\n","");
                System.out.printf("%24s Select your option: ", "");
                break;
            }
        }while(true);
    }
    
    private void makeRegistration(){
        System.out.printf("\n\n%15s ---------------- new appointment ---------------- \n\n","");
        
        String studName;
        do{
        System.out.printf("%20s Enter name: ","");
        studName = scanner.nextLine();
            if(!controller.NameValidation(studName)){
                System.out.printf("%20s Invalid name. Please try again.","");
            }
        }while(!controller.NameValidation(studName));
        
        // int ic 
        String studIC;
        do{
        System.out.printf("\n%20s Enter IC number ( without(-) ):","");
        studIC = scanner.nextLine();
        
        if(!controller.ICvalidation(studIC)){
            System.out.printf("\n%20sInvalid IC. Please try again.","");
        }
        
        }while(!controller.ICvalidation(studIC));
        
        //String PN
        String studPhoneNo;
        do{
        System.out.printf("\n%20s Enter phone number (with (-)):","");
        studPhoneNo = scanner.nextLine();
        if(!controller.PhoneValidation(studPhoneNo)){
            System.out.printf("\n%20s Invalid phone number. Please enter again.","");
        }
        }while(!controller.PhoneValidation(studPhoneNo));
        
        String studEmail;
        do{
        System.out.printf("\n%20s Enter email:","");
        studEmail = scanner.nextLine();
            if(!controller.EmailValidation(studEmail)){
                System.out.printf("\n%20s Invalid Email. Please enter again.","");
            }
        }while(!controller.EmailValidation(studEmail));
        
        String symContinue;
        ADT.ArrayList<String> studSymptoms = new ADT.ArrayList<>();
        do{
        System.out.printf("\n%20s Enter patient symptoms: ","");
        String symptoms = scanner.nextLine();
        studSymptoms.add(symptoms);
        
        System.out.printf("\n%20s Have other symptoms ? (y/n): ","");
        symContinue = scanner.nextLine();
        
        }while(symContinue.length() > 0 && Character.toLowerCase(symContinue.charAt(0)) == 'y');
        
        System.out.print("\n\n");
        
        Patient patient = controller.patientRegistration(studName, studIC, studPhoneNo, studEmail,studSymptoms, null);
        
        displayPatientDetails(patient);
        BackToMenu();
    }
    
    private void viewRecord(){
        boolean repeat;
        do{
            repeat = false;
            
            System.out.printf("%20s Enter Patient ID to search: ","");
            String id = scanner.nextLine();

            Patient patient = controller.findPatientByID(id);
            if (patient != null){
                System.out.printf("%20s Enter IC number for verification: ","");
                String ic = scanner.nextLine();

                if (ic.equals(patient.getIc())){
                    displayPatientDetails(patient); 
                }else{
                    System.out.printf("\n%16s IC number does not match. Access denied.\n","");   
                    System.out.printf("%24s Try again? (Y/N): ","");
                    String choice = scanner.nextLine();
                        if (!choice.isEmpty() && Character.toUpperCase(choice.charAt(0)) == 'Y') {                            
                            repeat = true;
                        }
                }
            }else{
                System.out.printf("\n%25s Patient not found.\n","");   
                System.out.printf("%25s Try again? (Y/N): ","");
                String choice = scanner.nextLine();
                if(choice.equalsIgnoreCase("Y")){
                    repeat = true;
                }
            }
        } while (repeat);

        BackToMenu();
    }

    
    private void deleteProfile(){        
        System.out.printf("%24s Enter Patient ID to delete: ","");
        String id = scanner.nextLine();
        
        Patient patient = controller.findPatientByID(id);
        
        if(patient == null){
            System.out.printf("%24s Patient not found.","");
        }else{
            System.out.printf("%24s Enter your IC number: ","");
            String icReqDelete = scanner.nextLine();
            
                if(icReqDelete.equals(patient.getIc())){
                    controller.deletePatientById(id);
                    System.out.printf("%20sPatient %s has been deleted successfully\n", "", id);
                }else{
                    System.out.printf("%20s IC number does not match. Record delete failed.\n","");
                }
            }
        
            BackToMenu();
        }
    
    private void viewQueue(){
        Patient nextPatient = controller.viewPatientQueue();
        
        if(nextPatient != null){
            System.out.printf("\n\n%10s---------------- Next Patient in Queue ----------------\n\n", "");
            System.out.printf("%10sPatient ID   : %s\n", "", nextPatient.getID());
            System.out.printf("%10sName         : %s\n", "", nextPatient.getName());
            System.out.printf("%10sGender       : %s\n", "", nextPatient.genderToString());
            System.out.printf("%10sPhone        : %s\n", "", nextPatient.getPhoneNumber());
            System.out.printf("%10sEmail        : %s\n", "", nextPatient.getEmail());
            System.out.printf("\n%10s---------------------------------------------------------\n\n", "");

        }else{
            System.out.printf("%10s No patients currently in queue.\n","");
        }
        BackToMenu();
    }
    
private void updateProfile() {
    System.out.printf("\n\n%10s ---------------- Information Update Center ---------------- \n\n","");
    Patient patient = null;
    String id;
    
    do{
    System.out.printf("%10s Enter Patient ID to update: ","");
    id = scanner.nextLine();

    patient = controller.findPatientByID(id);
    if (patient == null) {
        System.out.printf("\n%25s Patient not found.\n","");
        System.out.printf("%24s Try again? (Y/N): ","");
        String retry = scanner.nextLine();
            if (retry.isEmpty() || Character.toUpperCase(retry.charAt(0)) != 'Y') {
                BackToMenu();
                return;
            }
        }
    }while(patient==null);

    boolean icValidation = false;
    String ic;
    do{
        
    System.out.printf("\n%10s Enter IC number for verification: ","");
    ic = scanner.nextLine();
    if (ic.equals(patient.getIc())) {
        icValidation = true;
    }else{
        System.out.println("IC number does not match. Access denied.");
        System.out.printf("%20s Try again? (Y/N): ","");
            String retry = scanner.nextLine();
            if (retry.isEmpty() || Character.toUpperCase(retry.charAt(0)) != 'Y') {
                BackToMenu();
                return; 
            
            }
    }
    }while(!icValidation);

    boolean updating = true;

    while (updating) {
        System.out.printf(" %10s+-----------------------------------------------------------+\n", "");  
        System.out.printf(" %10s|%18s%-30s%11s|\n", "", "", "Select field to update:", "");  
        System.out.printf(" %10s|%18s%-30s%11s|\n", "", "", "1. Name", "");  
        System.out.printf(" %10s|%18s%-30s%11s|\n", "", "", "2. Phone Number", "");  
        System.out.printf(" %10s|%18s%-30s%11s|\n", "", "", "3. Email", "");  
        System.out.printf(" %10s|%18s%-30s%11s|\n", "", "", "4. Exit", "");  
        System.out.printf(" %10s+-----------------------------------------------------------+\n", "");  
        System.out.printf(" %24s  Enter choice: ", "");  


        int choice = scanner.nextInt();
        scanner.nextLine(); 

        String field = null;
        switch (choice) {
            case 1: 
                field = "name"; 
                break;
            case 2: 
                field = "phone"; 
                break;
            case 3: 
                field = "email"; 
                break;
            case 4: 
                updating = false; 
                displayPatientDetails(patient);
                continue;
            default:
                System.out.println("Invalid choice.");
                continue;
        }

        System.out.printf("\n%24s Enter new value for %s: ", "", field);
        String newValue = scanner.nextLine();
        boolean success = controller.updatePatient(id, ic, field, newValue);
        if (success) {
            System.out.printf("%24s Field updated successfully.\n","");
        } else {
            System.out.printf("\n%26s Update failed.\n\n","");
        }
    }

        BackToMenu();
    }
        

    private void displayPatientDetails(Patient patient){
        System.out.printf("\n %10s+-----------------------------------------------------------+\n", "");
        System.out.printf(" %10s| %-25s : %-29s |\n", "", "Patient ID", patient.getID());
        System.out.printf(" %10s| %-25s : %-29s |\n", "", "Name", patient.getName());
        System.out.printf(" %10s| %-25s : %-29s |\n", "", "IC Number", patient.getIc());
        System.out.printf(" %10s| %-25s : %-29d |\n", "", "Age", patient.getAge());
        System.out.printf(" %10s| %-25s : %-29s |\n", "", "Gender", patient.genderToString());
        System.out.printf(" %10s| %-25s : %-29s |\n", "", "State", patient.getState());
        System.out.printf(" %10s| %-25s : %-29s |\n", "", "Phone", patient.getPhoneNumber());
        System.out.printf(" %10s| %-25s : %-29s |\n", "", "Email", patient.getEmail());
        System.out.printf(" %10s| %-25s : %-29s |\n", "", "Symptoms", patient.getPatientSymtomps());
        System.out.printf(" %10s| %-25s : %-29s |\n", "", "Registration Date", patient.getRegistrationDate());
        System.out.printf(" %10s+-----------------------------------------------------------+\n", "");
    }

    
    public void viewReport(){

        System.out.printf("\n\n%15s ---------------- Patient Report ---------------- \n\n","");
        System.out.printf("%20s 1. View Age Report\n", "");
        System.out.printf("%20s 2. View Registration Report\n", "");
        System.out.printf("%20s 3. Back to patient menu\n", "");
        System.out.printf("\n%15s ------------------------------------------------ \n","");
        System.out.printf("%20s Enter your choice: ","");
        int reportChoice = scanner.nextInt();
        scanner.nextLine();
        switch(reportChoice){
            case 1:
                viewAgeReport();
                break;
            case 2:
                viewRegDateReport();
                break;
            case 3:
                showOption();
                break;
            default:
                System.out.printf("%20s Invalid choice.","");
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
        
        System.out.printf("\n\n +----------------------------- Patient Age Group -----------------------------+\n");

        for(int i = 0; i < labels.length;i++){
            System.out.printf(" | %s    █%s %-70s|\n", colors[i], RESET, labels[i]);
        }
        
        System.out.printf(" +------------------------------------------------------------------------------+\n", "");

        // Y-axis
        for (int level = maxCount; level >= 1; level--) {
        System.out.printf(" %3d    |", level);
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
        System.out.print("        |__________________________________________ Age group");
        System.out.println();

        System.out.print("        ");
        for (String label : labels) {
            System.out.print("    " + label.charAt(0));
        }
        System.out.println();
        BackToMenu();
    }

    public void viewRegDateReport(){
        int[] monthCount = controller.calRegGroup();
        String[] monthLabels = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        
        int total = 0;
        for(int i = 0; i < monthCount.length; i++){
            total += monthCount[i];
        }
        
//        String[] colors = {
//            "\u001B[31m", // Red
//            "\u001B[33m", // Yellow
//            "\u001B[32m", // Green
//            "\u001B[36m", // Cyan
//            "\u001B[34m", // Blue
//            "\u001B[35m", // Magenta
//            "\u001B[91m", // Bright Red
//            "\u001B[92m", // Bright Green
//            "\u001B[93m", // Bright Yellow
//            "\u001B[94m", // Bright Blue
//            "\u001B[95m", // Bright Magenta
//            "\u001B[96m"  // Bright Cyan
//        };

//        String RESET = "\u001B[0m";

        System.out.printf("\n\n -------------------- Patient Registration Date Statistic --------------------\n\n");
        System.out.print("            ____________________________________________> number of patient");
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
        
        System.out.printf("\nTotal patient: %d\n", total);
        System.out.printf("\n\n -----------------------------------------------------------------------------\n\n");

    }
    
    
    private void BackToMenu(){
        System.out.printf("%20s < Press Enter to return to Main Menu >","");
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
       
        ADT.ArrayList<String> symptoms25 = new ADT.ArrayList<>();
        symptoms25.add("Head");
        controller.patientRegistration("Xavier ng", "970731445599", "011-2345679", "xavier.lim@yahoo.com", symptoms25, LocalDate.of(2025, 7, 31));  // Young Adult
    
    }



    
    public static void clearScreen() {
        // ANSI escape code to clear the screen and move cursor to top-left
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
