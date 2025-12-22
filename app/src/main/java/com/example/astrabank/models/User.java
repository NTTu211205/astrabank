package com.example.astrabank.models;

import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.HashMap;

public class User {
    @SerializedName("userID")
    private String userID;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("dateOfBirth")
    private String dateOfBirth;

    @SerializedName("nationalID")
    private String nationalID;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("address")
    private String address;

    @SerializedName("occupation")
    private String occupation;

    @SerializedName("companyName")
    private String companyName;

    @SerializedName("averageSalary")
    private Double averageSalary;

    @SerializedName("status")
    private Boolean status;

    @SerializedName("transactionPIN")
    private String transactionPIN;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("updatedAt")
    private Date updatedAt;

    @SerializedName("role")
    private String role;


    public User() {}

    public User(String userID, String fullName, String dateOfBirth, String nationalID, String email, String phone, String address, String occupation, String companyName, Double averageSalary, Boolean status, String transactionPIN, String password) {
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
        this.createdAt = new Date();
        this.updatedAt = new Date();
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

    public User(String userID, String fullName, String dateOfBirth, String nationalID, String email, String phone, String address, String occupation, String companyName, Double averageSalary, Boolean status, String transactionPIN, Date createdAt, Date updatedAt, String role) {
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
