package com.example.astrabank.models;

import com.example.astrabank.constant.TransactionStatus;
import com.example.astrabank.constant.TransactionType;

import java.util.Date;

public class Transaction {
    private String transactionId;               // Mã giao dịch
    private String sourceAcc;                   // Tài khoản nguồn
    private String bankSourceSymbol;
    private String destinationAcc;              // Tài khoản đích
    private String bankDesSymbol;
    private TransactionStatus status;           // Trạng thái giao dịch (success, pending, failed)
    private long amount;                        // Số tiền giao dịch
    private TransactionType type;               // Loại giao dịch (transfer, deposit, withdraw,...)
    private String description;                 // Mô tả giao dịch
    private String createdAt;
    private String updatedAt;
    private String senderName;
    private String receiverName;

    public Transaction(String transactionId, String sourceAcc, String bankSourceSymbol, String destinationAcc, String bankDesSymbol, TransactionStatus status, long amount, TransactionType type, String description, String createdAt, String updatedAt, String senderName, String receiverName) {
        this.transactionId = transactionId;
        this.sourceAcc = sourceAcc;
        this.bankSourceSymbol = bankSourceSymbol;
        this.destinationAcc = destinationAcc;
        this.bankDesSymbol = bankDesSymbol;
        this.status = status;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSourceAcc() {
        return sourceAcc;
    }

    public void setSourceAcc(String sourceAcc) {
        this.sourceAcc = sourceAcc;
    }

    public String getBankSourceSymbol() {
        return bankSourceSymbol;
    }

    public void setBankSourceSymbol(String bankSourceSymbol) {
        this.bankSourceSymbol = bankSourceSymbol;
    }

    public String getDestinationAcc() {
        return destinationAcc;
    }

    public void setDestinationAcc(String destinationAcc) {
        this.destinationAcc = destinationAcc;
    }

    public String getBankDesSymbol() {
        return bankDesSymbol;
    }

    public void setBankDesSymbol(String bankDesSymbol) {
        this.bankDesSymbol = bankDesSymbol;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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
