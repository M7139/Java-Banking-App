package com.ga.project;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void bankerReturnsCorrectRole() {
        User banker = new Banker("B001", "Saad Iqbal", "hashedPassword");
        Assert.assertEquals("Banker", banker.getRole());
    }

    @Test
    public void customerReturnsCorrectRole() {
        User customer = new Customer("C001", "Melvin Gordon", "hashedPassword");
        Assert.assertEquals("Customer", customer.getRole());
    }

}