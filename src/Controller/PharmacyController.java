package Controller;

import java.util.Scanner;
import ADT.HashMap;
import ADT.AVLTree;
import Entity.Patient;
import Entity.Pharmacy;

public class PharmacyController {
	private final HashMap<String, Pharmacy.Medicine> medicines = new HashMap<>(1000);
	private final HashMap<String, Integer> dispenseCounts = new HashMap<>(256);
	private final HashMap<String, String> reorders = new HashMap<>(256);
	private final HashMap<String, Pharmacy.Supplier> supplierMap = new HashMap<>(256);
	private final Scanner scanner = new Scanner(System.in);
	private int medicineCounter = 1;
	private int supplierCounter = 1;

	// ------------------ 1. Medicine Inventory Management ------------------
	public void addMedicine() {
		String choice;
		do {
			System.out.println("\n");
			System.out.println("================================");
			System.out.println("           Add Medicine         ");
			System.out.println("================================");
			String medicineID = "M" + String.format("%04d", medicineCounter++);
			System.out.println("Medicine ID: " + medicineID);
			System.out.print("Enter Medicine Name: ");
			String name = scanner.nextLine();
			System.out.print("Enter Quantity: ");
			int quantity = Integer.parseInt(scanner.nextLine());
			System.out.print("Enter Price: ");
			double price = Double.parseDouble(scanner.nextLine());
			System.out.print("Enter Supplier ID: ");
			String supplierId = scanner.nextLine();
            Pharmacy.Supplier sup = supplierMap.get(supplierId);
			if (sup != null) {
				System.out.println("Supplier ID found!");
                medicines.put(medicineID, new Pharmacy.Medicine(medicineID, name, quantity, price, supplierId));
			    System.out.println("Medicine added successfully.\n");
			}
			else{
				System.out.println("Supplier ID not found!");
				System.out.println("Press Enter to return to Medicine Management Menu");
				scanner.nextLine();
				return;
			}
			System.out.print("Do you want to add more medicine? (Y/N): ");
			choice = scanner.nextLine();
		} while (choice.equalsIgnoreCase("Y"));
	}

	public void viewAllMedicines() {
		System.out.println("\n");
		System.out.println("=====================================================================================");
		System.out.println("                                  Medicine Inventory                                 ");
		System.out.println("=====================================================================================");
		final String headerFormat = "%-10s | %-28s | %13s | %5s | %-12s%n";
		final String rowFormat = "%-11s | %-28s | %17d | %5.2f | %-12s%n";
		System.out.printf(headerFormat, "Medicine ID", "Medicine Name", "Quantity In Stock", "Price", "Supplier ID");
		System.out.println("=====================================================================================");
		final int[] count = {0};
		medicines.forEach((id, med) -> {
			System.out.printf(rowFormat, med.getMedicineID(), med.getMedicineName(), med.getQuantityInStock(), med.getUnitPrice(), med.getSupplierID());
			count[0]++;
		});
		if (count[0] == 0) {
			System.out.println("(no medicines)");
		}
		System.out.println("-------------------------------------------------------------------------------------");
		System.out.println("Press Enter to return to Main Menu");
		scanner.nextLine();
	}

	public void editMedicine() {
		String choice;
		do {
			System.out.print("Enter Medicine ID to edit: ");
			String id = scanner.nextLine();

			Pharmacy.Medicine med = medicines.get(id);
			if (med != null) {
				System.out.println("Current Details: " + med);

				System.out.print("Enter new name (leave blank to keep current): ");
				String name = scanner.nextLine();
				if (!name.isEmpty()) med.setMedicineName(name);

				System.out.print("Enter new quantity (-1 to keep current): ");
				int quantity = Integer.parseInt(scanner.nextLine());
				if (quantity >= 0) med.setQuantityInStock(quantity);

				System.out.print("Enter new price (-1 to keep current): ");
				double price = Double.parseDouble(scanner.nextLine());
				if (price >= 0) med.setUnitPrice(price);

				medicines.put(id, med); // update
				System.out.println("Medicine updated successfully!");
			} else {
				System.out.println("Medicine ID not found!");
			}

			System.out.print("Do you want to edit another medicine? (Y/N): ");
			choice = scanner.nextLine();
		} while (choice.equalsIgnoreCase("Y"));
	}

