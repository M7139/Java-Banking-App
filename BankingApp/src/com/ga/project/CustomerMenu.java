package com.ga.project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CustomerMenu {

    private Scanner scanner;
    private Bank bank;
    private TransactionHistory transactionHistory;
    private TransactionFilterService filterService;

    public CustomerMenu(Scanner scanner, Bank bank) {
        this.scanner = scanner;
        this.bank = bank;
        this.transactionHistory = new TransactionHistory();
        this.filterService = new TransactionFilterService();
    }

    public void show(Customer customer) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("Welcome, " + customer.getFullName());
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
                    handleTransactionHistory(customer);
                    break;

                case "5":
                    handleFilterTransactionHistory(customer);
                    break;

                case "6":
                    handleAccountStatement(customer);
                    break;

                case "7":
                    running = false;
                    System.out.println("Logged out.");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private double readAmount() {
        System.out.print("Enter amount: ");

        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return -1;
        }
    }

    private Account chooseAccount(Customer customer) {
        List<Account> accounts = new ArrayList<>();

        customer.getCheckingAccount().ifPresent(accounts::add);
        customer.getSavingsAccount().ifPresent(accounts::add);

        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return null;
        }

        System.out.println("\nChoose account:");

        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);

            String accountName;

            if (account instanceof CheckingAccount) {
                accountName = "Checking";
            } else {
                accountName = "Saving";
            }

            System.out.println(
                    (i + 1) + ". "
                            + accountName
                            + " | Balance: $"
                            + account.getBalance()
            );
        }

        System.out.print("Choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice < 1 || choice > accounts.size()) {
                System.out.println("Invalid account.");
                return null;
            }

            return accounts.get(choice - 1);

        } catch (NumberFormatException e) {
            System.out.println("Invalid account.");
            return null;
        }
    }

    private Account chooseAccountWithoutBalance(Customer customer) {
        List<Account> accounts = new ArrayList<>();

        customer.getCheckingAccount().ifPresent(accounts::add);
        customer.getSavingsAccount().ifPresent(accounts::add);

        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return null;
        }

        System.out.println("\nChoose account:");

        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);

            String accountName;

            if (account instanceof CheckingAccount) {
                accountName = "Checking";
            } else {
                accountName = "Saving";
            }

            System.out.println(
                    (i + 1) + ". " + accountName
            );
        }

        System.out.print("Choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice < 1 || choice > accounts.size()) {
                System.out.println("Invalid account.");
                return null;
            }

            return accounts.get(choice - 1);

        } catch (NumberFormatException e) {
            System.out.println("Invalid account.");
            return null;
        }
    }

    private void handleDeposit(Customer customer) {
        Account account = chooseAccount(customer);

        if (account == null) {
            return;
        }

        double amount = readAmount();

        if (amount <= 0) {
            return;
        }

        String result = bank.deposit(customer, account, amount);

        System.out.println("Deposit result: " + result);

        if (result.equals("SUCCESS")) {
            System.out.println("New balance: $" + account.getBalance());
        }
    }

    private void handleWithdraw(Customer customer) {
        Account account = chooseAccount(customer);

        if (account == null) {
            return;
        }

        double amount = readAmount();

        if (amount <= 0) {
            return;
        }

        String result = bank.withdraw(customer, account, amount);

        System.out.println("Withdrawal result: " + result);

        if (result.equals("SUCCESS")) {
            System.out.println("New balance: $" + account.getBalance());
        }
    }

    private void handleTransfer(Customer customer) {
        System.out.println("\n--- Transfer ---");

        System.out.println("Choose the account to transfer from:");
        Account source = chooseAccount(customer);

        if (source == null) {
            return;
        }

        System.out.print("Transfer to your own account? (y/n): ");
        String answer = scanner.nextLine();

        boolean isOwnAccount = answer.equalsIgnoreCase("y");

        Account destination;
        Customer destinationCustomer;

        if (isOwnAccount) {

            if (source instanceof CheckingAccount) {
                Optional<SavingsAccount> savings =
                        customer.getSavingsAccount();

                if (savings.isEmpty()) {
                    System.out.println(
                            "You do not have another account to transfer to."
                    );
                    return;
                }

                destination = savings.get();

            } else {
                Optional<CheckingAccount> checking =
                        customer.getCheckingAccount();

                if (checking.isEmpty()) {
                    System.out.println(
                            "You do not have another account to transfer to."
                    );
                    return;
                }

                destination = checking.get();
            }

            destinationCustomer = customer;

        } else {
            System.out.print("Enter recipient customer ID: ");
            String recipientId = scanner.nextLine();

            Optional<User> user = bank.findUserById(recipientId);

            if (user.isEmpty() || !(user.get() instanceof Customer)) {
                System.out.println("Customer not found.");
                return;
            }

            destinationCustomer = (Customer) user.get();

            destination =
                    chooseAccountWithoutBalance(destinationCustomer);

            if (destination == null) {
                return;
            }
        }

        double amount = readAmount();

        if (amount <= 0) {
            return;
        }

        String result = bank.transfer(
                customer,
                source,
                destinationCustomer,
                destination,
                amount,
                isOwnAccount
        );

        System.out.println("Transfer result: " + result);

        if (result.equals("SUCCESS")) {
            System.out.println(
                    "New balance: $" + source.getBalance()
            );
        }
    }

    private List<Transaction> getAllTransactions(Customer customer) {
        List<Transaction> transactions = new ArrayList<>();

        customer.getCheckingAccount()
                .ifPresent(account ->
                        transactions.addAll(account.getTransactions()));

        customer.getSavingsAccount()
                .ifPresent(account ->
                        transactions.addAll(account.getTransactions()));

        transactions.sort(
                (t1, t2) ->
                        t1.getTimestamp().compareTo(t2.getTimestamp())
        );

        return transactions;
    }

    private void handleTransactionHistory(Customer customer) {
        List<Transaction> transactions =
                getAllTransactions(customer);

        System.out.println(
                transactionHistory.display(customer, transactions)
        );
    }

    private void handleFilterTransactionHistory(Customer customer) {
        List<Transaction> transactions =
                getAllTransactions(customer);

        System.out.println("\n--- Filter Transactions ---");
        System.out.println("1. Today");
        System.out.println("2. Yesterday");
        System.out.println("3. Last Week");
        System.out.println("4. Last 7 Days");
        System.out.println("5. Last Month");
        System.out.println("6. Last 30 Days");
        System.out.println("7. Custom Date Range");
        System.out.print("Choose filter: ");

        String choice = scanner.nextLine();

        List<Transaction> filteredTransactions;

        switch (choice) {
            case "1":
                filteredTransactions =
                        filterService.filterToday(transactions);
                break;

            case "2":
                filteredTransactions =
                        filterService.filterYesterday(transactions);
                break;

            case "3":
                filteredTransactions =
                        filterService.filterLastWeek(transactions);
                break;

            case "4":
                filteredTransactions =
                        filterService.filterLast7Days(transactions);
                break;

            case "5":
                filteredTransactions =
                        filterService.filterLastMonth(transactions);
                break;

            case "6":
                filteredTransactions =
                        filterService.filterLast30Days(transactions);
                break;

            case "7":
                filteredTransactions =
                        handleCustomDateFilter(transactions);
                break;

            default:
                System.out.println("Invalid option.");
                return;
        }

        System.out.println(
                transactionHistory.display(
                        customer,
                        filteredTransactions
                )
        );
    }

    private List<Transaction> handleCustomDateFilter(
            List<Transaction> transactions) {

        try {
            System.out.print("Enter start date (yyyy-MM-dd): ");
            LocalDate startDate =
                    LocalDate.parse(scanner.nextLine());

            System.out.print("Enter end date (yyyy-MM-dd): ");
            LocalDate endDate =
                    LocalDate.parse(scanner.nextLine());

            if (endDate.isBefore(startDate)) {
                System.out.println(
                        "End date cannot be before start date."
                );

                return new ArrayList<>();
            }

            LocalDateTime start =
                    startDate.atStartOfDay();

            LocalDateTime end =
                    endDate.plusDays(1)
                            .atStartOfDay()
                            .minusNanos(1);

            return filterService.filterByDateRange(
                    transactions,
                    start,
                    end
            );

        } catch (DateTimeParseException e) {
            System.out.println(
                    "Invalid format. Use yyyy-MM-dd"
            );

            return new ArrayList<>();
        }
    }

    private void handleAccountStatement(Customer customer) {
        Account account = chooseAccount(customer);

        if (account == null) {
            return;
        }

        System.out.println(
                transactionHistory.generateStatement(
                        customer,
                        account
                )
        );
    }
}