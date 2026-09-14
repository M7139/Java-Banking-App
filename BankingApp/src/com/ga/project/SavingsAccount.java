package com.ga.project;

public class SavingsAccount extends Account {

    public SavingsAccount(String accountNumber, Card card) {
        super(accountNumber, card);
    }

    @Override
    protected String getAccountType() {
        return "SAVINGS";
    }
}