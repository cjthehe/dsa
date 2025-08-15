package Boundaries;
import java.util.Scanner;
import Controller.PharmacyController;
import Entity.Patient;
import ADT.AVLTree;

public class UIPharmacy {

    private final Scanner scanner = new Scanner(System.in);
    private final PharmacyController controller = new PharmacyController();
    private final AVLTree<String, Patient> patientTree = new AVLTree<>();   
    public void showMenu() {  
        int choice;
        do {
            System.out.println("\n");
            System.out.println("========================================================");
            System.out.println("               Pharmacy Management System               ");
            System.out.println("========================================================");
            System.out.println("1. Medicine Inventory Management");
            System.out.println("2. Medicine Dispensing");
            System.out.println("3. Stock Reordering");
            System.out.println("4. Supplier Management");
            System.out.println("5. Exit");
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
                    System.out.println("Exiting Pharmacy System...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        } while (choice != 5);
    }

    // ================== SUB MENUS ==================

    private void medicineInventoryMenu() {
        int choice;
        do {
            System.out.println("\n");
            System.out.println("========================================================");
            System.out.println("              Medicine Inventory Management             ");
            System.out.println("========================================================");
            System.out.println("1. View All Medicines");
            System.out.println("2. Add Medicine");
            System.out.println("3. Edit Medicine");
            System.out.println("4. Delete Medicine");
            System.out.println("5. View Stock Level Report");
            System.out.println("6. Back to Main Menu");
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
                    controller.viewStockReport();
                    break;
                case 6:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        } while (choice != 6);
    }

    private void medicineDispensingMenu() {
        int choice;
        do {
            System.out.println("\n");
            System.out.println("========================================================");
            System.out.println("                   Medicine Dispensing                  ");
            System.out.println("========================================================");
            System.out.println("1. Dispense Medicine to Patient");
            System.out.println("2. View Dispensed Medicines Report");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter choice: ");

            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    controller.dispenseMedicine(patientTree);
                    break;
                case 2:
                    controller.viewDispensedReport();
                    break;
                case 3:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        } while (choice != 3);
    }

    private void stockReorderingMenu() {
        int choice;
        do {
            System.out.println("\n");
            System.out.println("========================================================");
            System.out.println("                   Stock Reordering                  ");
            System.out.println("========================================================");
            System.out.println("1. View Low Stock Items");
            System.out.println("2. Generate Reorder Request");
            System.out.println("3. Track Reorder Status");
            System.out.println("4. Back to Main Menu");
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
            System.out.println("========================================================");
            System.out.println("                   Supplier Management                  ");
            System.out.println("========================================================");
            System.out.println("1. View Suppliers");
            System.out.println("2. Add Supplier");
            System.out.println("3. Edit Supplier");
            System.out.println("4. Delete Supplier");
            System.out.println("5. Search Supplier");
            System.out.println("6. View Products by Supplier");
            System.out.println("7. Back to Main Menu");
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

    public static void main(String[] args) {
        new UIPharmacy().showMenu();
    }
}