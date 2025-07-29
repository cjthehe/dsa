/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.time.LocalDate;

/**
 *
 * @author user
 */
public class Patient {
    //attribute
    private String PID;
    private String Pname;
    private String ic;
    private int age;
    private char gender;
    private String phoneNumber;
    private String email;
    private String DoctorAssigned;
//    private String medicalHistory;
    private LocalDate registrationDate;
    //private Time consultationHours;
    
    //Constructor
    public Patient(String PID,String Pname,String ic, int age, char gender, String phoneNumber, String email, String DoctorAssigned, LocalDate registrationDate){
        this.PID = PID;
        this.Pname = Pname;
        this.ic= ic;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.DoctorAssigned = DoctorAssigned;
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

    public String getDoctorAssigned(){
        return DoctorAssigned;
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
    
    public void setDoctorAssigned(String DoctorAssigned){
        this.DoctorAssigned = DoctorAssigned;
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
    @Override
    public String toString(){
        return Pname + " | " + ic + " | " + age + " | " + gender 
                + " | " + phoneNumber + " | " + email + " | " + DoctorAssigned
                + " | " + registrationDate + " | ";
    } 
}