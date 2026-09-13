package com.ga.project;

import java.util.Optional;

public class Customer extends User {
    private Optional<CheckingAccount> checkingAccount;
    private Optional<SavingsAccount> savingsAccount;

    public Customer(String id, String name, String encryptedPassword) {
        super(id, name, encryptedPassword);
        this.checkingAccount = Optional.empty();
        this.savingsAccount = Optional.empty();
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    public Optional<CheckingAccount> getCheckingAccount() {
        return checkingAccount;
    }

    public void setCheckingAccount(Optional<CheckingAccount> checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    public Optional<SavingsAccount> getSavingsAccount() {
        return savingsAccount;
    }

    public void setSavingsAccount(Optional<SavingsAccount> savingsAccount) {
        this.savingsAccount = savingsAccount;
    }
}