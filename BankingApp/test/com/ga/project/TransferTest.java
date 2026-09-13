package com.ga.project;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TransferTest {

    CheckingAccount checking;
    SavingsAccount savings;
    CheckingAccount otherCustomerAccount;

    @BeforeEach
    public void setUp() {
        Card card = new MastercardPlatinum("4000123456789012");
        checking = new CheckingAccount("CHK-001", card);
        savings = new SavingsAccount("SAV-001", card);
        otherCustomerAccount = new CheckingAccount("CHK-002", card);

        checking.deposit(500);
    }

    @Test
    @DisplayName("When transferring between own accounts then balances update correctly")
    public final void whenTransferBetweenOwnAccountsThenBalancesUpdate() {
        String result = checking.transferTo(savings, 200);

        Assert.assertEquals("SUCCESS", result);
        Assert.assertEquals(300.0, checking.getBalance(), 0.001);
        Assert.assertEquals(200.0, savings.getBalance(), 0.001);
    }

    @Test
    @DisplayName("When transferring to another customer's account then balances update correctly")
    public final void whenTransferToOtherCustomerThenBalancesUpdate() {
        String result = checking.transferTo(otherCustomerAccount, 150);

        Assert.assertEquals("SUCCESS", result);
        Assert.assertEquals(350.0, checking.getBalance(), 0.001);
        Assert.assertEquals(150.0, otherCustomerAccount.getBalance(), 0.001);
    }

    @Test
    @DisplayName("When transfer causes overdraft then fee is applied to sender only")
    public final void whenTransferCausesOverdraftThenFeeAppliedToSenderOnly() {
        String result = checking.transferTo(savings, 600); // exceeds 500 balance

        Assert.assertEquals("SUCCESS", result);
        // 500 - 600 - 35 fee = -135
        Assert.assertEquals(-135.0, checking.getBalance(), 0.001);
        Assert.assertEquals(600.0, savings.getBalance(), 0.001); // receiver gets full amount, no fee
    }

    @Test
    @DisplayName("When transfer is blocked due to deactivated account then destination is untouched")
    public final void whenSenderDeactivatedThenDestinationUnaffected() {
        checking.withdraw(600); // 1st overdraft, balance -135
        checking.deposit(135);  // back to 0
        checking.withdraw(50);  // 2nd overdraft, deactivated

        String result = checking.transferTo(savings, 10);

        Assert.assertEquals("ACCOUNT_DEACTIVATED", result);
        Assert.assertEquals(0.0, savings.getBalance(), 0.001); // nothing was deposited
    }
}