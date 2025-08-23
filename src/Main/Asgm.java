package Main;

import java.util.Scanner;
import Boundaries.UIPatientManagement;
import Boundaries.UIConsultation;
import Boundaries.UIDoctorManagement;
import Boundaries.UIPharmacy;
import Boundaries.UIMedicalTreatment;

/**
 *
 * @author chanj
 */
public class Asgm {

    public static void main(String[] args) {
        Asgm asgm = new Asgm();
        asgm.startMenu();  // directly start main menu
    }

    public void startMenu() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println(" +------------------------- Clinic Management System -------------------------+ ");
        System.out.printf(" |%23s%-30s%23s|\n", "", "1. Patient Management", "");
        System.out.printf(" |%23s%-30s%23s|\n", "", "2. Doctor Management", "");
        System.out.printf(" |%23s%-30s%23s|\n", "", "3. Consultation Management", "");
        System.out.printf(" |%23s%-30s%22s|\n", "", "4. Medical Treatment Management", "");
        System.out.printf(" |%23s%-30s%23s|\n", "", "5. Pharmacy Management", "");
        System.out.printf(" |%23s%-30s%23s|\n", "", "6. Exit", "");
        System.out.println(" +----------------------------------------------------------------------------+ ");
        
        System.out.printf("%24s Select your option: "," ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.println("");
        
        switch(choice){
            case 1:
                UIPatientManagement uiPatient = new UIPatientManagement();
                uiPatient.DummyData();
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
                UIMedicalTreatment uiTreatment = new UIMedicalTreatment();
                uiTreatment.showMenu();
                break;
            case 5:
                UIPharmacy uiPharmacy = new UIPharmacy();
                uiPharmacy.showMenu();
                break;
            case 6:
                System.out.printf("%18sThank you for using the system. Goodbye!\n\n","");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }
    
    public void clearScreen(){
        for(int i = 0; i < 12; i++){  // instead of 20
            System.out.println("");
        }
    }
    
}
