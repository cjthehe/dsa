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
                System.out.println("Returning to main menu...");
                asgm.startMenu();
                break;
            default:
                System.out.printf("%21s Invalid option. Pls try again.\n\n","");
                System.out.printf("%24s Select your option: ", "");
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
        
        String faculty = null;
        do{
            showFacultyOption();
            int facultyChoice = scanner.nextInt();
            scanner.nextLine();
            switch(facultyChoice){
                case 1: 
                    faculty = "FAFB";
                break;
                
                case 2: 
                    faculty = "FOCS";
                break;
                
                case 3: 
                    faculty = "FOBE";
                break;
                
                case 4: 
                    faculty = "FCCI";
                break;
                
                case 5: 
                    faculty = "FOAS";
                break;
                
                case 6: 
                    faculty = "FOET";
                break;
                
                case 7: 
                    faculty = "FSSH";
                break;
                
                default:
                    System.out.println("Invalid faculty. Please Try Again.");
                    
            }
                
        }while(faculty == null);
        
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
        
        Patient patient = controller.patientRegistration(studName, studIC, studPhoneNo, studEmail, faculty,studSymptoms, null);
        
        displayPatientDetails(patient);
        BackToMenu();
    }
    
    private void viewRecord(){
        boolean repeat;
        do{
            repeat = false;
            
            System.out.printf("%20s Enter Patient ID to search: ","");
            String id = scanner.nextLine();
            
            String idUpCase = id.toUpperCase();
            Patient patient = controller.findPatientByID(idUpCase);
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
            System.out.printf("%10sFaculty      : %s\n", "", nextPatient.getFaculty());
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
        System.out.printf(" %10s|%18s%-30s%11s|\n", "", "", "4. Faculty", "");  
        System.out.printf(" %10s|%18s%-30s%11s|\n", "", "", "5. Exit", "");  
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
                field = "faculty";
                break;
            case 5: 
                updating = false; 
                displayPatientDetails(patient);
                continue;
            default:
                System.out.println("Invalid choice.");
                continue;
        }
        if(choice == 4){
            showFacultyOption();
            System.out.printf("\n%24s Select options for new %s: ", "", field);
            String newValue = scanner.nextLine();
            
            boolean success = controller.updatePatient(id, ic, field, newValue);
            if (success) {
                System.out.printf("\n%24s Field updated successfully.\n",""); 
            } else {
                System.out.printf("\n%26s Update failed.\n\n",""); 
            }
        }else{
            System.out.printf("\n%24s Enter new value for %s: ", "", field);
            String newValue = scanner.nextLine();
            
            boolean success = controller.updatePatient(id, ic, field, newValue);
            if (success) {
                System.out.printf("\n%24s Field updated successfully.\n","");
            } else {
                System.out.printf("\n%26s Update failed.\n\n","");
            }
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
        System.out.printf(" %10s| %-25s : %-29s |\n", "", "Faculty", patient.getFaculty());
        System.out.printf(" %10s| %-25s : %-29s |\n", "", "Symptoms", patient.getPatientSymtomps());
        System.out.printf(" %10s| %-25s : %-29s |\n", "", "Registration Date", patient.getRegistrationDate());
        System.out.printf(" %10s+-----------------------------------------------------------+\n", "");
    }

    
    public void viewReport(){

        System.out.println(" \n\n+---------------------------- Patient Report ----------------------------+");
        System.out.printf(" |%20s%-30s%20s |\n", "", "1. View Faculty Report", "");
        System.out.printf(" |%20s%-30s%20s |\n", "", "2. View Registration Report", "");
        System.out.printf(" |%20s%-30s%20s |\n", "", "3. Back to Patient Menu", "");
        System.out.println(" +-----------------------------------------------------------------------+");
        
        System.out.printf("%20s Enter your choice: ","");
        int reportChoice = scanner.nextInt();
        scanner.nextLine();
        switch(reportChoice){
            case 1:
                viewFacultyReport();
                break;
            case 2:
                System.out.printf("%20s Do you want to sort the table? (y/n)","");
                String sortChoice = scanner.nextLine();

                if (!sortChoice.isEmpty() && Character.toLowerCase(sortChoice.charAt(0)) == 'y') {
                    viewSortedRegDateReport();
                } else {
                    viewRegDateReport();
                }
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
    public void viewFacultyReport(){
        int[] facultyCount = controller.calFacultyGroup();
        String[] labels = {"FAFB", "FOCS", "FOBE", "FOCI", "FOAS", "FOET", "FSSH"};
        
        int maxCount = 1;
        for (int count : facultyCount) {
            if (count > maxCount) {
                maxCount = count;
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
        
        System.out.printf("\n\n +----------------------------- Patient Faculty Group -----------------------------+\n");

        for(int i = 0; i < labels.length;i++){
            System.out.printf(" | %s    █%s %-70s|\n", colors[i], RESET, labels[i]);
        }
        
        System.out.printf(" +------------------------------------------------------------------------------+\n", "");

        // Y-axis
        for (int level = maxCount; level >= 1; level--) {
        System.out.printf(" %3d    |", level);
        for (int i = 0; i < facultyCount.length; i++) {
            if(facultyCount[i] >= level){
                System.out.print(colors[i] + "    ▍▍   " + RESET);
            }else{
                System.out.print("        ");
            }
        }        
            System.out.println();
        }

        // X-axis
        System.out.print("        |______________________________________________________________ Fcaulty group");
        System.out.println();

        System.out.print("        ");
        for (String label : labels) {
            System.out.print("    " + label);
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
    
    
    public void viewSortedRegDateReport(){
        int[] monthCount = controller.calRegGroup();
        String[] monthLabels = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        
        int total = 0;
        for(int i = 0; i < monthCount.length; i++){
            total += monthCount[i];
        }
        
        for (int i = 0; i < monthCount.length - 1; i++) {
            for (int j = i + 1; j < monthCount.length; j++) {
                if (monthCount[j] > monthCount[i]) {
                    
                    int tempCount = monthCount[i];
                    monthCount[i] = monthCount[j];
                    monthCount[j] = tempCount;

                    String tempLabel = monthLabels[i];
                    monthLabels[i] = monthLabels[j];
                    monthLabels[j] = tempLabel;
                }
            }
        }
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
    
    public void showFacultyOption(){
        System.out.printf("\n%20s Select faculty:","");
                System.out.printf("\n%20s 1. Faculty of Accountancy, Finance & Business (FAFB)","");
                System.out.printf("\n%20s 2. Faculty of Computing and Information Technology (FOCS)","");
                System.out.printf("\n%20s 3. Faculty of the Built Environment (FOBE)","");
                System.out.printf("\n%20s 4. Faculty of Communication and Creative Industries (FCCI)","");
                System.out.printf("\n%20s 5. Faculty of Arts & Social Sciences (FOAS)","");
                System.out.printf("\n%20s 6. Faculty of Engineering & Technology (FOET)","");
                System.out.printf("\n%20s 7. Faculty of Social Sciences & Humanities (FSSH)","");
    }
   public void DummyData(){
    ADT.ArrayList<String> symptoms1 = new ADT.ArrayList<>();
    symptoms1.add("Fever");
    symptoms1.add("Cough");   // extra
    controller.patientRegistration("Alice Tan", "060105123456", "012-3456789", "alice.tan@gmail.com", "FAFB", symptoms1, LocalDate.of(2025, 1, 5));   

    ADT.ArrayList<String> symptoms2 = new ADT.ArrayList<>();
    symptoms2.add("Runny Nose");
    controller.patientRegistration("Benjamin Lee", "050120567890", "013-5678901", "ben.lee@yahoo.com", "FOCS", symptoms2, LocalDate.of(2025, 1, 20)); 

    // ================= February (3) =================
    ADT.ArrayList<String> symptoms3 = new ADT.ArrayList<>();
    symptoms3.add("Headache");
    symptoms3.add("Fatigue");   // extra
    controller.patientRegistration("Carmen Wong", "040202234567", "014-6789012", "carmen.wong@hotmail.com", "FOBE", symptoms3, LocalDate.of(2025, 2, 2)); 

    ADT.ArrayList<String> symptoms4 = new ADT.ArrayList<>();
    symptoms4.add("Stomach Pain");
    controller.patientRegistration("Daniel Lim", "030214876543", "016-7890123", "daniel.lim@gmail.com", "FCCI", symptoms4, LocalDate.of(2025, 2, 14));   

    ADT.ArrayList<String> symptoms5 = new ADT.ArrayList<>();
    symptoms5.add("Fatigue");
    symptoms5.add("Headache");   // extra
    controller.patientRegistration("Elaine Ng", "020223112233", "017-8901234", "elaine.ng@yahoo.com", "FOAS", symptoms5, LocalDate.of(2025, 2, 23));    

    // ================= March (4) =================
    ADT.ArrayList<String> symptoms6 = new ADT.ArrayList<>();
    symptoms6.add("Back Pain");
    controller.patientRegistration("Felix Tan", "010305998877", "018-9012345", "felix.tan@gmail.com", "FOET", symptoms6, LocalDate.of(2025, 3, 5));     

    ADT.ArrayList<String> symptoms7 = new ADT.ArrayList<>();
    symptoms7.add("Joint Pain");
    symptoms7.add("Back Pain");   // extra
    symptoms7.add("Fatigue");     // extra
    controller.patientRegistration("Grace Ho", "060312445566", "019-1234567", "grace.ho@hotmail.com", "FSSH", symptoms7, LocalDate.of(2025, 3, 12));    

    ADT.ArrayList<String> symptoms8 = new ADT.ArrayList<>();
    symptoms8.add("Cough");
    controller.patientRegistration("Henry Ong", "050320556677", "011-2345678", "henry.ong@yahoo.com", "FAFB", symptoms8, LocalDate.of(2025, 3, 20));    

    ADT.ArrayList<String> symptoms9 = new ADT.ArrayList<>();
    symptoms9.add("Flu");
    symptoms9.add("Sore Throat");   // extra
    controller.patientRegistration("Ivy Goh", "040328223344", "012-4567890", "ivy.goh@gmail.com", "FOCS", symptoms9, LocalDate.of(2025, 3, 28));        

    // ================= April (1) =================
    ADT.ArrayList<String> symptoms10 = new ADT.ArrayList<>();
    symptoms10.add("Sore Throat");
    controller.patientRegistration("Jason Lim", "030404667788", "013-5671234", "jason.lim@hotmail.com", "FOBE", symptoms10, LocalDate.of(2025, 4, 4));   

    // ================= May (5) =================
    ADT.ArrayList<String> symptoms11 = new ADT.ArrayList<>();
    symptoms11.add("Headache");
    symptoms11.add("Nausea");   // extra (new symptom)
    controller.patientRegistration("Kelly Tan", "020506334455", "014-6782345", "kelly.tan@gmail.com", "FCCI", symptoms11, LocalDate.of(2025, 5, 6));     

    ADT.ArrayList<String> symptoms12 = new ADT.ArrayList<>();
    symptoms12.add("Back Pain");
    controller.patientRegistration("Leonard Chua", "010514778899", "016-7893456", "leonard.chua@yahoo.com", "FOAS", symptoms12, LocalDate.of(2025, 5, 14)); 

    ADT.ArrayList<String> symptoms13 = new ADT.ArrayList<>();
    symptoms13.add("Chest Pain");
    controller.patientRegistration("Michelle Lee", "060521112244", "017-8904567", "michelle.lee@hotmail.com", "FOET", symptoms13, LocalDate.of(2025, 5, 21)); 

    ADT.ArrayList<String> symptoms14 = new ADT.ArrayList<>();
    symptoms14.add("Fever");
    symptoms14.add("Cough");   // extra
    controller.patientRegistration("Nicholas Yap", "050528889900", "018-9015678", "nicholas.yap@gmail.com", "FSSH", symptoms14, LocalDate.of(2025, 5, 28)); 

    ADT.ArrayList<String> symptoms15 = new ADT.ArrayList<>();
    symptoms15.add("Cough");
    controller.patientRegistration("Olivia Chan", "040530667799", "019-1236789", "olivia.chan@yahoo.com", "FAFB", symptoms15, LocalDate.of(2025, 5, 30)); 

    // ================= June (3) =================
    ADT.ArrayList<String> symptoms16 = new ADT.ArrayList<>();
    symptoms16.add("Allergy");
    controller.patientRegistration("Patrick Wong", "030608223355", "011-2347890", "patrick.wong@gmail.com", "FOCS", symptoms16, LocalDate.of(2025, 6, 8)); 

    ADT.ArrayList<String> symptoms17 = new ADT.ArrayList<>();
    symptoms17.add("Flu");
    symptoms17.add("Cough");   // extra
    controller.patientRegistration("Queenie Lau", "020616445577", "012-3458901", "queenie.lau@hotmail.com", "FOBE", symptoms17, LocalDate.of(2025, 6, 16)); 

    ADT.ArrayList<String> symptoms18 = new ADT.ArrayList<>();
    symptoms18.add("Headache");
    controller.patientRegistration("Ryan Tan", "010624998800", "013-5679012", "ryan.tan@yahoo.com", "FCCI", symptoms18, LocalDate.of(2025, 6, 24));      

    // ================= July (6) =================
    ADT.ArrayList<String> symptoms19 = new ADT.ArrayList<>();
    symptoms19.add("Back Pain");
    symptoms19.add("Joint Pain");   // extra
    controller.patientRegistration("Samantha Ng", "060703556677", "014-6780123", "samantha.ng@gmail.com", "FOAS", symptoms19, LocalDate.of(2025, 7, 3)); 

    ADT.ArrayList<String> symptoms20 = new ADT.ArrayList<>();
    symptoms20.add("Fatigue");
    controller.patientRegistration("Thomas Lee", "050710889922", "016-7891234", "thomas.lee@hotmail.com", "FOET", symptoms20, LocalDate.of(2025, 7, 10)); 

    ADT.ArrayList<String> symptoms21 = new ADT.ArrayList<>();
    symptoms21.add("Cough");
    symptoms21.add("Fever");   // extra
    controller.patientRegistration("Uma Devi", "040717334466", "017-8902345", "uma.devi@yahoo.com", "FSSH", symptoms21, LocalDate.of(2025, 7, 17));      

    ADT.ArrayList<String> symptoms22 = new ADT.ArrayList<>();
    symptoms22.add("Flu");
    controller.patientRegistration("Victor Tan", "030724667788", "018-9013456", "victor.tan@gmail.com", "FAFB", symptoms22, LocalDate.of(2025, 7, 24));  

    ADT.ArrayList<String> symptoms23 = new ADT.ArrayList<>();
    symptoms23.add("Stomach Pain");
    symptoms23.add("Nausea");   // extra (new symptom)
    controller.patientRegistration("Wendy Ho", "020728223344", "019-1234568", "wendy.ho@hotmail.com", "FOCS", symptoms23, LocalDate.of(2025, 7, 28));    

    ADT.ArrayList<String> symptoms24 = new ADT.ArrayList<>();
    symptoms24.add("Headache");
    symptoms24.add("Dizziness");   // extra (new symptom)
    controller.patientRegistration("Xavier Lim", "010731445599", "011-2345679", "xavier.lim@yahoo.com", "FOBE", symptoms24, LocalDate.of(2025, 7, 31));  
}



    
    public static void clearScreen() {
        // ANSI escape code to clear the screen and move cursor to top-left
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
