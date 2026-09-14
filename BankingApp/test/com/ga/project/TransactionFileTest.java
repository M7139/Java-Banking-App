package com.ga.project;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class TransactionFileTest {

    FileManager fileManager;
    Customer customer;
    CheckingAccount checking;
    String testFileName = "Transactions-MelvinGordon-10001.txt";

    @BeforeEach
    public void setUp() {
        fileManager = new FileManager();
        Card card = new MastercardPlatinum("4000123456789012");
        checking = new CheckingAccount("CHK-10001", card);
        customer = new Customer("10001", "Melvin", "Gordon", "encryptedPass");

        new File(testFileName).delete(); // clean the file before starting
    }

//    @AfterEach
//    public void tearDown() {
//        new File(testFileName).delete(); // clean up after the test
//    }

    @Test
    @DisplayName("When transactions are appended and loaded then history matches")
    public final void whenTransactionsAppendedThenHistoryMatches() {
        checking.deposit(500);
        checking.withdraw(200);

        for (Transaction t : checking.getTransactions()) {
            fileManager.appendTransaction(customer, t);
        }

        List<Transaction> loaded = fileManager.loadTransactions(customer);

        Assert.assertEquals(2, loaded.size());
        Assert.assertEquals("DEPOSIT", loaded.get(0).getType());
        Assert.assertEquals("CHECKING", loaded.get(0).getAccountType());
        Assert.assertEquals(500.0, loaded.get(0).getAmount(), 0.001);
        Assert.assertEquals("WITHDRAW", loaded.get(1).getType());
        Assert.assertEquals(300.0, loaded.get(1).getPostBalance(), 0.001);
    }
}