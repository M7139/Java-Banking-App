package com.ga.project;

public class MastercardPlatinum extends Card {

    public MastercardPlatinum(String cardNumber) {
        super(cardNumber);
        this.withdrawLimitPerDay = 20000;
        this.transferLimitPerDay = 40000;
        this.transferLimitPerDayOwnAccount = 80000;
        this.depositLimitPerDay = 100000;
        this.depositLimitPerDayOwnAccount = 200000;
    }
}