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
    private double depositedOwnToday;
    private double depositedOtherToday;
    private LocalDate lastResetDate;

    public Card(String cardNumber) {
        this.cardNumber = cardNumber;
        this.withdrawnToday = 0;
        this.transferredToday = 0;
        this.transferredOwnToday = 0;
        this.depositedOwnToday = 0;
        this.depositedOtherToday = 0;
        this.lastResetDate = LocalDate.now();
    }

    private void resetIfNewDay() {
        LocalDate today = LocalDate.now();
        if (!today.equals(lastResetDate)) {
            withdrawnToday = 0;
            transferredToday = 0;
            transferredOwnToday = 0;
            depositedOwnToday = 0;
            depositedOtherToday = 0;
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


    public String checkAndRecordDeposit(double amount, boolean isOwnAccount) {
        resetIfNewDay();
        if (isOwnAccount) {
            if (depositedOwnToday + amount > depositLimitPerDayOwnAccount) {
                return "DAILY_LIMIT_EXCEEDED";
            }
            depositedOwnToday += amount;
        } else {
            if (depositedOtherToday + amount > depositLimitPerDay) {
                return "DAILY_LIMIT_EXCEEDED";
            }
            depositedOtherToday += amount;
        }
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