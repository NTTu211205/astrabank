package com.example.astrabank.api.request;

import com.example.astrabank.constant.AccountType;

public class CheckingAccountRequest {
    private String userId;           // ID người sở hữu tài khoản
    private AccountType accountType;

    public CheckingAccountRequest(String userId, AccountType accountType) {
        this.userId = userId;
        this.accountType = accountType;
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
}
