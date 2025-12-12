package com.example.astrabank.TESTACCOUNT;

public class Transaction {
    private String title;
    private String date;
    private double amount;

    public Transaction(String title, String date, double amount) {
        this.title = title;
        this.date = date;
        this.amount = amount;
    }

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public double getAmount() { return amount; }
}
