package Controller;

import ADT.Graph;
import ADT.ArrayList;
import ADT.HashMap;
import Entity.Consultation;
import Entity.Doctor;
import Entity.Patient;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for generating graph-based consultation reports
 */
public class ConsultationReportController {
    private ConsultationController consultationController;
    private DoctorController doctorController;
    private PatientController patientController;
    
    public ConsultationReportController() {
        this.consultationController = new ConsultationController();
        this.doctorController = new DoctorController();
        this.patientController = new PatientController();
    }
    
    /**
     * 1. DOCTOR-PATIENT RELATIONSHIP REPORT
     * Creates a graph where:
     * - Nodes = Doctors + Patients
     * - Edges = Consultations between doctors and patients
     */
    
    /**
     * Build the doctor-patient relationship graph
     */
    public Graph<String> buildDoctorPatientGraph() {
        Graph<String> graph = new Graph<>();
        
        // Get all consultations
        ArrayList<Consultation> allConsultations = getAllConsultations();
        
        for (int i = 0; i < allConsultations.size(); i++) {
            Consultation consultation = allConsultations.get(i);
            String doctorId = consultation.getDoctorId();
            String patientId = consultation.getPatientId();
            
            // Add doctor and patient as vertices if not already present
            if (!graph.hasVertex(doctorId)) {
                graph.addVertex(doctorId);
            }
            if (!graph.hasVertex(patientId)) {
                graph.addVertex(patientId);
            }
            
            // Add edge from doctor to patient (consultation relationship)
            if (!graph.hasEdge(doctorId, patientId)) {
                graph.addEdge(doctorId, patientId);
            }
        }
        
        return graph;
    }
    
    /**
     * Show all patients connected to a specific doctor this month
     */
    public ArrayList<String> getPatientsForDoctorThisMonth(String doctorId) {
        ArrayList<String> patients = new ArrayList<>();
        Graph<String> graph = buildDoctorPatientGraph();
        
        if (!graph.hasVertex(doctorId)) {
            return patients; // Doctor not found
        }
        
        // Get all neighbors (patients) of the doctor
        ArrayList<String> neighbors = (ArrayList<String>) graph.neighborsOf(doctorId);
        
        // Filter for consultations this month
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);
        
        for (int i = 0; i < neighbors.size(); i++) {
            String patientId = neighbors.get(i);
            
            // Check if there are consultations this month between this doctor and patient
            ArrayList<Consultation> consultations = consultationController.getConsultationsByPatient(patientId);
            boolean hasConsultationThisMonth = false;
            
            for (int j = 0; j < consultations.size(); j++) {
                Consultation c = consultations.get(j);
                if (c.getDoctorId().equals(doctorId) && 
                    c.getAppointmentDateTime().isAfter(startOfMonth) && 
                    c.getAppointmentDateTime().isBefore(endOfMonth)) {
                    hasConsultationThisMonth = true;
                    break;
                }
            }
            
            if (hasConsultationThisMonth) {
                patients.add(patientId);
            }
        }
        
