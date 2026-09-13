package com.ga.project;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthenticationTest {

    Authentication authentication;
    PasswordEncryptor passwordEncryptor;
    Customer customer;
    String rawPassword = "myPassword123";

    @BeforeEach
    public void setUp() {
        passwordEncryptor = new PasswordEncryptor();
        authentication = new Authentication(passwordEncryptor);
        String encrypted = passwordEncryptor.encrypt(rawPassword);
        customer = new Customer("10001", "Saad", "Iqbal", encrypted);
    }

    @Test
    @DisplayName("When password is correct then login returns SUCCESS and resets failed count")
    public final void whenPasswordIsCorrectThenLoginSucceeds() {
        String result = authentication.login(customer, rawPassword);
        Assert.assertEquals("SUCCESS", result);
        Assert.assertEquals(0, customer.getFailedLoginCount());
        Assert.assertNull(customer.getLockedUntil());
    }

    @Test
    @DisplayName("When password is wrong then login returns INVALID_PASSWORD")
    public final void whenPasswordIsWrongThenInvalidPasswordReturned() {
        String result = authentication.login(customer, "wrongPassword");
        Assert.assertEquals("INVALID_PASSWORD", result);
        Assert.assertEquals(1, customer.getFailedLoginCount());
    }

    @Test
    @DisplayName("When 3 failed attempts happen then next login returns LOCKED")
    public final void whenThreeFailedAttemptsThenAccountLocks() {
        authentication.login(customer, "wrongPassword");
        authentication.login(customer, "wrongPassword");
        authentication.login(customer, "wrongPassword");

        String result = authentication.login(customer, rawPassword);
        Assert.assertEquals("LOCKED", result);
    }
}