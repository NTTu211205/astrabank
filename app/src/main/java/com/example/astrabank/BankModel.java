package com.example.astrabank;

public class BankModel {
    private String shortName; // Ví dụ: Techcombank - TCB
    private String fullName;  // Ví dụ: Ngân hàng TMCP Kỹ thương...
    private int logoResId;    // ID của hình ảnh trong drawable (R.drawable.logo_tcb)

    public BankModel(String shortName, String fullName, int logoResId) {
        this.shortName = shortName;
        this.fullName = fullName;
        this.logoResId = logoResId;
    }

    public String getShortName() { return shortName; }
    public String getFullName() { return fullName; }
    public int getLogoResId() { return logoResId; }
}