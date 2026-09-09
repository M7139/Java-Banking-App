package com.ga.project;

public class Mastercard extends Card {

    public Mastercard(String cardNumber) {
        super(cardNumber);
        this.withdrawLimitPerDay = 5000;
        this.transferLimitPerDay = 10000;
        this.transferLimitPerDayOwnAccount = 20000;
        this.depositLimitPerDay = 100000;
        this.depositLimitPerDayOwnAccount = 200000;
    }
}