package com.example.astrabank.models;

public class PaymentRecord {
    private String title;
    private String date;
    private double amount;

    public PaymentRecord(String title, String date, double amount) {
        this.title = title;
        this.date = date;
        this.amount = amount;
    }

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public double getAmount() { return amount; }
}