        return patients;
    }
    
    /**
     * Find patients who consulted multiple doctors
     */
    public ArrayList<String> getPatientsWithMultipleDoctors() {
        ArrayList<String> patientsWithMultipleDoctors = new ArrayList<>();
        Graph<String> graph = buildDoctorPatientGraph();
        
        // Get all vertices (doctors and patients)
        ArrayList<String> vertices = getAllVertices(graph);
        
        for (int i = 0; i < vertices.size(); i++) {
            String vertex = vertices.get(i);
            
            // Check if this is a patient (starts with 'P')
            if (vertex.startsWith("P")) {
                // Count how many doctors this patient has consulted
                ArrayList<String> doctors = (ArrayList<String>) graph.neighborsOf(vertex);
                if (doctors.size() > 1) {
                    patientsWithMultipleDoctors.add(vertex);
                }
            }
        }
        
        return patientsWithMultipleDoctors;
    }
    
    /**
     * Show referral chains (Dr. A → Dr. B → Patient)
     */
    public ArrayList<ArrayList<String>> getReferralChains() {
        ArrayList<ArrayList<String>> referralChains = new ArrayList<>();
        Graph<String> graph = buildDoctorPatientGraph();
        
        // Get all vertices
        ArrayList<String> vertices = getAllVertices(graph);
        
        for (int i = 0; i < vertices.size(); i++) {
            String vertex = vertices.get(i);
            
            // Check if this is a doctor
            if (vertex.startsWith("D")) {
                ArrayList<String> patients = (ArrayList<String>) graph.neighborsOf(vertex);
                
                for (int j = 0; j < patients.size(); j++) {
                    String patient = patients.get(j);
                    
                    // Check if this patient has consulted other doctors
                    ArrayList<String> otherDoctors = (ArrayList<String>) graph.neighborsOf(patient);
                    
                    for (int k = 0; k < otherDoctors.size(); k++) {
                        String otherDoctor = otherDoctors.get(k);
                        
                        // Avoid self-referral
                        if (!otherDoctor.equals(vertex)) {
                            ArrayList<String> chain = new ArrayList<>();
                            chain.add(vertex); // Original doctor
                            chain.add(otherDoctor); // Referred doctor
                            chain.add(patient); // Patient
                            referralChains.add(chain);
                        }
                    }
                }
            }
        }
        
        return referralChains;
    }
    
    /**
     * 2. CONSULTATION DEPENDENCY GRAPH
     * Creates a graph where:
     * - Nodes = Consultations
     * - Edges = Follow-up relationships between consultations
     */
    
    /**
     * Build the consultation dependency graph
     */
    public Graph<String> buildConsultationDependencyGraph() {
        Graph<String> graph = new Graph<>();
        
        // Get all consultations
        ArrayList<Consultation> allConsultations = getAllConsultations();
        
        // Add all consultations as vertices
        for (int i = 0; i < allConsultations.size(); i++) {
            Consultation consultation = allConsultations.get(i);
            graph.addVertex(consultation.getConsultationId());
        }
        
        // Add edges for follow-up relationships
        for (int i = 0; i < allConsultations.size(); i++) {
            Consultation consultation = allConsultations.get(i);
            String followUpId = consultation.getFollowUpConsultationId();
            
            if (followUpId != null && graph.hasVertex(followUpId)) {
                graph.addEdge(consultation.getConsultationId(), followUpId);
            }
        }
        
        return graph;
    }
    
    /**
     * Generate Follow-up Path Report for a patient
     */
    public ArrayList<String> getFollowUpPathForPatient(String patientId) {
        ArrayList<String> followUpPath = new ArrayList<>();
        Graph<String> graph = buildConsultationDependencyGraph();
        
        // Get all consultations for this patient
        ArrayList<Consultation> patientConsultations = consultationController.getConsultationsByPatient(patientId);
        
        if (patientConsultations.isEmpty()) {
            return followUpPath;
        }
        
        // Find the root consultation (one that has no incoming edges)
        String rootConsultation = findRootConsultation(graph, patientConsultations);
        
        if (rootConsultation != null) {
            // Use BFS to traverse the follow-up chain
            ArrayList<String> path = (ArrayList<String>) graph.bfs(rootConsultation);
            followUpPath = path;
        }
        
        return followUpPath;
    }
    
    /**
     * Get all linked consultations for a specific consultation
     */
    public ArrayList<String> getLinkedConsultations(String consultationId) {
        ArrayList<String> linkedConsultations = new ArrayList<>();
        Graph<String> graph = buildConsultationDependencyGraph();
        
        if (!graph.hasVertex(consultationId)) {
            return linkedConsultations;
        }
        
        // Use BFS to find all connected consultations
        ArrayList<String> connected = (ArrayList<String>) graph.bfs(consultationId);
        linkedConsultations = connected;
        
        return linkedConsultations;
    }
    
    /**
     * Create a follow-up consultation and link it to the original
     */
    public Consultation createFollowUpConsultation(String originalConsultationId, 
                                                  String newDoctorId, 
                                                  LocalDateTime newDateTime) {
        // Get the original consultation
        Consultation original = consultationController.getConsultationById(originalConsultationId);
        if (original == null) {
            return null;
        }
        
        // Create the follow-up consultation
        Consultation followUp = consultationController.createConsultation(
            original.getPatientId(), 
            newDoctorId, 
            newDateTime
        );
        
        // Link the original consultation to the follow-up
        original.setFollowUpConsultationId(followUp.getConsultationId());
        
        return followUp;
    }
    
    // Helper methods
    
    private ArrayList<Consultation> getAllConsultations() {
        return consultationController.getAllConsultations();
    }
    
    private ArrayList<String> getAllVertices(Graph<String> graph) {
        return (ArrayList<String>) graph.getAllVertices();
    }
    
    private String findRootConsultation(Graph<String> graph, ArrayList<Consultation> consultations) {
        // Find a consultation that has no incoming edges (root of the chain)
        for (int i = 0; i < consultations.size(); i++) {
            String consultationId = consultations.get(i).getConsultationId();
            
            // Check if this consultation has any incoming edges
            boolean hasIncoming = false;
            ArrayList<String> allVertices = getAllVertices(graph);
            
            for (int j = 0; j < allVertices.size(); j++) {
                String vertex = allVertices.get(j);
                if (graph.hasEdge(vertex, consultationId)) {
                    hasIncoming = true;
                    break;
                }
            }
            
            if (!hasIncoming) {
                return consultationId;
            }
        }
        
        // If no root found, return the first consultation
        return consultations.isEmpty() ? null : consultations.get(0).getConsultationId();
    }
    
    /**
     * Print a formatted doctor-patient relationship report
     */
    public void printDoctorPatientReport(String doctorId) {
        System.out.println("=== DOCTOR-PATIENT RELATIONSHIP REPORT ===");
        System.out.println("Doctor ID: " + doctorId);
        
        ArrayList<String> patients = getPatientsForDoctorThisMonth(doctorId);
        System.out.println("Patients consulted this month: " + patients.size());
        for (int i = 0; i < patients.size(); i++) {
            System.out.println("  - " + patients.get(i));
        }
        
        ArrayList<String> multiDoctorPatients = getPatientsWithMultipleDoctors();
        System.out.println("\nPatients with multiple doctors: " + multiDoctorPatients.size());
        for (int i = 0; i < multiDoctorPatients.size(); i++) {
            System.out.println("  - " + multiDoctorPatients.get(i));
        }
        
        ArrayList<ArrayList<String>> referralChains = getReferralChains();
        System.out.println("\nReferral chains found: " + referralChains.size());
        for (int i = 0; i < referralChains.size(); i++) {
            ArrayList<String> chain = referralChains.get(i);
            System.out.println("  " + chain.get(0) + " → " + chain.get(1) + " → " + chain.get(2));
        }
    }
    
    /**
     * Print a formatted follow-up path report
     */
    public void printFollowUpPathReport(String patientId) {
        System.out.println("=== FOLLOW-UP PATH REPORT ===");
        System.out.println("Patient ID: " + patientId);
        
        ArrayList<String> followUpPath = getFollowUpPathForPatient(patientId);
        System.out.println("Follow-up consultation chain:");
        
        if (followUpPath.isEmpty()) {
            System.out.println("  No follow-up consultations found.");
        } else {
            for (int i = 0; i < followUpPath.size(); i++) {
                String consultationId = followUpPath.get(i);
                Consultation consultation = consultationController.getConsultationById(consultationId);
                if (consultation != null) {
                    System.out.println("  " + (i + 1) + ". " + consultation.toString());
                }
            }
        }
    }
}
