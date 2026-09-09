package com.ga.project;

public abstract class Card {
    protected String cardNumber;
    protected double withdrawLimitPerDay;
    protected double transferLimitPerDay;
    protected double transferLimitPerDayOwnAccount;
    protected double depositLimitPerDay;
    protected double depositLimitPerDayOwnAccount;

    public Card(String cardNumber) {
        this.cardNumber = cardNumber;
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