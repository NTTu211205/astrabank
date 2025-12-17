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

    // JSON key viết hoa chữ A → bắt buộc dùng SerializedName
    @SerializedName("AccountStatus")
    private Boolean accountStatus;

    @SerializedName("balance")
    private Long balance;

    @SerializedName("accountType")
    private AccountType accountType; // hoặc enum AccountType

    @SerializedName("createdAt")
    private Date createdAt;

    public Account(String userId, String accountNumber, boolean accountStatus,
                   long balance, AccountType accountType) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountStatus = accountStatus;
        this.balance = balance;
        this.accountType = accountType;
        this.createdAt = new Date();
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
