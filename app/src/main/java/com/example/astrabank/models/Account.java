package com.example.astrabank.models;


import androidx.annotation.NonNull;

import com.example.astrabank.constant.AccountType;
import com.google.firebase.Timestamp;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

public class Account {
    @SerializedName("userId")
    private String userId;

    @SerializedName("accountNumber")
    private String accountNumber;

    @SerializedName("AccountStatus")
    private Boolean accountStatus;

    @SerializedName("balance")
    private Long balance;

    @SerializedName("accountType")
    private AccountType accountType;

    @SerializedName("createdAt")
    private Date createdAt;

    @SerializedName("interestRate")
    private Double interestRate;

    @SerializedName("isLoan")
    private Boolean isLoan;

    @SerializedName("presentLoanId")
    private String presentLoanId;

    public Account(String userId, String accountNumber, boolean accountStatus,
                   long balance, AccountType accountType) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountStatus = accountStatus;
        this.balance = balance;
        this.accountType = accountType;
        this.createdAt = new Date();
    }

    public Account(String userId, String accountNumber, Boolean accountStatus, Long balance, AccountType accountType, Date createdAt, Double interestRate, Boolean isLoan, String presentLoanId) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountStatus = accountStatus;
        this.balance = balance;
        this.accountType = accountType;
        this.createdAt = createdAt;
        this.interestRate = interestRate;
        this.isLoan = isLoan;
        this.presentLoanId = presentLoanId;
    }

    public Boolean getLoan() {
        return isLoan;
    }

    public void setLoan(Boolean loan) {
        isLoan = loan;
    }

    public String getPresentLoanId() {
        return presentLoanId;
    }

    public void setPresentLoanId(String presentLoanId) {
        this.presentLoanId = presentLoanId;
    }

    public Boolean getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Boolean accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public boolean isAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(boolean accountStatus) {
        accountStatus = accountStatus;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        accountType = accountType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @NonNull
    @Override
    public String toString() {
        return accountType + "(" + accountNumber + ")";
    }
}
