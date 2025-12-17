package com.example.astrabank.models;

import androidx.annotation.NonNull;

public class Bank {
    private String bankName;
    private String bankSymbol;
    private String bankFullName;

    public Bank(String bankName, String bankSymbol, String bankFullName) {
        this.bankName = bankName;
        this.bankSymbol = bankSymbol;
        this.bankFullName = bankFullName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankFullName() {
        return bankFullName;
    }

    public void setBankFullName(String bankFullName) {
        this.bankFullName = bankFullName;
    }

    public String getBankSymbol() {
        return bankSymbol;
    }

    public void setBankSymbol(String bankSymbol) {
        this.bankSymbol = bankSymbol;
    }

    @NonNull
    @Override
    public String toString() {
        return this.bankFullName + "(" + this.bankName + ")";
    }
}
