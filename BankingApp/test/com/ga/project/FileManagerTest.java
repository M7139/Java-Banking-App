package com.ga.project;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class FileManagerTest {

    @Test
    public void whenBankersSavedAndLoadedThenDataMatches() {
        FileManager fileManager = new FileManager();
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();

        Banker banker1 = new Banker("B001", "Saad", "Iqbal", passwordEncryptor.encrypt("bankerpass"));
        List<Banker> bankers = Arrays.asList(banker1);
        fileManager.saveBankers(bankers);

        List<Banker> loadedBankers = fileManager.loadBankers();

        Assert.assertEquals(1, loadedBankers.size());
        Assert.assertEquals("B001", loadedBankers.get(0).getId());
        Assert.assertEquals("Saad", loadedBankers.get(0).getFirstName());
        Assert.assertEquals("Iqbal", loadedBankers.get(0).getLastName());
    }

    @Test
    public void whenCustomersSavedAndLoadedThenDataMatches() {
        FileManager fileManager = new FileManager();
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
        AddCustomer addCustomer = new AddCustomer(passwordEncryptor);
        Banker banker = new Banker("B001", "Saad", "Iqbal", passwordEncryptor.encrypt("bankerpass"));

        Customer customer1 = addCustomer.addNewCustomer(banker, "10001", "Melvin", "Gordon", "pass123", true, true, "PLATINUM");
        customer1.getCheckingAccount().get().deposit(500);
        customer1.getSavingsAccount().get().deposit(2000);

        Customer customer2 = addCustomer.addNewCustomer(banker, "10002", "Stacey", "Abrams", "pass456", true, false, "TITANIUM");
        customer2.getCheckingAccount().get().deposit(1000);

        List<Customer> customers = Arrays.asList(customer1, customer2);
        fileManager.saveCustomers(customers);

        List<Customer> loadedCustomers = fileManager.loadCustomers();

        Assert.assertEquals(2, loadedCustomers.size());

        Customer loaded1 = loadedCustomers.get(0);
        Assert.assertEquals("10001", loaded1.getId());
        Assert.assertEquals("Melvin", loaded1.getFirstName());
        Assert.assertTrue(loaded1.getCheckingAccount().isPresent());
        Assert.assertEquals(500.0, loaded1.getCheckingAccount().get().getBalance(), 0.001);
        Assert.assertTrue(loaded1.getSavingsAccount().isPresent());
        Assert.assertEquals(2000.0, loaded1.getSavingsAccount().get().getBalance(), 0.001);

        Customer loaded2 = loadedCustomers.get(1);
        Assert.assertEquals("10002", loaded2.getId());
        Assert.assertTrue(loaded2.getCheckingAccount().isPresent());
        Assert.assertEquals(1000.0, loaded2.getCheckingAccount().get().getBalance(), 0.001);
        Assert.assertFalse(loaded2.getSavingsAccount().isPresent());
    }
}