package com.ga.project;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileManager {
    private static final String BANKERS_FILE = "bankers.txt";
    private static final String CUSTOMERS_FILE = "customers.txt";

    public void saveBankers(List<Banker> bankers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BANKERS_FILE))) {
            for (Banker banker : bankers) {
                writer.println(bankerToLine(banker));
            }
        } catch (IOException e) {
            System.out.println("Error saving bankers file: " + e.getMessage());
        }
    }

    public List<Banker> loadBankers() {
        List<Banker> bankers = new ArrayList<>();
        File file = new File(BANKERS_FILE);

        if (!file.exists()) {
            return bankers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    bankers.add(lineToBanker(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading bankers file: " + e.getMessage());
        }

        return bankers;
    }

    private String bankerToLine(Banker banker) {
        return banker.getId() + "," +
                banker.getFirstName() + "," +
                banker.getLastName() + "," +
                banker.getEncryptedPassword();
    }

    private Banker lineToBanker(String line) {
        String[] fields = line.split(",", -1);
        String id = fields[0];
        String firstName = fields[1];
        String lastName = fields[2];
        String encryptedPassword = fields[3];

        return new Banker(id, firstName, lastName, encryptedPassword);
    }

    public void saveCustomers(List<Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : customers) {
                writer.println(customerToLine(customer));
            }
        } catch (IOException e) {
            System.out.println("Error saving customers file: " + e.getMessage());
        }
    }

    public List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(CUSTOMERS_FILE);

        if (!file.exists()) {
            return customers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    customers.add(lineToCustomer(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading customers file: " + e.getMessage());
        }

        return customers;
    }

    private String customerToLine(Customer customer) {
        boolean hasChecking = customer.getCheckingAccount().isPresent();
        boolean hasSavings = customer.getSavingsAccount().isPresent();

        double checkingBalance = hasChecking ? customer.getCheckingAccount().get().getBalance() : 0;
        double savingsBalance = hasSavings ? customer.getSavingsAccount().get().getBalance() : 0;

        String checkingFlag = hasChecking ? "CHK" : "";
        String savingsFlag = hasSavings ? "SAV" : "";

        String cardType = "MASTERCARD";
        if (hasChecking) {
            cardType = getCardTypeName(customer.getCheckingAccount().get().getCard());
        } else if (hasSavings) {
            cardType = getCardTypeName(customer.getSavingsAccount().get().getCard());
        }

        return customer.getId() + "," +
                customer.getFirstName() + "," +
                customer.getLastName() + "," +
                customer.getEncryptedPassword() + "," +
                checkingFlag + "," +
                checkingBalance + "," +
                savingsFlag + "," +
                savingsBalance + "," +
                cardType;
    }

    private String getCardTypeName(Card card) {
        if (card instanceof MastercardPlatinum) {
            return "PLATINUM";
        } else if (card instanceof MastercardTitanium) {
            return "TITANIUM";
        } else {
            return "MASTERCARD";
        }
    }

    private Customer lineToCustomer(String line) {
        String[] fields = line.split(",", -1);
        String id = fields[0];
        String firstName = fields[1];
        String lastName = fields[2];
        String encryptedPassword = fields[3];
        boolean hasChecking = fields[4].equals("CHK");
        double checkingBalance = Double.parseDouble(fields[5]);
        boolean hasSavings = fields[6].equals("SAV");
        double savingsBalance = Double.parseDouble(fields[7]);
        String cardType = fields[8];

        Customer customer = new Customer(id, firstName, lastName, encryptedPassword);

        if (hasChecking) {
            Card card = createCard(cardType, id + "-CHK-CARD");
            CheckingAccount checking = new CheckingAccount("CHK-" + id, card);
            checking.setBalance(checkingBalance);
            customer.setCheckingAccount(Optional.of(checking));
        }

        if (hasSavings) {
            Card card = createCard(cardType, id + "-SAV-CARD");
            SavingsAccount savings = new SavingsAccount("SAV-" + id, card);
            savings.setBalance(savingsBalance);
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

    public void appendTransaction(Customer customer, Transaction transaction) {
        String fileName = getTransactionFileName(customer);

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(transactionToLine(transaction));
        } catch (IOException e) {
            System.out.println("Error saving transaction file: " + e.getMessage());
        }
    }

    public List<Transaction> loadTransactions(Customer customer) {
        List<Transaction> transactions = new ArrayList<>();
        String fileName = getTransactionFileName(customer);
        File file = new File(fileName);

        if (!file.exists()) {
            return transactions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    transactions.add(lineToTransaction(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading transaction file: " + e.getMessage());
        }

        return transactions;
    }

    private String getTransactionFileName(Customer customer) {
        return "Transactions-" + customer.getFirstName() + customer.getLastName() + "-" + customer.getId() + ".txt";
    }

    private String transactionToLine(Transaction transaction) {
        return transaction.getTransactionId() + "," +
                transaction.getAccountType() + "," +
                transaction.getType() + "," +
                transaction.getAmount() + "," +
                transaction.getPostBalance() + "," +
                transaction.getTimestamp();
    }

    private Transaction lineToTransaction(String line) {
        String[] fields = line.split(",", -1);
        String transactionId = fields[0];
        String accountType = fields[1];
        String type = fields[2];
        double amount = Double.parseDouble(fields[3]);
        double postBalance = Double.parseDouble(fields[4]);
        LocalDateTime timestamp = LocalDateTime.parse(fields[5]);

        return new Transaction(transactionId, accountType, type, amount, postBalance, timestamp);
    }
}