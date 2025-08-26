package Controller;

import java.util.Scanner;
import java.time.LocalDate;
import ADT.HashMap;
import ADT.AVLTree;
import Entity.Patient;
import Entity.Pharmacy;

public class PharmacyController {
	private final HashMap<String, Pharmacy.Medicine> medicines = new HashMap<>(1000);
	private final HashMap<String, Integer> dispenseCounts = new HashMap<>(256);
	private final HashMap<String, HashMap<String, Integer>> monthlyDispense = new HashMap<>(64); // yyyy-MM -> (medicineId -> qty)
	private final HashMap<String, String> reorders = new HashMap<>(256);
	private final HashMap<String, Pharmacy.Supplier> supplierMap = new HashMap<>(256);
	private final Scanner scanner = new Scanner(System.in);
	private int medicineCounter = 1;
	private int supplierCounter = 1;

	public PharmacyController() {
		dummyData();
	}

	public void dummyData() {
		// Suppliers
		supplierMap.put("S0001", new Pharmacy.Supplier("S0001", "MediSupply", "012-3456789"));
		supplierMap.put("S0002", new Pharmacy.Supplier("S0002", "HealthSource", "013-9876543"));
		supplierMap.put("S0003", new Pharmacy.Supplier("S0003", "PharmaOne", "011-22223333"));
		supplierCounter = 4;

		// Medicines
		medicines.put("M0001", new Pharmacy.Medicine("M0001", "Paracetamol", 50, 3.50, "S0001"));
		medicines.put("M0002", new Pharmacy.Medicine("M0002", "Amoxicillin", 30, 12.90, "S0002"));
		medicines.put("M0003", new Pharmacy.Medicine("M0003", "Ibuprofen", 8, 5.20, "S0001"));
		medicines.put("M0004", new Pharmacy.Medicine("M0004", "Vitamin C", 100, 0.80, "S0003"));
		medicines.put("M0005", new Pharmacy.Medicine("M0005", "Cough Syrup", 15, 9.99, "S0002"));
		medicines.put("M0006", new Pharmacy.Medicine("M0006", "Cough ", 5, 9.99, "S0002"));
		medicineCounter = 6; // next id will be M0006

		// Initial dispensed counts (aggregate)
		dispenseCounts.put("M0001", 120);
		dispenseCounts.put("M0002", 90);
		dispenseCounts.put("M0003", 150);
		dispenseCounts.put("M0004", 60);

		// Seed monthly breakdown for current month
		String monthKey = java.time.LocalDate.now().toString().substring(0, 7);
		HashMap<String, Integer> initMonth = new HashMap<>(32);
		initMonth.put("M0001", 120);
		initMonth.put("M0002", 90);
		initMonth.put("M0003", 150);
		initMonth.put("M0004", 60);
		monthlyDispense.put(monthKey, initMonth);

		// Seed previous month breakdown
		String prevMonthKey = java.time.LocalDate.now().minusMonths(1).toString().substring(0, 7);
		HashMap<String, Integer> prevMonth = new HashMap<>(32);
		prevMonth.put("M0001", 80);
		prevMonth.put("M0002", 110);
		prevMonth.put("M0003", 40);
		prevMonth.put("M0004", 75);
		prevMonth.put("M0005", 20);
		monthlyDispense.put(prevMonthKey, prevMonth);
	}

	// ------------------ 1. Medicine Inventory Management ------------------
	public void addMedicine() {
		String choice;
		do {
			System.out.println("\n");
			System.out.println("================================");
			System.out.println("           Add Medicine         ");
			System.out.println("================================");
			String medicineID = "M" + String.format("%04d", medicineCounter);
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
				medicineCounter++; // increment only after successful add
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
		final String RESET = "\u001B[0m";
		final String RED = "\u001B[31m";
		final int[] totalStock = {0};
		medicines.forEach((id, med) -> totalStock[0] += med.getQuantityInStock());
		if (totalStock[0] == 0) {
			System.out.println("No stock to report.");
			System.out.println("Press Enter to return to Main Menu");
			scanner.nextLine();
			return;
		}
		System.out.print("            ______________________________________________________ stock level (quantity)\n");
		medicines.forEach((id, med) -> {
			double percentage = (med.getQuantityInStock() * 100.0) / totalStock[0];
			int barCount = (int) percentage;
			System.out.printf("%-12s |", med.getMedicineName());
			for (int j = 0; j < barCount; j++) {
				if (med.getQuantityInStock() < 10) {
					System.out.print(RED + "█" + RESET);
				} else {
					System.out.print("█");
				}
			}
			System.out.printf(" %d (%.2f%%)\n", med.getQuantityInStock(), percentage);
		});
		System.out.printf("\nTotal Stock Across All Medicines: %d\n", totalStock[0]);
		System.out.println("\nLegend:");
		System.out.println("█ = Stock Level");
		System.out.println(RED + "█" + RESET + " = Low Stock (below 10 units)");
		System.out.println("--------------------------------------------------------------------------------");        
		System.out.println("Press Enter to return to Main Menu");
		scanner.nextLine();
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

			// Track monthly dispense counts
			String monthKey = LocalDate.now().toString().substring(0, 7); // yyyy-MM
			HashMap<String, Integer> monthMap = monthlyDispense.get(monthKey);
			if (monthMap == null) {
				monthMap = new HashMap<>(32);
				monthlyDispense.put(monthKey, monthMap);
			}
			Integer mcurr = monthMap.get(id);
			monthMap.put(id, (mcurr == null ? 0 : mcurr) + qty);

			System.out.println("Medicine dispensed to " + patient.getName() + ".\n");
			System.out.print("Do you want to dispense another medicine? (Y/N): ");
			choice = scanner.nextLine();
		} while (choice.equalsIgnoreCase("Y"));
	}

	public void viewDispensedReport() {
		System.out.print("Enter month (yyyy-MM): ");
		String monthKey = scanner.nextLine().trim();
		HashMap<String, Integer> monthMap = monthlyDispense.get(monthKey);
		if (monthMap == null || monthMap.isEmpty()) {
			System.out.println("No dispense data for " + monthKey + ".");
			return;
		}
		final int[] totalDispensed = {0};
		monthMap.forEach((mid, qty) -> totalDispensed[0] += qty);
		final String[] maxId = {null};
		final int[] maxQty = {-1};
		monthMap.forEach((mid, qty) -> {
			if (qty > maxQty[0]) { maxQty[0] = qty; maxId[0] = mid; }
		});
		System.out.println("============= Medical Dispense Report for " + monthKey + " =============");
		System.out.println("Medicine         | Dispense Count | Percentage | Chart");
		System.out.println("-----------------------------------------------------------");
		monthMap.forEach((mid, qty) -> {
			Pharmacy.Medicine med = medicines.get(mid);
			String name = med != null ? med.getMedicineName() : mid;
			double percentage = (qty * 100.0) / totalDispensed[0];
			System.out.printf("%-15s | %-14d | %8.2f%% | ", name, qty, percentage);
			int barCount = (int) percentage;
			for (int j = 0; j < barCount; j++) {
				System.out.print("█");
			}
			System.out.println();
		});
		System.out.printf("\nTotal Medicines Dispensed: %d\n", totalDispensed[0]);
		if (maxId[0] != null) {
			Pharmacy.Medicine med = medicines.get(maxId[0]);
			String name = med != null ? med.getMedicineName() : maxId[0];
			System.out.println("Most Dispensed Medicine: " + name + " (" + maxQty[0] + " units)");
		}
        System.out.println("--------------------------------------------------------------------------------");        
		System.out.println("Press Enter to return to Main Menu");
		scanner.nextLine();
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
