package com.ga.project;

public class SeedData {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
        AddCustomer addCustomer = new AddCustomer(passwordEncryptor);

        Bank bank = new Bank(fileManager);

        Banker banker = new Banker("B001", "Saad", "Kahn", passwordEncryptor.encrypt("bankerpass"));
        bank.getBankers().add(banker);
        bank.saveBankers();

        Customer customer = addCustomer.addNewCustomer("C001", "Mohamed", "Aljaomee", "custpass", true, true, "PLATINUM");
        customer.getCheckingAccount().get().deposit(1000);
        customer.getSavingsAccount().get().deposit(500);
        bank.addCustomer(customer);

        Customer customer2 = addCustomer.addNewCustomer("C002", "Rashad", "Abdulla", "custpass", true, true, "PLATINUM");
        customer2.getCheckingAccount().get().deposit(1000);
        customer2.getSavingsAccount().get().deposit(500);
        bank.addCustomer(customer2);

        System.out.println("Seed data created.");
        System.out.println("Banker login -> ID: B001, password: bankerpass");
        System.out.println("Customer login -> ID: 10001, password: custpass");
    }
}