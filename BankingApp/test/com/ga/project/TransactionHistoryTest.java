package com.ga.project;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TransactionHistoryTest {

    TransactionHistory transactionHistory;
    Customer customer;
    CheckingAccount checking;

    @BeforeEach
    public void setUp() {
        transactionHistory = new TransactionHistory();
        customer = new Customer("10001", "Melvin", "Gordon", "encryptedPass");
        Card card = new MastercardPlatinum("4000123456789012");
        checking = new CheckingAccount("CHK-10001", card);
    }

    @Test
    @DisplayName("When customer has no transactions then history shows empty message")
    public final void whenNoTransactionsThenEmptyMessageShown() {
        String result = transactionHistory.display(customer, checking.getTransactions());

        Assert.assertTrue(result.contains("Melvin Gordon"));
        Assert.assertTrue(result.contains("No transactions found."));
    }

    @Test
    @DisplayName("When customer has transactions then history shows each one with key details")
    public final void whenTransactionsExistThenHistoryShowsDetails() {
        checking.deposit(500);
        checking.withdraw(200);

        String result = transactionHistory.display(customer, checking.getTransactions());

        Assert.assertTrue(result.contains("Melvin Gordon"));
        Assert.assertTrue(result.contains("DEPOSIT"));
        Assert.assertTrue(result.contains("WITHDRAW"));
        Assert.assertTrue(result.contains("CHECKING"));
        Assert.assertTrue(result.contains("500.0"));
        Assert.assertTrue(result.contains("300.0")); // balance after withdraw
    }
}