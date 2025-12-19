package com.example.astrabank.models;

import androidx.annotation.NonNull;

public class Bank {
    private String bankName;
    private String bankSymbol;
    private String bankFullName;
    private int logoResId;

    public int getLogoResId() {
        return logoResId;
    }

    public void setLogoResId(int logoResId) {
        this.logoResId = logoResId;
    }


    public Bank(String bankName, String bankSymbol, String bankFullName, int logoResId) {
        this.bankName = bankName;
        this.bankSymbol = bankSymbol;
        this.bankFullName = bankFullName;
        this.logoResId = logoResId;
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
