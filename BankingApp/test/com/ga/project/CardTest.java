package com.ga.project;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CardTest {

    CheckingAccount account;
    CheckingAccount otherAccount;

    @BeforeEach
    public void setUp() {
        Card card = new Mastercard("4000123456789099");
        account = new CheckingAccount("CHK-001", card);

        Card otherCard = new Mastercard("4000123456789098");
        otherAccount = new CheckingAccount("CHK-002", otherCard);

        account.deposit(1000);
    }

    @Test
    @DisplayName("When withdrawal exceeds daily withdraw limit then it is blocked")
    public final void whenWithdrawExceedsDailyLimitThenBlocked() {
        String result = account.withdraw(6000);
        Assert.assertEquals("DAILY_LIMIT_EXCEEDED", result);
    }

    @Test
    @DisplayName("When transfer to another customer exceeds daily transfer limit then it is blocked")
    public final void whenTransferExceedsDailyLimitThenBlocked() {
        String result = account.transferTo(otherAccount, 11000, false);
        Assert.assertEquals("DAILY_LIMIT_EXCEEDED", result);
    }

    @Test
    @DisplayName("When transfer to own account exceeds own transfer limit then it is blocked")
    public final void whenOwnTransferExceedsDailyLimitThenBlocked() {
        String result = account.transferTo(otherAccount, 21000, true);
        Assert.assertEquals("DAILY_LIMIT_EXCEEDED", result);
    }

    @Test
    @DisplayName("When cash deposit exceeds own deposit limit then it is blocked")
    public final void whenCashDepositExceedsOwnLimitThenBlocked() {
        // Own-account deposit limit is $200,000 for all card tiers
        String result = account.deposit(205000);
        Assert.assertEquals("DAILY_LIMIT_EXCEEDED", result);
    }

    @Test
    @DisplayName("When incoming transfer exceeds destination's deposit limit then it is blocked")
    public final void whenIncomingTransferExceedsLimitThenBlocked() {
        Card highLimitSenderCard = new MastercardPlatinum("4000123456789097");
        CheckingAccount highLimitSender = new CheckingAccount("CHK-003", highLimitSenderCard);
        highLimitSender.deposit(150000);

        String result = highLimitSender.transferTo(otherAccount, 150000, false);
        Assert.assertEquals("DAILY_LIMIT_EXCEEDED", result);
    }
}