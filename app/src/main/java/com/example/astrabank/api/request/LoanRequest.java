package com.example.astrabank.api.request;

public class LoanRequest {
    private String accountNumber;
    private int term;
    private double interestRate;
    private String address;
    private long amount;
    private String name;

    public LoanRequest(String accountNumber, int term, double interestRate, String address, long amount, String name) {
        this.accountNumber = accountNumber;
        this.term = term;
        this.interestRate = interestRate;
        this.address = address;
        this.amount = amount;
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
