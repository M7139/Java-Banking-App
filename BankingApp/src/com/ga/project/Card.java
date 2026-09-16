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
        this.lastResetDate = LocalDate.now();
    }

    private void resetIfNewDay() {
        if (!LocalDate.now().equals(lastResetDate)) {
            withdrawnToday = 0;
            transferredToday = 0;
            transferredOwnToday = 0;
            depositedOwnToday = 0;
            depositedOtherToday = 0;
            lastResetDate = LocalDate.now();
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

    public String checkAndRecordDeposit(double amount, boolean isOwnAccount) {
        String result = checkDepositLimit(amount, isOwnAccount);

        if (result.equals("SUCCESS")) {
            recordDeposit(amount, isOwnAccount);
        }

        return result;
    }

    // Checks transfer limit without changing the daily counter
    public String checkTransferLimit(double amount, boolean isOwnAccount) {
        resetIfNewDay();

        if (isOwnAccount) {
            if (transferredOwnToday + amount > transferLimitPerDayOwnAccount) {
                return "DAILY_LIMIT_EXCEEDED";
            }
        } else {
            if (transferredToday + amount > transferLimitPerDay) {
                return "DAILY_LIMIT_EXCEEDED";
            }
        }

        return "SUCCESS";
    }

    // Records transfer only after the transfer succeeds
    public void recordTransfer(double amount, boolean isOwnAccount) {
        resetIfNewDay();

        if (isOwnAccount) {
            transferredOwnToday += amount;
        } else {
            transferredToday += amount;
        }
    }

    // Checks deposit limit without changing the daily counter
    public String checkDepositLimit(double amount, boolean isOwnAccount) {
        resetIfNewDay();

        if (isOwnAccount) {
            if (depositedOwnToday + amount > depositLimitPerDayOwnAccount) {
                return "DAILY_LIMIT_EXCEEDED";
            }
        } else {
            if (depositedOtherToday + amount > depositLimitPerDay) {
                return "DAILY_LIMIT_EXCEEDED";
            }
        }

        return "SUCCESS";
    }

    // Records deposit only after the transfer succeeds
    public void recordDeposit(double amount, boolean isOwnAccount) {
        resetIfNewDay();

        if (isOwnAccount) {
            depositedOwnToday += amount;
        } else {
            depositedOtherToday += amount;
        }
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