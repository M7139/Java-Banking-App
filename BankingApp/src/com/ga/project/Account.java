package com.ga.project;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected int overdraftCount;
    protected boolean active;
    protected Card card;
    protected List<Transaction> transactions;

    public Account(String accountNumber, Card card) {
        this.accountNumber = accountNumber;
        this.balance = 0.0;
        this.overdraftCount = 0;
        this.active = true;
        this.card = card;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getOverdraftCount() {
        return overdraftCount;
    }

    public void setOverdraftCount(int overdraftCount) {
        this.overdraftCount = overdraftCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Card getCard() {
        return card;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}