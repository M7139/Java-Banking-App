package com.ga.project;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AddCustomerTest {

    AddCustomer addCustomer;
    PasswordEncryptor passwordEncryptor;
    Banker banker;

    @BeforeEach
    public void setUp() {
        passwordEncryptor = new PasswordEncryptor();
        addCustomer = new AddCustomer(passwordEncryptor);
        banker = new Banker("B001", "Saad", "Iqbal", passwordEncryptor.encrypt("bankerpass"));
    }

    @Test
    @DisplayName("When adding a customer with only checking then only checking account is present")
    public final void whenAddingCustomerWithOnlyCheckingThenOnlyCheckingPresent() {
        Customer customer = addCustomer.addNewCustomer(banker, "10001", "Saad", "Iqbal", "pass123", true, false, "PLATINUM");

        Assert.assertTrue(customer.getCheckingAccount().isPresent());
        Assert.assertFalse(customer.getSavingsAccount().isPresent());
        Assert.assertEquals("CHK-10001", customer.getCheckingAccount().get().getAccountNumber());
    }

    @Test
    @DisplayName("When adding a customer with only savings then only savings account is present")
    public final void whenAddingCustomerWithOnlySavingsThenOnlySavingsPresent() {
        Customer customer = addCustomer.addNewCustomer(banker, "10002", "Saad", "Khan", "pass123", false, true, "TITANIUM");

        Assert.assertFalse(customer.getCheckingAccount().isPresent());
        Assert.assertTrue(customer.getSavingsAccount().isPresent());
        Assert.assertEquals("SAV-10002", customer.getSavingsAccount().get().getAccountNumber());
    }

    @Test
    @DisplayName("When adding a customer with both accounts then both are present")
    public final void whenAddingCustomerWithBothThenBothPresent() {
        Customer customer = addCustomer.addNewCustomer(banker, "10003", "Melvin", "Gordon", "pass123", true, true, "MASTERCARD");

        Assert.assertTrue(customer.getCheckingAccount().isPresent());
        Assert.assertTrue(customer.getSavingsAccount().isPresent());
    }

    @Test
    @DisplayName("When adding a customer then password is encrypted, not stored in plain text")
    public final void whenAddingCustomerThenPasswordIsEncrypted() {
        Customer customer = addCustomer.addNewCustomer(banker, "10004", "Stacey", "Abrams", "pass123", true, false, "PLATINUM");

        Assert.assertNotEquals("pass123", customer.getEncryptedPassword());
        Assert.assertTrue(passwordEncryptor.verify("pass123", customer.getEncryptedPassword()));
    }

    @Test
    @DisplayName("When adding a customer with PLATINUM card then card limits match Platinum tier")
    public final void whenAddingCustomerWithPlatinumThenLimitsMatch() {
        Customer customer = addCustomer.addNewCustomer(banker, "10005", "Micheal", "Paul", "pass123", true, false, "PLATINUM");

        Card card = customer.getCheckingAccount().get().getCard();
        Assert.assertEquals(20000.0, card.getWithdrawLimitPerDay(), 0.001);
    }
}