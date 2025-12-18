package com.example.astrabank.api.request;

import com.example.astrabank.constant.AccountType;

public class SavingAccountRequest {
    private String userId;           // ID người sở hữu tài khoản
    private AccountType accountType;        // Loại tài khoản (saving, checking,...)
    private Long balance;

    public SavingAccountRequest(String userId, AccountType accountType, Long balance) {
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
}
