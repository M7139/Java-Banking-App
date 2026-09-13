package com.ga.project;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OverdraftPolicyTest {

    CheckingAccount account;

    @BeforeEach
    public void setUp() {
        Card card = new MastercardPlatinum("4000123456789012");
        account = new CheckingAccount("CHK-001", card);
        account.deposit(100);
    }

    @Test
    @DisplayName("When withdrawal exceeds balance then overdraft fee is applied and balance goes negative")
    public final void whenOverdraftingThenFeeIsApplied() {
        String result = account.withdraw(150);

        Assert.assertEquals("SUCCESS", result);
        // 100 - 150 = -50, minus $35 fee = -85
        Assert.assertEquals(-85.0, account.getBalance(), 0.001);
        Assert.assertEquals(1, account.getOverdraftCount());
        Assert.assertTrue(account.isActive());
    }

    @Test
    @DisplayName("When balance is negative then withdrawal over $100 is blocked")
    public final void whenBalanceNegativeThenLargeWithdrawalBlocked() {
        account.withdraw(150); // balance now -85, 1 overdraft

        String result = account.withdraw(150); // exceeds $100 cap while negative
        Assert.assertEquals("EXCEEDS_NEGATIVE_BALANCE_LIMIT", result);
        Assert.assertEquals(-85.0, account.getBalance(), 0.001); // unchanged
    }

    @Test
    @DisplayName("When 2nd overdraft happens then account is deactivated")
    public final void whenSecondOverdraftThenAccountDeactivates() {
        account.withdraw(150); // 1st overdraft, balance -85
        account.deposit(85);   // back to 0, reactivation check runs (already active, no-op)

        String result = account.withdraw(50); // 2nd overdraft: 0 - 50 - 35 fee = -85
        Assert.assertEquals("SUCCESS", result);
        Assert.assertEquals(2, account.getOverdraftCount());
        Assert.assertFalse(account.isActive());
    }

    @Test
    @DisplayName("When account is deactivated then further withdrawals are blocked")
    public final void whenDeactivatedThenWithdrawalsBlocked() {
        account.withdraw(150); // 1st overdraft
        account.deposit(85);   // back to 0
        account.withdraw(50);  // 2nd overdraft, now deactivated

        String result = account.withdraw(10);
        Assert.assertEquals("ACCOUNT_DEACTIVATED", result);
    }

    @Test
    @DisplayName("When deposit resolves negative balance then account reactivates")
    public final void whenDepositResolvesBalanceThenReactivates() {
        account.withdraw(150); // 1st overdraft, balance -85
        account.deposit(85);   // back to 0
        account.withdraw(50);  // 2nd overdraft, balance -85, now deactivated

        Assert.assertFalse(account.isActive());

        account.deposit(85); // resolves balance back to 0
        Assert.assertTrue(account.isActive());
        Assert.assertEquals(0, account.getOverdraftCount());
    }
}