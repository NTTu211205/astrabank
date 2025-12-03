package com.example.astrabank.models;

public class Bank {
    private String bankName;
    private String bankSymbol;

    public Bank(String bankName, String bankSymbol) {
        this.bankName = bankName;
        this.bankSymbol = bankSymbol;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankSymbol() {
        return bankSymbol;
    }

    public void setBankSymbol(String bankSymbol) {
        this.bankSymbol = bankSymbol;
    }
}
