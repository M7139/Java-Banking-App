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
        String result = checking.transferTo(savings, 200, true);

        Assert.assertEquals("SUCCESS", result);
        Assert.assertEquals(300.0, checking.getBalance(), 0.001);
        Assert.assertEquals(200.0, savings.getBalance(), 0.001);
    }

    @Test
    @DisplayName("When transferring to another customer's account then balances update correctly")
    public final void whenTransferToOtherCustomerThenBalancesUpdate() {
        String result = checking.transferTo(otherCustomerAccount, 150, false);

        Assert.assertEquals("SUCCESS", result);
        Assert.assertEquals(350.0, checking.getBalance(), 0.001);
        Assert.assertEquals(150.0, otherCustomerAccount.getBalance(), 0.001);
    }

    @Test
    @DisplayName("When transfer causes overdraft then fee is applied to sender only")
    public final void whenTransferCausesOverdraftThenFeeAppliedToSenderOnly() {
        String result = checking.transferTo(savings, 600, true);

        Assert.assertEquals("SUCCESS", result);
        // 500 - 600 - 35 fee = -135
        Assert.assertEquals(-135.0, checking.getBalance(), 0.001);
        Assert.assertEquals(600.0, savings.getBalance(), 0.001);
    }

    @Test
    @DisplayName("When transfer is blocked due to deactivated account then destination is untouched")
    public final void whenSenderDeactivatedThenDestinationUnaffected() {
        checking.withdraw(600); // 1st overdraft, balance -135
        checking.deposit(135);  // back to 0
        checking.withdraw(50);  // 2nd overdraft, deactivated

        String result = checking.transferTo(savings, 10, true);

        Assert.assertEquals("ACCOUNT_DEACTIVATED", result);
        Assert.assertEquals(0.0, savings.getBalance(), 0.001);
    }

    @Test
    @DisplayName("When transfer is above withdrawal limit but within transfer limit then transfer succeeds")
    public final void whenTransferWithinTransferLimitThenWithdrawalLimitDoesNotBlockIt() {
        Card senderCard = new MastercardPlatinum("4000111111111111");
        Card receiverCard = new MastercardPlatinum("4000222222222222");

        CheckingAccount sender =
                new CheckingAccount("CHK-100", senderCard);

        CheckingAccount receiver =
                new CheckingAccount("CHK-200", receiverCard);

        sender.deposit(30000);

        // Platinum withdrawal limit = 20,000
        // Platinum transfer to another customer limit = 40,000
        String result = sender.transferTo(receiver, 25000, false);

        Assert.assertEquals("SUCCESS", result);
        Assert.assertEquals(5000.0, sender.getBalance(), 0.001);
        Assert.assertEquals(25000.0, receiver.getBalance(), 0.001);
    }

    @Test
    @DisplayName("When failed transfer occurs then transfer limit is not consumed")
    public final void whenFailedTransferThenTransferLimitIsNotConsumed() {
        Card senderCard = new MastercardPlatinum("4000333333333333");
        Card receiverCard = new MastercardPlatinum("4000444444444444");

        CheckingAccount sender =
                new CheckingAccount("CHK-300", senderCard);

        CheckingAccount receiver =
                new CheckingAccount("CHK-400", receiverCard);

        sender.deposit(50);

        // Causes first overdraft and makes balance negative
        sender.withdraw(100);

        // Balance is negative, so withdrawal over $100 is blocked.
        // This transfer should fail.
        String failedResult =
                sender.transferTo(receiver, 200, false);

        Assert.assertEquals(
                "EXCEEDS_NEGATIVE_BALANCE_LIMIT",
                failedResult
        );

        // Bring account back to positive balance
        sender.deposit(100);

        // Platinum transfer limit to another customer is $40,000.
        // If the failed $200 transfer was incorrectly recorded,
        // this $40,000 transfer would exceed the daily limit.
        String successfulResult =
                sender.transferTo(receiver, 40000, false);

        Assert.assertEquals("SUCCESS", successfulResult);
    }

    @Test
    @DisplayName("When destination rejects transfer then sender transfer limit is not consumed")
    public final void whenDestinationRejectsTransferThenSenderLimitIsNotConsumed() {
        Card senderCard = new MastercardPlatinum("4000555555555555");
        Card receiverCard = new Mastercard("4000666666666666");

        CheckingAccount sender =
                new CheckingAccount("CHK-500", senderCard);

        CheckingAccount receiver =
                new CheckingAccount("CHK-600", receiverCard);

        sender.deposit(50000);

        // Use most of receiver's incoming deposit limit.
        // Normal Mastercard incoming deposit limit = $100,000.
        receiver.getCard().checkAndRecordDeposit(95000, false);

        // Receiver only has $5,000 of incoming deposit limit remaining,
        // so this transfer should fail.
        String failedResult =
                sender.transferTo(receiver, 10000, false);

        Assert.assertEquals(
                "DAILY_LIMIT_EXCEEDED",
                failedResult
        );

        // Use a new destination so its deposit limit does not block us.
        Card secondReceiverCard =
                new MastercardPlatinum("4000777777777777");

        CheckingAccount secondReceiver =
                new CheckingAccount("CHK-700", secondReceiverCard);

        // Platinum sender limit to another customer = $40,000.
        // This should succeed because the failed $10,000 transfer
        // should not have consumed any of the sender's transfer limit.
        String successfulResult =
                sender.transferTo(secondReceiver, 40000, false);

        Assert.assertEquals("SUCCESS", successfulResult);
    }
}