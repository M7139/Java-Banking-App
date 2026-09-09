package com.ga.project;

public class MastercardTitanium extends Card {

    public MastercardTitanium(String cardNumber) {
        super(cardNumber);
        this.withdrawLimitPerDay = 10000;
        this.transferLimitPerDay = 20000;
        this.transferLimitPerDayOwnAccount = 40000;
        this.depositLimitPerDay = 100000;
        this.depositLimitPerDayOwnAccount = 200000;
    }
}