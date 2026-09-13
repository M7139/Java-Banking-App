package com.ga.project;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AccountTest {

    CheckingAccount account;

    @BeforeEach
    public void setUp() {
        Card card = new MastercardPlatinum("4000123456789012");
        account = new CheckingAccount("CHK-001", card);
    }

    @Test
    @DisplayName("When a valid amount is deposited then balance increases and SUCCESS is returned")
    public final void whenValidDepositThenBalanceIncreases() {
        String result = account.deposit(500);
        Assert.assertEquals("SUCCESS", result);
        Assert.assertEquals(500.0, account.getBalance(), 0.001);
    }

    @Test
    @DisplayName("When a negative amount is deposited then INVALID_AMOUNT is returned and balance is unchanged")
    public final void whenNegativeDepositThenInvalidAmountReturned() {
        String result = account.deposit(-50);
        Assert.assertEquals("INVALID_AMOUNT", result);
        Assert.assertEquals(0.0, account.getBalance(), 0.001);
    }

    @Test
    @DisplayName("When withdrawing within balance then balance decreases and SUCCESS is returned")
    public final void whenWithdrawWithinBalanceThenSucceeds() {
        account.deposit(500);
        String result = account.withdraw(200);
        Assert.assertEquals("SUCCESS", result);
        Assert.assertEquals(300.0, account.getBalance(), 0.001);
    }

    @Test
    @DisplayName("When withdrawing more than balance then INSUFFICIENT_FUNDS is returned and balance is unchanged")
    public final void whenWithdrawExceedsBalanceThenInsufficientFundsReturned() {
        account.deposit(500);
        String result = account.withdraw(1000);
        Assert.assertEquals("INSUFFICIENT_FUNDS", result);
        Assert.assertEquals(500.0, account.getBalance(), 0.001);
    }

    @Test
    @DisplayName("When deposit and withdraw happen then transactions are recorded correctly")
    public final void whenTransactionsOccurThenHistoryIsRecorded() {
        account.deposit(500);
        account.withdraw(200);

        Assert.assertEquals(2, account.getTransactions().size());
        Assert.assertEquals("DEPOSIT", account.getTransactions().get(0).getType());
        Assert.assertEquals("WITHDRAW", account.getTransactions().get(1).getType());
        Assert.assertEquals(300.0, account.getTransactions().get(1).getPostBalance(), 0.001);
    }
}