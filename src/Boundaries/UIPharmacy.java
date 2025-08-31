package Boundaries;
import java.util.Scanner;
import Controller.PharmacyController;
import Entity.Patient;
import Main.Asgm;
import ADT.AVLTree;

public class UIPharmacy {
    private Asgm asgm = new Asgm();
    private final Scanner scanner = new Scanner(System.in);
    private final PharmacyController controller = new PharmacyController();
    private final AVLTree<String, Patient> patientTree = new AVLTree<>();   
    private boolean seededPatients = false;
    public void showMenu() {  
        asgm.clearScreen();
        if (!seededPatients) {
            new UIPatientManagement().DummyData();
            seededPatients = true;
        }
        int choice;
        do {
            System.out.println("\n");
            System.out.println(" +-------------------------- Pharmacy Management System --------------------------+");
            System.out.printf(" |%22s%-35s%22s |\n", "", "1. Medicine Inventory Management", "");
            System.out.printf(" |%22s%-35s%22s |\n", "", "2. Medicine Dispensing", "");
            System.out.printf(" |%22s%-35s%22s |\n", "", "3. Stock Reordering", "");
            System.out.printf(" |%22s%-35s%22s |\n", "", "4. Supplier Management", "");
            System.out.printf(" |%22s%-35s%22s |\n", "", "5. Report", "");
            System.out.printf(" |%22s%-35s%22s |\n", "", "6. Exit", "");
            System.out.println(" +--------------------------------------------------------------------------------+");

            System.out.print("Enter choice: ");

            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                     medicineInventoryMenu();
                     break;
                case 2: 
                    medicineDispensingMenu();
                    break;
                case 3:
                    stockReorderingMenu();
                    break;
                case 4:
                    supplierManagementMenu();
                    break;
                case 5:
                    viewReports();
                    break;
                case 6:
                    System.out.println("Returning to main menu...");
                    asgm.startMenu();
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        } while (choice != 5);
    }


    private void medicineInventoryMenu() {
        int choice;
        do {
            System.out.println("\n");
            System.out.println(" +----------------------- Medicine Inventory Management -----------------------+");
            System.out.printf(" |%23s%-30s%23s |\n", "", "1. View All Medicines", "");
            System.out.printf(" |%23s%-30s%23s |\n", "", "2. Add Medicine", "");
            System.out.printf(" |%23s%-30s%23s |\n", "", "3. Edit Medicine", "");
            System.out.printf(" |%23s%-30s%23s |\n", "", "4. Delete Medicine", "");
            System.out.printf(" |%23s%-30s%23s |\n", "", "5. Back to Main Menu", "");
            System.out.println(" +-----------------------------------------------------------------------------+");
            
            System.out.print("Enter choice: ");

            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    controller.viewAllMedicines();
                    break;
                case 2:
                    controller.addMedicine();
                    break;
                case 3:
                    controller.editMedicine();
                    break;
                case 4:
                    controller.deleteMedicine();
                    break;
                case 5:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        } while (choice != 5);
    }

    private void medicineDispensingMenu() {
        int choice;
        do {
            System.out.println("\n");
            System.out.println(" +--------------------------- Medicine Dispensing ---------------------------+");
            System.out.printf(" |%20s%-35s%20s|\n", "", "1. Dispense Medicine to Patient", "");
            System.out.printf(" |%20s%-35s%20s|\n", "", "2. Back to Main Menu", "");
            System.out.println(" +---------------------------------------------------------------------------+");
            
            System.out.print("Enter choice: ");

            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    controller.dispenseMedicine(patientTree);
                    break;
                case 2:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        } while (choice != 2);
    }

    private void stockReorderingMenu() {
        int choice;
        do {
            System.out.println("\n");
            System.out.println(" +---------------------------- Stock Reordering ----------------------------+");
            System.out.printf(" |%20s%-35s%20s|\n", "", "1. View Low Stock Items", "");
            System.out.printf(" |%20s%-35s%20s|\n", "", "2. Generate Reorder Request", "");
            System.out.printf(" |%20s%-35s%20s|\n", "", "3. Track Reorder Status", "");
            System.out.printf(" |%20s%-35s%20s|\n", "", "4. Back to Main Menu", "");
            System.out.println(" +--------------------------------------------------------------------------+");
            
            System.out.print("Enter choice: ");

            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    controller.viewLowStock();
                    break;
                case 2:
                    controller.generateReorderRequest();
                    break;
                case 3:
                    controller.trackReorderStatus();
                    break;
                case 4:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (choice != 4);
    }

    private void supplierManagementMenu() {
        int choice;
        do {
            System.out.println("\n");
            System.out.println(" +--------------------------- Supplier Management ---------------------------+");
            System.out.printf(" |%20s%-35s%20s|\n", "", "1. View Suppliers", "");
            System.out.printf(" |%20s%-35s%20s|\n", "", "2. Add Supplier", "");
            System.out.printf(" |%20s%-35s%20s|\n", "", "3. Edit Supplier", "");
            System.out.printf(" |%20s%-35s%20s|\n", "", "4. Delete Supplier", "");
            System.out.printf(" |%20s%-35s%20s|\n", "", "5. Search Supplier", "");
            System.out.printf(" |%20s%-35s%20s|\n", "", "6. View Products by Supplier", "");
            System.out.printf(" |%20s%-35s%20s|\n", "", "7. Back to Main Menu", "");
            System.out.println(" +--------------------------------------------------------------------------+");
            
            System.out.print("Enter choice: ");

            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    controller.viewSuppliers();
                    break;
                case 2:
                    controller.addSupplier();
                    break;
                case 3:
                    controller.editSupplier();
                    break;
                case 4:
                    controller.deleteSupplier();
                    break;
                case 5:
                    controller.searchSupplier();        
                    break;
                case 6:
                    controller.viewProductsBySupplier();
                    break;
                case 7:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (choice != 7);
    }

    private void viewReports(){
        int choice;
        do {
            System.out.println("\n +-------------------------- View Reports -------------------------+ ");
            System.out.println();
            System.out.println("\n +--------------------------- Pharmacy Reports Menu ---------------------------+ ");
            System.out.printf(" |%20s%-34s%23s|\n", "", "1. Stock Level Report", "");
            System.out.printf(" |%20s%-34s%23s|\n", "", "2. Dispensed Medicine Report", "");
            System.out.printf(" |%20s%-34s%23s|\n", "", "3. Exit", "");
            System.out.println(" +-----------------------------------------------------------------------------+ ");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1: {
                    controller.viewStockReport();
                    showMenu();
                    break;
                }
                case 2: {
                    controller.viewDispensedReport();
                    showMenu();
                    break;
                }
                case 3:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);    

    }
}