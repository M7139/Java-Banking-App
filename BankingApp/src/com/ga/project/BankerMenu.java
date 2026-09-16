package com.ga.project;

import java.util.Scanner;

public class BankerMenu {
    private Scanner scanner;
    private Bank bank;

    public BankerMenu(Scanner scanner, Bank bank) {
        this.scanner = scanner;
        this.bank = bank;
    }

    public void show(Banker banker) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Banker Menu ---");
            System.out.println("1. Add New Customer");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleAddCustomer(banker);
                    break;
                case "2":
                    running = false;
                    System.out.println("Logging out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private Boolean readYesNoOrCancel(String prompt) {
        while (true) {
            System.out.println(prompt);
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("3. Cancel and return to main menu");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                return true;
            } else if (choice.equals("2")) {
                return false;
            } else if (choice.equals("3")) {
                return null;
            } else {
                System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void handleAddCustomer(Banker banker) {
        System.out.print("Enter new customer ID: ");
        String id = scanner.nextLine();

        if (bank.findUserById(id).isPresent()) {
            System.out.println("A user with that ID already exists.");
            return;
        }

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Boolean addChecking = readYesNoOrCancel("Add checking account?");
        if (addChecking == null) {
            System.out.println("Cancelled.");
            return;
        }

        Boolean addSavings = readYesNoOrCancel("Add savings account?");
        if (addSavings == null) {
            System.out.println("Cancelled.");
            return;
        }

        if (!addChecking && !addSavings) {
            System.out.println("Customer must have at least one account.");
            return;
        }

        String cardType = "";

        while (true) {
            System.out.println("Choose card type:");
            System.out.println("1. Mastercard");
            System.out.println("2. Mastercard Titanium");
            System.out.println("3. Mastercard Platinum");
            System.out.print("Enter choice: ");

            String cardChoice = scanner.nextLine();

            if (cardChoice.equals("1")) {
                cardType = "MASTERCARD";
                break;
            } else if (cardChoice.equals("2")) {
                cardType = "TITANIUM";
                break;
            } else if (cardChoice.equals("3")) {
                cardType = "PLATINUM";
                break;
            } else {
                System.out.println("Invalid option, please try again.");
            }
        }

        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
        AddCustomer addCustomer = new AddCustomer(passwordEncryptor);

        Customer customer = addCustomer.addNewCustomer(
                id,
                firstName,
                lastName,
                password,
                addChecking,
                addSavings,
                cardType
        );

        bank.addCustomer(customer);

        System.out.println(
                "Customer " + customer.getFullName()
                        + " added successfully with ID "
                        + id + "."
        );
    }
}