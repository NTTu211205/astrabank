package com.example.astrabank.models;

import com.example.astrabank.constant.TransactionStatus;
import com.example.astrabank.constant.TransactionType;

public class Transaction {
    private String transactionId;               // Mã giao dịch
    private String sourceAcc;           // Tài khoản nguồn
    private String destinationAcc;           // Tài khoản đích
    private TransactionStatus status;           // Trạng thái giao dịch (success, pending, failed)
    private double amount;           // Số tiền giao dịch
    private TransactionType type;             // Loại giao dịch (transfer, deposit, withdraw,...)
    private String description;      // Mô tả giao dịch
    private String createdAt;
    private String sourceBankName;
    private String desBankName;

    public Transaction(String transactionId, String sourceAcc, String destinationAcc, TransactionStatus status, double amount, TransactionType type, String description, String createdAt, String sourceBankName, String desBankName) {
        this.transactionId = transactionId;
        this.sourceAcc = sourceAcc;
        this.destinationAcc = destinationAcc;
        this.status = status;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.createdAt = createdAt;
        this.sourceBankName = sourceBankName;
        this.desBankName = desBankName;
    }


    public String getSourceBankName() {
        return sourceBankName;
    }

    public void setSourceBankName(String sourceBankName) {
        this.sourceBankName = sourceBankName;
    }

    public String getDesBankName() {
        return desBankName;
    }

    public void setDesBankName(String desBankName) {
        this.desBankName = desBankName;
    }

    public String gettransactionId() {
        return transactionId;
    }

    public void settransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSourceAcc() {
        return sourceAcc;
    }

    public void setSourceAcc(String sourceAcc) {
        this.sourceAcc = sourceAcc;
    }

    public String getDestinationAcc() {
        return destinationAcc;
    }

    public void setDestinationAcc(String destinationAcc) {
        this.destinationAcc = destinationAcc;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
