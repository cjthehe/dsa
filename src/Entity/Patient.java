package Entity;

import java.time.LocalDate;
import ADT.ArrayList;


public class Patient {
    private ArrayList<String> patientSymptoms;
    //attribute
    private String PID;
    private String Pname;
    private String ic;
    private int age;
    private char gender;
    private String phoneNumber;
    private String email;
    private String faculty;
    private String state;
    private String DoctorAssigned;
    private String patientDisease;
    private LocalDate registrationDate;
    
    //Constructor
    public Patient(String PID,String Pname, String ic, int age, char gender, String phoneNumber, String email,String faculty, String state, String DoctorAssigned,String patientDisease, ArrayList<String> patientSymptoms, LocalDate registrationDate){
        this.PID = PID;
        this.Pname = Pname;
        this.ic= ic;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.faculty = faculty;
        this.state = state;
        this.DoctorAssigned = DoctorAssigned;
        this.patientDisease = patientDisease;
        this.patientSymptoms = patientSymptoms;
        this.registrationDate = registrationDate;
    }
    
//region getter
    public String getID(){
        return PID;
    }
   
    public String getName(){
        return Pname;
    }

    public String getIc(){
        return ic;
    }

    public int getAge(){
        return age;
    }
    
    public char getGender(){
        return gender;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getEmail(){
        return email;
    }
    
    public String getFaculty(){
        return faculty;
    }
    
    public String getState(){
        return state;
    }
    
    public String getDoctorAssigned(){
        return DoctorAssigned;
    }
    
    public String getPatientDisease(){
        return patientDisease;
    }
    
    public ArrayList<String> getPatientSymtomps(){
        return patientSymptoms;
    }
    
    public LocalDate getRegistrationDate(){
        return registrationDate;
    }
    
    //setter
    public void setID(String PID){
        this.PID = PID;
    }
    
    public void setName(String Pname){
        this.Pname = Pname;
    }

    public void setIc(String ic){
        this.ic= ic;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setGender(char gender){
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email){
        this.email = email;
    }
    
    public void setFaculty(String faculty){
        this.faculty = faculty;
    }
    public void setState(String state){
        this.state = state;
    }
    
    public void setDoctorAssigned(String DoctorAssigned){
        this.DoctorAssigned = DoctorAssigned;
    }
    
    public void setPatientDisease(String patientDisease){
        this.patientDisease = patientDisease;
    }
    
    public void addSymptoms(String patientSymptoms){
        this.patientSymptoms.add(patientSymptoms);
    }
    
    public void setRegistrationDate(LocalDate registrationDate){
        this.registrationDate = registrationDate;
    }
    
    //additional char to String method
    public String genderToString(){
        switch (gender) {
            case 'M':
                return "Male";
            case 'F':
                return "Female";
            default:
                return "Other";
        }
    }
    
}