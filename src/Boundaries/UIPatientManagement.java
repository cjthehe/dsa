/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundaries;

import Controller.PatientController;
import java.util.Scanner;

/**
 *
 * @author user
 */
public class UIPatientManagement {
    Scanner scanner = new Scanner(System.in);
    PatientController controller = new PatientController();
    public void showOption(){
        
        System.out.println(" ===== Clinic name =====");
        //patient appointment management CRUD
        System.out.println("1.Make an appointment ");
        System.out.println("2.View my/all appointment ");
        System.out.println("3.Reschedule appointment ");
        System.out.println("4.Cancel appointment"); 
        System.out.println("5.Clear my appointment history");
        System.out.println("6.Exit ");
        System.out.println("========================\n");
        
        System.out.print("Select your option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\n");
        
        switch(choice){
            case 1:
                makeAppointment();
                break;
            case 2:
                break;
//            case 3:
//                break;
//            case 4:
//                break;
//            case 5:
//                break;
            case 6:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Pls try again.");
                break;
        }
    }
    
    private void makeAppointment(){
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
}
