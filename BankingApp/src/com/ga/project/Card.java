package com.ga.project;

import java.time.LocalDate;

public abstract class Card {
    protected String cardNumber;
    protected double withdrawLimitPerDay;
    protected double transferLimitPerDay;
    protected double transferLimitPerDayOwnAccount;
    protected double depositLimitPerDay;
    protected double depositLimitPerDayOwnAccount;

    private double withdrawnToday;
    private double transferredToday;
    private double transferredOwnToday;
    private double cashDepositedToday;
    private double incomingTransferToday;
    private LocalDate lastResetDate;

    public Card(String cardNumber) {
        this.cardNumber = cardNumber;
        this.withdrawnToday = 0;
        this.transferredToday = 0;
        this.transferredOwnToday = 0;
        this.cashDepositedToday = 0;
        this.incomingTransferToday = 0;
        this.lastResetDate = LocalDate.now();
    }

    private void resetIfNewDay() {
        LocalDate today = LocalDate.now();
        if (!today.equals(lastResetDate)) {
            withdrawnToday = 0;
            transferredToday = 0;
            transferredOwnToday = 0;
            cashDepositedToday = 0;
            incomingTransferToday = 0;
            lastResetDate = today;
        }
    }

    public String checkAndRecordWithdraw(double amount) {
        resetIfNewDay();
        if (withdrawnToday + amount > withdrawLimitPerDay) {
            return "DAILY_LIMIT_EXCEEDED";
        }
        withdrawnToday += amount;
        return "SUCCESS";
    }

    public String checkAndRecordTransfer(double amount, boolean isOwnAccount) {
        resetIfNewDay();
        if (isOwnAccount) {
            if (transferredOwnToday + amount > transferLimitPerDayOwnAccount) {
                return "DAILY_LIMIT_EXCEEDED";
            }
            transferredOwnToday += amount;
        } else {
            if (transferredToday + amount > transferLimitPerDay) {
                return "DAILY_LIMIT_EXCEEDED";
            }
            transferredToday += amount;
        }
        return "SUCCESS";
    }

    //Cash deposit
    public String checkAndRecordDeposit(double amount) {
        resetIfNewDay();
        if (cashDepositedToday + amount > depositLimitPerDayOwnAccount) {
            return "DAILY_LIMIT_EXCEEDED";
        }
        cashDepositedToday += amount;
        return "SUCCESS";
    }

    //Money from other account
    public String checkAndRecordIncomingTransfer(double amount) {
        resetIfNewDay();
        if (incomingTransferToday + amount > depositLimitPerDay) {
            return "DAILY_LIMIT_EXCEEDED";
        }
        incomingTransferToday += amount;
        return "SUCCESS";
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public double getWithdrawLimitPerDay() {
        return withdrawLimitPerDay;
    }

    public double getTransferLimitPerDay() {
        return transferLimitPerDay;
    }

    public double getTransferLimitPerDayOwnAccount() {
        return transferLimitPerDayOwnAccount;
    }

    public double getDepositLimitPerDay() {
        return depositLimitPerDay;
    }

    public double getDepositLimitPerDayOwnAccount() {
        return depositLimitPerDayOwnAccount;
    }
}