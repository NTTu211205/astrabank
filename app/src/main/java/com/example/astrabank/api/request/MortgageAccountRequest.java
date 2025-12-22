package com.example.astrabank.api.request;

public class MortgageAccountRequest {
    private String userId;
    private long balance;
    private double interestRate;

    public MortgageAccountRequest(String userId, long balance, double interestRate) {
        this.userId = userId;
        this.balance = balance;
        this.interestRate = interestRate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
