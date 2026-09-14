package com.ga.project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected int overdraftCount;
    protected boolean active;
    protected Card card;
    protected List<Transaction> transactions;
    protected OverdraftPolicy overdraftPolicy;

    public Account(String accountNumber, Card card) {
        this.accountNumber = accountNumber;
        this.balance = 0.0;
        this.overdraftCount = 0;
        this.active = true;
        this.card = card;
        this.transactions = new ArrayList<>();
        this.overdraftPolicy = new OverdraftPolicy();
    }
    public String deposit(double amount){
        return deposit(amount, true);
    }

    public String deposit(double amount, boolean isOwnAccount) {
        if (amount <= 0) {
            return "INVALID_AMOUNT";
        }

        String limitCheck = card.checkAndRecordDeposit(amount, isOwnAccount);
        if (!limitCheck.equals("SUCCESS")) {
            return limitCheck;
        }

        balance += amount;
        recordTransaction("DEPOSIT", amount);
        overdraftPolicy.reactivateIfEligible(this);

        return "SUCCESS";
    }

    public String withdraw(double amount) {
        if (amount <= 0) {
            return "INVALID_AMOUNT";
        }

        String limitCheck = card.checkAndRecordWithdraw(amount);
        if (!limitCheck.equals("SUCCESS")) {
            return limitCheck;
        }

        String result = overdraftPolicy.processWithdrawal(this, amount);

        if (result.equals("SUCCESS")) {
            recordTransaction("WITHDRAW", amount);
        }

        return result;
    }

    public String transferTo(Account destination, double amount, boolean isOwnAccount) {
        String transferLimitCheck = card.checkAndRecordTransfer(amount, isOwnAccount);
        if (!transferLimitCheck.equals("SUCCESS")) {
            return transferLimitCheck;
        }

        String receivingLimitCheck = destination.card.checkAndRecordTransfer(amount, isOwnAccount);
        if (!receivingLimitCheck.equals("SUCCESS")) {
            return receivingLimitCheck;
        }

        String withdrawResult = this.withdraw(amount);
        if (!withdrawResult.equals("SUCCESS")) {
            return withdrawResult;
        }

        destination.balance += amount;
        destination.recordTransaction("DEPOSIT", amount);
        destination.overdraftPolicy.reactivateIfEligible(destination);

        return "SUCCESS";
    }

    protected void recordTransaction(String type, double amount) {
        Transaction transaction = new Transaction(
                UUID.randomUUID().toString(),
                type,
                amount,
                balance,
                LocalDateTime.now()
        );
        transactions.add(transaction);
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