	public void deleteMedicine() {
		String choice;
		do {
			System.out.print("Enter Medicine ID to delete: ");
			String id = scanner.nextLine();

			Pharmacy.Medicine removed = medicines.remove(id);
			if (removed != null) {
				System.out.println("Medicine deleted successfully!");
			} else {
				System.out.println("Medicine ID not found!");
			}

			System.out.println("\nRemaining Medicines: ");
			System.out.println("========================================================");
			System.out.println("Medicine ID | Medicine Name | Quantity In Stock | Price");
			System.out.println("========================================================");
			medicines.forEach((mid, med) -> System.out.println(med));

			System.out.print("Do you want to delete another medicine? (Y/N): ");
			choice = scanner.nextLine();
		} while (choice.equalsIgnoreCase("Y"));
	}

	public void viewStockReport() {
		System.out.println("\n--- Stock Level Report ---");
		System.out.println("Medicine ID | Medicine Name | Quantity In Stock | Price");
		medicines.forEach((id, med) -> System.out.println(med));
	}

	// ------------------ 2. Medicine Dispensing ------------------
	public void dispenseMedicine(AVLTree<String, Patient> patientTree) {
		String choice;
		do {
			System.out.print("Enter Patient ID: ");
			String patientId = scanner.nextLine();

			Patient patient = patientTree.search(patientId);
			if (patient == null) {
				System.out.println("Patient not found.");
				return;
			}

			System.out.print("Enter Medicine ID to dispense: ");
			String id = scanner.nextLine();
			if (!medicines.containsKey(id)) {
				System.out.println("Medicine not found.");
				return;
			}

			System.out.print("Enter Quantity to dispense: ");
			int qty = Integer.parseInt(scanner.nextLine());

			Pharmacy.Medicine med = medicines.get(id);
			if (med.getQuantityInStock() < qty) {
				System.out.println("Not enough stock.");
				return;
			}

			med.setQuantityInStock(med.getQuantityInStock() - qty);
			medicines.put(id, med); // update

			Integer current = dispenseCounts.get(id);
			int newTotal = (current == null ? 0 : current) + qty;
			dispenseCounts.put(id, newTotal);

			System.out.println("Medicine dispensed to " + patient.getName() + ".\n");
			System.out.print("Do you want to dispense another medicine? (Y/N): ");
			choice = scanner.nextLine();
		} while (choice.equalsIgnoreCase("Y"));
	}

	public void viewDispensedReport() {
		System.out.println("\n--- Dispensed Medicines Report ---");
		if (dispenseCounts.isEmpty()) {
			System.out.println("No medicines dispensed.");
			return;
		}
		dispenseCounts.forEach((id, qty) -> System.out.println(id + " | Qty: " + qty));
	}

	// ------------------ 3. Stock Reordering ------------------
	public void viewLowStock() {
		System.out.println("\n--- Low Stock Items ---");
		final boolean[] found = {false};
		medicines.forEach((mid, med) -> {
			if (med.getQuantityInStock() < 10) {
				System.out.println(med.getMedicineID() + " - " + med.getMedicineName() + " | Quantity: " + med.getQuantityInStock());
				found[0] = true;
			}
		});
		if (!found[0]) {
			System.out.println("No low stock items.");
		}
        System.out.println("Press Enter to return to Main Menu");
		scanner.nextLine();
	}

	public void generateReorderRequest() {
		System.out.print("Enter Medicine ID to reorder: ");
		String id = scanner.nextLine();
		if (!medicines.containsKey(id)) {
			System.out.println("Medicine not found.");
			return;
		}

		System.out.print("Enter Quantity to reorder: ");
		int qty = Integer.parseInt(scanner.nextLine());

		Pharmacy.Medicine med = medicines.get(id);  //get the medicine
		med.setQuantityInStock(med.getQuantityInStock() + qty);
		medicines.put(id, med);
		reorders.put(id, "COMPLETED");

		System.out.println("Reorder processed. New quantity: " + med.getQuantityInStock() + "\n");
        System.out.println("Press Enter to return to Main Menu");
		scanner.nextLine();
	}

	public void trackReorderStatus() {
		System.out.println("\n--- Reorder Status ---");
		if (reorders.isEmpty()) {
			System.out.println("No reorder requests.");
			return;
		}
		reorders.forEach((mid, status) -> System.out.println("Medicine ID: " + mid + " | Status: " + status));
        System.out.println("Press Enter to return to Main Menu");
		scanner.nextLine();
	}

	// ------------------ 4. Supplier Management ------------------
	public void addSupplier() {
		String choice;
		do {
			System.out.println("\n");
			System.out.println("================================");
			System.out.println("           Add Supplier         ");
			System.out.println("================================");

			String supplierID = "S" + String.format("%04d", supplierCounter++);
			System.out.println("Supplier ID: " + supplierID);

			System.out.print("Enter Supplier Name: ");
			String name = scanner.nextLine();
			System.out.print("Enter Supplier Contact: ");
			String contact = scanner.nextLine();

			supplierMap.put(supplierID, new Pharmacy.Supplier(supplierID, name, contact));
			System.out.println("Supplier added successfully!\n");

			System.out.print("Do you want to add another supplier? (Y/N): ");
			choice = scanner.nextLine();
		} while (choice.equalsIgnoreCase("Y"));
	}

