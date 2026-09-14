package com.ga.project;

import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private String accountType;
    private String type;
    private double amount;
    private double postBalance;
    private LocalDateTime timestamp;

    public Transaction(String transactionId, String accountType, String type, double amount, double postBalance, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.accountType = accountType;
        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getPostBalance() {
        return postBalance;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}