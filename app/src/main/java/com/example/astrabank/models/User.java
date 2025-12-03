package com.example.astrabank.models;

import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.HashMap;

public class User {
    private String userID;
    private String fullName;
    private String dateOfBirth;
    private String nationalID;
    private String email;
    private String phone;
    private String address;
    private String occupation;
    private String companyName;
    private Double averageSalary;
    private Boolean status;
    private String transactionPIN;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public User() {}

    public User(String userID, String fullName, String dateOfBirth, String nationalID, String email, String phone, String address, String occupation, String companyName, Double averageSalary, Boolean status, String transactionPIN) {
        this.userID = userID;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.nationalID = nationalID;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.occupation = occupation;
        this.companyName = companyName;
        this.averageSalary = averageSalary;
        this.status = status;
        this.transactionPIN = transactionPIN;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }

    public HashMap<String, Object> mapping() {
        HashMap<String, Object> userData = new HashMap<>();

        userData.put("fullName", fullName);
        userData.put("dateOfBirth", dateOfBirth);
        userData.put("nationalID", nationalID);
        userData.put("email", email);
        userData.put("phone", phone);
        userData.put("address", address);
        userData.put("occupation", occupation);
        userData.put("companyName", companyName);
        userData.put("averageSalary", averageSalary);
        userData.put("status", status);
        userData.put("createdAt", FieldValue.serverTimestamp());
        userData.put("updatedAt", FieldValue.serverTimestamp());

        return userData;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(Double averageSalary) {
        this.averageSalary = averageSalary;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTransactionPIN() {
        return transactionPIN;
    }

    public void setTransactionPIN(String transactionPIN) {
        this.transactionPIN = transactionPIN;
    }
}
