package com.example.astrabank.models;


import com.example.astrabank.constant.AccountType;
import com.google.firebase.Timestamp;

import java.math.BigDecimal;

public class Account {
    private String userId;           // ID người sở hữu tài khoản
    private String accountNumber;    // Số tài khoản
    private boolean AccountStatus;          // Trạng thái tài khoản (active, disabled,...)
    private long balance;            // Số dư
    private AccountType AccountType;        // Loại tài khoản (saving, checking,...)
    private Timestamp createdAt;

    public Account(String userId, String accountNumber, boolean accountStatus,
                   long balance, AccountType accountType) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        AccountStatus = accountStatus;
        this.balance = balance;
        AccountType = accountType;
        this.createdAt = Timestamp.now();
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
        return AccountStatus;
    }

    public void setAccountStatus(boolean accountStatus) {
        AccountStatus = accountStatus;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return AccountType;
    }

    public void setAccountType(AccountType accountType) {
        AccountType = accountType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
