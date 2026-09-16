package com.ga.project;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class BankTest {

    String customersFile = "customers.txt";
    String bankersFile = "bankers.txt";
    String melvinTransactionsFile = "Transactions-MelvinGordon-10001.txt";

    @BeforeEach
    public void setUp() throws IOException {
        new File(customersFile).delete();
        new File(bankersFile).delete();
        new File(melvinTransactionsFile).delete();

        // Simulate pre-existing mock data, as if provided before the program started
        try (PrintWriter writer = new PrintWriter(new FileWriter(customersFile))) {
            writer.println("10001,Melvin,Gordon,ef92b778,CHK,2000.0,SAV,500.0,PLATINUM");
            writer.println("10002,Stacey,Abrams,3a7bd3e2,CHK,1000.0,,0.0,TITANIUM");
        }
    }

    @AfterEach
    public void tearDown() {
        new File(customersFile).delete();
        new File(bankersFile).delete();
        new File(melvinTransactionsFile).delete();
    }

    @Test
    @DisplayName("When Bank is created then it loads existing mock customers")
    public final void whenBankCreatedThenLoadsMockData() {
        Bank bank = new Bank(new FileManager());
        Assert.assertEquals(2, bank.getCustomers().size());
    }

    @Test
    @DisplayName("When a new customer is added then original mock customers are preserved")
    public final void whenCustomerAddedThenOriginalCustomersPreserved() {
        Bank bank = new Bank(new FileManager());

        PasswordEncryptor encryptor = new PasswordEncryptor();
        AddCustomer addCustomer = new AddCustomer(encryptor);
        Banker banker = new Banker("B001", "Saad", "Iqbal", encryptor.encrypt("bankerpass"));

        Customer newCustomer = addCustomer.addNewCustomer("10003", "Micheal", "Paul", "pass123", true, false, "MASTERCARD");
        bank.addCustomer(newCustomer);

        Bank reloadedBank = new Bank(new FileManager());

        Assert.assertEquals(3, reloadedBank.getCustomers().size());
        Assert.assertEquals("10001", reloadedBank.getCustomers().get(0).getId());
        Assert.assertEquals("10002", reloadedBank.getCustomers().get(1).getId());
        Assert.assertEquals("10003", reloadedBank.getCustomers().get(2).getId());
    }

    @Test
    @DisplayName("When deposit is made through Bank then balance persists after simulated restart")
    public final void whenDepositMadeThenBalancePersists() {
        Bank bank = new Bank(new FileManager());
        Customer melvin = bank.getCustomers().get(0); // Melvin, loaded from mock data

        Account checking = melvin.getCheckingAccount().get();
        String result = bank.deposit(melvin, checking, 500);
        Assert.assertEquals("SUCCESS", result);

        Bank reloadedBank = new Bank(new FileManager());
        Customer reloadedMelvin = reloadedBank.getCustomers().get(0);

        // original 2000 + new 500 = 2500
        Assert.assertEquals(2500.0, reloadedMelvin.getCheckingAccount().get().getBalance(), 0.001);
    }

    @Test
    @DisplayName("When deposit is made then transaction file has an entry")
    public final void whenDepositMadeThenTransactionFileHasEntry() {
        Bank bank = new Bank(new FileManager());
        Customer melvin = bank.getCustomers().get(0);

        Account checking = melvin.getCheckingAccount().get();
        bank.deposit(melvin, checking, 500);

        FileManager fileManager = new FileManager();
        List<Transaction> transactions = fileManager.loadTransactions(melvin);

        Assert.assertEquals(1, transactions.size());
        Assert.assertEquals("DEPOSIT", transactions.get(0).getType());
        Assert.assertEquals(500.0, transactions.get(0).getAmount(), 0.001);
    }

    @Test
    @DisplayName("When withdraw is made through Bank then balance persists after simulated restart")
    public final void whenWithdrawMadeThenBalancePersists() {
        Bank bank = new Bank(new FileManager());
        Customer melvin = bank.getCustomers().get(0);
        Account checking = melvin.getCheckingAccount().get();

        String result = bank.withdraw(melvin, checking, 200);
        Assert.assertEquals("SUCCESS", result);

        Bank reloadedBank = new Bank(new FileManager());
        Customer reloadedMelvin = reloadedBank.getCustomers().get(0);

        // original 2000 - 200 = 1800
        Assert.assertEquals(1800.0, reloadedMelvin.getCheckingAccount().get().getBalance(), 0.001);
    }
}