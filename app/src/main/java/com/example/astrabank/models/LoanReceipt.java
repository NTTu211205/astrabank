package com.example.astrabank.models;

import java.util.Date;

public class LoanReceipt {
    private String receiptId;
    private String loanId;
    private boolean paid;
    private int period;
    private long amount;
    private Date finalDate;
    private Date updatedAt;

    public LoanReceipt() {}

    public LoanReceipt(String receiptId, String loanId, boolean paid, int period, long amount, Date finalDate, Date updatedAt) {
        this.receiptId = receiptId;
        this.loanId = loanId;
        this.paid = paid;
        this.period = period;
        this.amount = amount;
        this.finalDate = finalDate;
        this.updatedAt = updatedAt;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
