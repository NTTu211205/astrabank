package com.example.astrabank.api.request;

import com.example.astrabank.constant.TransactionType;

public class TransactionRequest {
    private String sourceAccountNumber;
    private String sourceBankSymbol;
    private String destinationAccountNumber;
    private String destinationBankSymbol;
    private long amount;
    private TransactionType transactionType;
    private String description;
    private String senderName;
    private String receiverName;

    public TransactionRequest(String sourceAccountNumber, String sourceBankSymbol, String destinationAccountNumber, String destinationBankSymbol, long amount, TransactionType transactionType, String description, String senderName, String receiverName) {
        this.sourceAccountNumber = sourceAccountNumber;
        this.sourceBankSymbol = sourceBankSymbol;
        this.destinationAccountNumber = destinationAccountNumber;
        this.destinationBankSymbol = destinationBankSymbol;
        this.amount = amount;
        this.transactionType = transactionType;
        this.description = description;
        this.senderName = senderName;
        this.receiverName = receiverName;
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

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public String getDestinationBankSymbol() {
        return destinationBankSymbol;
    }

    public void setDestinationBankSymbol(String destinationBankSymbol) {
        this.destinationBankSymbol = destinationBankSymbol;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
