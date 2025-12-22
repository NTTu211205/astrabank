package com.example.astrabank.api.request;

public class ReceiptPaymentRequest {
    private String receiptId;
    private String sourceAccountNumber;
    private String sourceBankSymbol;
    private String senderName;

    public ReceiptPaymentRequest(String receiptId, String sourceAccountNumber, String sourceBankSymbol, String senderName) {
        this.receiptId = receiptId;
        this.sourceAccountNumber = sourceAccountNumber;
        this.sourceBankSymbol = sourceBankSymbol;
        this.senderName = senderName;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getSourceBankSymbol() {
        return sourceBankSymbol;
    }

    public void setSourceBankSymbol(String sourceBankSymbol) {
        this.sourceBankSymbol = sourceBankSymbol;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
