package com.example.astrabank.api.request;

public class UpdateUserRequest {
    private String fullName;
    private String dateOfBirth;
    private String nationalID;
    private String email;
    private String phone;
    private String address;
    private String occupation;
    private String companyName;
    private Double averageSalary;

    public UpdateUserRequest(String fullName, String dateOfBirth, String nationalID, String email, String phone, String address, String occupation, String companyName, Double averageSalary) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.nationalID = nationalID;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.occupation = occupation;
        this.companyName = companyName;
        this.averageSalary = averageSalary;
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
}
