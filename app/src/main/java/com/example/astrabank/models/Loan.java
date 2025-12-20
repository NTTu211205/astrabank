package com.example.astrabank.models;

import java.util.Date;

public class Loan {
    private String loanId;
    private String accountNumber;
    private int term;
    private long originalPrincipal;
    private double interestRate;
    private boolean isComplete;
    private Date createdAt;
    private Date disbursementDate;
    private String status;
    private String address;

    public Loan() {}

    public Loan(String loanId, String accountNumber, int term, long originalPrincipal, double interestRate, boolean isComplete, Date createdAt, Date disbursementDate, String status, String address) {
        this.loanId = loanId;
        this.accountNumber = accountNumber;
        this.term = term;
        this.originalPrincipal = originalPrincipal;
        this.interestRate = interestRate;
        this.isComplete = isComplete;
        this.createdAt = createdAt;
        this.disbursementDate = disbursementDate;
        this.status = status;
        this.address = address;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
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

    public long getOriginalPrincipal() {
        return originalPrincipal;
    }

    public void setOriginalPrincipal(long originalPrincipal) {
        this.originalPrincipal = originalPrincipal;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(Date disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
