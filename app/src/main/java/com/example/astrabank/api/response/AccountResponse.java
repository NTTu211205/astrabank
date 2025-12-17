package com.example.astrabank.api.response;

import com.google.gson.annotations.SerializedName;

public class AccountResponse {
    @SerializedName("accountNumber")
    private String accountNumber;
    @SerializedName("accountName")
    private String accountName;

    public AccountResponse(String accountNumber, String accountName) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
