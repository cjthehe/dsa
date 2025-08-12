/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

import java.util.Scanner;
import Boundaries.UIPatientManagement;
import Boundaries.UIConsultation;
import Boundaries.UIDoctorManagement;

/**
 *
 * @author chanj
 */
public class Asgm {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println(" ===== Clinic Management System =====");
        System.out.println("1.Patient Management ");
        System.out.println("2.Doctor Management ");
        System.out.println("3.Consultation Management ");
        System.out.println("4.Medical Treatment Management "); 
        System.out.println("5.Pharmacy Management ");
        System.out.println("6.Exit ");
        System.out.println("========================\n");
        
        System.out.print("Select your option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\n");
        
        switch(choice){
            case 1:
                UIPatientManagement uiPatient = new UIPatientManagement();
                uiPatient.showOption();
                break;
            case 2:
                UIDoctorManagement uiDoctor = new UIDoctorManagement();
                uiDoctor.showMenu();
                break;
            case 3:
                UIConsultation uiConsultation = new UIConsultation();
                uiConsultation.showMenu();
                break;
            case 4:
                //medical treatment
                break;
            case 5:
                //pharmacy
                break;
            case 6:
                System.out.println("Thank you for using the system. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }
    
}
