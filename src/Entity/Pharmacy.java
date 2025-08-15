package Entity;

import java.time.LocalDate;

public class Pharmacy {

    // ====== Medicine Inventory ======
    public static class Medicine {
        private String medicineID;
        private String medicineName;
        private int quantityInStock;
        private double unitPrice;
        private String supplierID;

        public Medicine(String medicineID, String medicineName, int quantityInStock, double unitPrice, String supplierID) {
            this.medicineID = medicineID;
            this.medicineName = medicineName;
            this.quantityInStock = quantityInStock;
            this.unitPrice = unitPrice;
            this.supplierID = supplierID;
        }

        // Getters and setters
        public String getMedicineID() { return medicineID; }
        public String getMedicineName() { return medicineName; }
        public int getQuantityInStock() { return quantityInStock; }
        public double getUnitPrice() {return unitPrice;}
        public String getSupplierID() { return supplierID; }
        public void setMedicineName(String medicineName) {this.medicineName = medicineName;}
        public void setQuantityInStock(int qty) { this.quantityInStock = qty; }
        public void setUnitPrice(double price) {this.unitPrice = price;}
        public void setSupplierID(String supplierID) { this.supplierID = supplierID; }

        @Override
        public String toString() {
            return medicineID + " | " + medicineName + " | " + quantityInStock + " | " + unitPrice;
        }
    }

    // ====== Stock Reordering ======
    public static class Reorder {
        private String reorderID;
        private String medicineID;
        private int reorderQuantity;
        private LocalDate reorderDate;
        private String reorderStatus;

        public Reorder(String reorderID, String medicineID, int reorderQuantity,
                       LocalDate reorderDate, String reorderStatus) {
            this.reorderID = reorderID;
            this.medicineID = medicineID;
            this.reorderQuantity = reorderQuantity;
            this.reorderDate = reorderDate;
            this.reorderStatus = reorderStatus;
        }

        public String getReorderID() { return reorderID; }
        public String getMedicineID() { return medicineID; }
        public int getReorderQuantity() { return reorderQuantity; }
        public LocalDate getReorderDate() { return reorderDate; }
        public String getReorderStatus() { return reorderStatus; }
    }

    // ====== Medicine Dispensing ======
    public static class Dispense {
        private String dispenseID;
        private String patientID;
        private String medicineID;
        private int quantityDispensed;
        private LocalDate dispenseDate;
        private String pharmacistID;

        public Dispense(String dispenseID, String patientID, String medicineID,
                        int quantityDispensed, LocalDate dispenseDate, String pharmacistID) {
            this.dispenseID = dispenseID;
            this.patientID = patientID;
            this.medicineID = medicineID;
            this.quantityDispensed = quantityDispensed;
            this.dispenseDate = dispenseDate;
            this.pharmacistID = pharmacistID;
        }

        public String getDispenseID() { return dispenseID; }
        public String getPatientID() { return patientID; }
        public String getMedicineID() { return medicineID; }
        public int getQuantityDispensed() { return quantityDispensed; }
        public LocalDate getDispenseDate() { return dispenseDate; }
        public String getPharmacistID() { return pharmacistID; }
    }

    // ====== Supplier Management ======
    public static class Supplier {
        private String supplierID;
        private String supplierName;
        private String contactNumber;

        public Supplier(String supplierID, String supplierName, String contactNumber) {
            this.supplierID = supplierID;
            this.supplierName = supplierName;
            this.contactNumber = contactNumber;
        }

        public String getSupplierID() { return supplierID; }
        public String getSupplierName() { return supplierName; }
        public String getContactNumber() { return contactNumber; }
        public void setSupplierName(String name) { this.supplierName = name; }
        public void setContactNumber(String contact) { this.contactNumber = contact; }

        @Override
        public String toString() {
            return supplierID + " | " + supplierName + " | " + contactNumber;
        }
    }

}