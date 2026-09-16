package com.ga.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CustomerMenu {
    private Scanner scanner;
    private Bank bank;

    public CustomerMenu(Scanner scanner, Bank bank) {
        this.scanner = scanner;
        this.bank = bank;
    }

    public void show(Customer customer) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. View Transaction History");
            System.out.println("5. Filter Transaction History");
            System.out.println("6. View Account Statement");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleDeposit(customer);
                    break;
                case "2":
                    handleWithdraw(customer);
                    break;
                case "3":
                    handleTransfer(customer);
                    break;
                case "4":
                    handleViewHistory(customer);
                    break;
                case "5":
                    handleFilterHistory(customer);
                    break;
                case "6":
                    handleViewStatement(customer);
                    break;
                case "7":
                    running = false;
                    System.out.println("Logging out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private double readAmount() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number, please try again: ");
            }
        }
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            System.out.println(prompt);
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                return true;
            } else if (choice.equals("2")) {
                return false;
            } else {
                System.out.println("Invalid option, please enter 1 or 2.");
            }
        }
    }

    private Optional<Account> chooseAccount(Customer customer) {
        return selectAccount(customer, true);
    }

    private Optional<Account> chooseRecipientAccount(Customer recipient) {
        return selectAccount(recipient, false);
    }

    private Optional<Account> selectAccount(Customer accountOwner, boolean showBalance) {
        while (true) {
            List<Account> options = new ArrayList<>();

            System.out.println("Choose account:");
            int optionNumber = 1;

            if (accountOwner.getCheckingAccount().isPresent()) {
                Account checking = accountOwner.getCheckingAccount().get();
                String label = optionNumber + ". Checking" + (showBalance ? " (balance: " + checking.getBalance() + ")" : "");
                System.out.println(label);
                options.add(checking);
                optionNumber++;
            }

            if (accountOwner.getSavingsAccount().isPresent()) {
                Account savings = accountOwner.getSavingsAccount().get();
                String label = optionNumber + ". Savings" + (showBalance ? " (balance: " + savings.getBalance() + ")" : "");
                System.out.println(label);
                options.add(savings);
                optionNumber++;
            }

            System.out.println(optionNumber + ". Back to main menu");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            try {
                int selected = Integer.parseInt(choice);

                if (selected == optionNumber) {
                    return Optional.empty();
                }

                if (selected >= 1 && selected <= options.size()) {
                    return Optional.of(options.get(selected - 1));
                }

                System.out.println("Invalid option, please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void handleDeposit(Customer customer) {
        Optional<Account> account = chooseAccount(customer);
        if (!account.isPresent()) {
            return;
        }

        System.out.print("Enter amount to deposit: ");
        double amount = readAmount();

        String result = bank.deposit(customer, account.get(), amount);
        System.out.println("Result: " + result);
    }

    private void handleWithdraw(Customer customer) {
        Optional<Account> account = chooseAccount(customer);
        if (!account.isPresent()) {
            return;
        }

        System.out.print("Enter amount to withdraw: ");
        double amount = readAmount();

        String result = bank.withdraw(customer, account.get(), amount);
        System.out.println("Result: " + result);
    }

    private void handleTransfer(Customer customer) {
        System.out.println("Transfer from:");
        Optional<Account> senderAccount = chooseAccount(customer);
        if (!senderAccount.isPresent()) {
            return;
        }

        boolean isOwnAccount = readYesNo("Transfer to your own other account?");

        Account destinationAccount;
        Customer destinationCustomer;

        if (isOwnAccount) {
            boolean isCheckingSender = senderAccount.get() == customer.getCheckingAccount().orElse(null);
            if (isCheckingSender && customer.getSavingsAccount().isPresent()) {
                destinationAccount = customer.getSavingsAccount().get();
            } else if (!isCheckingSender && customer.getCheckingAccount().isPresent()) {
                destinationAccount = customer.getCheckingAccount().get();
            } else {
                System.out.println("You don't have a second account to transfer to.");
                return;
            }
            destinationCustomer = customer;
        } else {
            System.out.print("Enter recipient's customer ID: ");
            String recipientId = scanner.nextLine();
            Optional<User> recipient = bank.findUserById(recipientId);

            if (!recipient.isPresent() || !(recipient.get() instanceof Customer)) {
                System.out.println("Recipient not found.");
                return;
            }

            destinationCustomer = (Customer) recipient.get();
            Optional<Account> recipientAccount = chooseRecipientAccount(destinationCustomer);
            if (!recipientAccount.isPresent()) {
                return;
            }
            destinationAccount = recipientAccount.get();
        }

        System.out.print("Enter amount to transfer: ");
        double amount = readAmount();

        String result = bank.transfer(customer, senderAccount.get(), destinationCustomer, destinationAccount, amount, isOwnAccount);
        System.out.println("Result: " + result);
    }

    private void handleViewHistory(Customer customer) {
        TransactionHistory transactionHistory = new TransactionHistory();
        List<Transaction> allTransactions = new ArrayList<>();

        if (customer.getCheckingAccount().isPresent()) {
            allTransactions.addAll(customer.getCheckingAccount().get().getTransactions());
        }
        if (customer.getSavingsAccount().isPresent()) {
            allTransactions.addAll(customer.getSavingsAccount().get().getTransactions());
        }

        System.out.println(transactionHistory.display(customer, allTransactions));
    }

    private void handleFilterHistory(Customer customer) {
        List<Transaction> allTransactions = new ArrayList<>();
        if (customer.getCheckingAccount().isPresent()) {
            allTransactions.addAll(customer.getCheckingAccount().get().getTransactions());
        }
        if (customer.getSavingsAccount().isPresent()) {
            allTransactions.addAll(customer.getSavingsAccount().get().getTransactions());
        }

        System.out.println("Filter by:");
        System.out.println("1. Today");
        System.out.println("2. Yesterday");
        System.out.println("3. Last 7 days");
        System.out.println("4. Last 30 days");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine();

        TransactionFilterService filterService = new TransactionFilterService();
        List<Transaction> filtered;

        switch (choice) {
            case "1":
                filtered = filterService.filterToday(allTransactions);
                break;
            case "2":
                filtered = filterService.filterYesterday(allTransactions);
                break;
            case "3":
                filtered = filterService.filterLast7Days(allTransactions);
                break;
            case "4":
                filtered = filterService.filterLast30Days(allTransactions);
                break;
            default:
                System.out.println("Invalid option.");
                return;
        }

        TransactionHistory transactionHistory = new TransactionHistory();
        System.out.println(transactionHistory.display(customer, filtered));
    }

    private void handleViewStatement(Customer customer) {
        TransactionHistory transactionHistory = new TransactionHistory();
        Optional<Account> account = chooseAccount(customer);

        if (!account.isPresent()) {
            return;
        }

        System.out.println(transactionHistory.generateStatement(customer, account.get()));
    }
}