	public void viewSuppliers() {
		System.out.println("\n");
		System.out.println("=================================================================================");
		System.out.println("                                 Supplier List                                   ");
		System.out.println("=================================================================================");
		final String headerFormat = "%-12s | %-30s | %-20s%n";
		final String rowFormat = "%-12s | %-30s | %-20s%n";
		System.out.printf(headerFormat, "Supplier ID", "Supplier Name", "Contact");
		System.out.println("=================================================================================");
		final int[] count = {0};
		supplierMap.forEach((id, sup) -> {
			System.out.printf(rowFormat, sup.getSupplierID(), sup.getSupplierName(), sup.getContactNumber());
			count[0]++;
		});
		if (count[0] == 0) {
			System.out.println("(no suppliers)");
		}
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println("Press Enter to return to Main Menu");
		scanner.nextLine();
	}

	public void editSupplier() {
		String choice;
		do {
			System.out.print("Enter Supplier ID to edit: ");
			String id = scanner.nextLine();

			Pharmacy.Supplier sup = supplierMap.get(id);
			if (sup != null) {
				System.out.println("Current Details: " + sup);

				System.out.print("Enter new name (leave blank to keep current): ");
				String name = scanner.nextLine();
				if (!name.isEmpty()) sup.setSupplierName(name);

				System.out.print("Enter new contact (leave blank to keep current): ");
				String contact = scanner.nextLine();
				if (!contact.isEmpty()) sup.setContactNumber(contact);

				supplierMap.put(id, sup);
				System.out.println("Supplier updated successfully!");
			} else {
				System.out.println("Supplier ID not found!");
			}

			System.out.print("Do you want to edit another supplier? (Y/N): ");
			choice = scanner.nextLine();
		} while (choice.equalsIgnoreCase("Y"));
	}

	public void deleteSupplier() {
		String choice;
		do {
			System.out.print("Enter Supplier ID to delete: ");
			String id = scanner.nextLine();

			Pharmacy.Supplier removed = supplierMap.remove(id);
			if (removed != null) {
				System.out.println("Supplier deleted successfully!");
			} else {
				System.out.println("Supplier ID not found!");
			}

			System.out.println("\nRemaining Suppliers:");
			System.out.println("========================================================");
			System.out.println("Supplier ID | Supplier Name         | Contact");
			System.out.println("========================================================");
			supplierMap.forEach((sid, sup) -> System.out.println(sup));

			System.out.print("Do you want to delete another supplier? (Y/N): ");
			choice = scanner.nextLine();
		} while (choice.equalsIgnoreCase("Y"));
	}

	public void searchSupplier() {
		System.out.print("Enter Supplier Name to search: ");
		String searchName = scanner.nextLine().toLowerCase();

		final boolean[] found = {false};
		supplierMap.forEach((id, sup) -> {
			String n = sup.getSupplierName() == null ? "" : sup.getSupplierName();
			if (n.toLowerCase().contains(searchName)) {
				System.out.println(sup);
				found[0] = true;
			}
		});

		if (!found[0]) {
			System.out.println("No supplier found with that name.");
		}
	}

	public void viewProductsBySupplier() {
		System.out.print("Enter Supplier ID: ");
		String id = scanner.nextLine();

		Pharmacy.Supplier sup = supplierMap.get(id);
		if (sup == null) {
			System.out.println("Supplier ID not found.");
			return;
		}
		System.out.println("\nProducts supplied by: " + sup.getSupplierName() + " (" + sup.getSupplierID() + ")");
		final String headerFormat = "%-10s | %-28s | %13s | %5s%n";
		final String rowFormat = "%-11s | %-28s | %17d | %5.2f%n";
		System.out.printf(headerFormat, "Medicine ID", "Medicine Name", "Quantity In Stock", "Price");
		System.out.println("=================================================================================");
		final int[] count = {0};
		medicines.forEach((mid, med) -> {
			if (id.equals(med.getSupplierID())) {
				System.out.printf(rowFormat, med.getMedicineID(), med.getMedicineName(), med.getQuantityInStock(), med.getUnitPrice());
				count[0]++;
			}
		});
		if (count[0] == 0) {
			System.out.println("(no products for this supplier)");
		}
		System.out.println("--------------------------------------------------------------------------------");
	}
}
