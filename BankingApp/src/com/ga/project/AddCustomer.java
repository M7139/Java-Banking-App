package com.ga.project;

import java.util.Optional;

public class AddCustomer {
    private PasswordEncryptor passwordEncryptor;

    public AddCustomer(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

    public Customer addNewCustomer(String id, String firstName, String lastName, String rawPassword,
                                   boolean addChecking, boolean addSavings, String cardType) {

        String encryptedPassword = passwordEncryptor.encrypt(rawPassword);
        Customer customer = new Customer(id, firstName, lastName, encryptedPassword);

        if (addChecking) {
            Card card = createCard(cardType, id + "-CHK-CARD");
            CheckingAccount checking = new CheckingAccount("CHK-" + id, card);
            customer.setCheckingAccount(Optional.of(checking));
        }

        if (addSavings) {
            Card card = createCard(cardType, id + "-SAV-CARD");
            SavingsAccount savings = new SavingsAccount("SAV-" + id, card);
            customer.setSavingsAccount(Optional.of(savings));
        }

        return customer;
    }

    private Card createCard(String cardType, String cardNumber) {
        switch (cardType.toUpperCase()) {
            case "PLATINUM":
                return new MastercardPlatinum(cardNumber);
            case "TITANIUM":
                return new MastercardTitanium(cardNumber);
            default:
                return new Mastercard(cardNumber);
        }
    }
}