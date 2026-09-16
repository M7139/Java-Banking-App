package com.ga.project;

import java.util.List;
import java.util.Optional;

public class Bank {
    private FileManager fileManager;
    private List<Banker> bankers;
    private List<Customer> customers;

    public Bank(FileManager fileManager) {
        this.fileManager = fileManager;
        this.bankers = fileManager.loadBankers();
        this.customers = fileManager.loadCustomers();

        for (Customer customer : customers) {
            List<Transaction> history = fileManager.loadTransactions(customer);

            for (Transaction transaction : history) {
                if (transaction.getAccountType().equals("CHECKING") && customer.getCheckingAccount().isPresent()) {
                    customer.getCheckingAccount().get().getTransactions().add(transaction);
                } else if (transaction.getAccountType().equals("SAVINGS") && customer.getSavingsAccount().isPresent()) {
                    customer.getSavingsAccount().get().getTransactions().add(transaction);
                }
            }
        }
    }

    public List<Banker> getBankers() {
        return bankers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public Optional<User> findUserById(String id) {
        for (Banker banker : bankers) {
            if (banker.getId().equals(id)) {
                return Optional.of(banker);
            }
        }
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                return Optional.of(customer);
            }
        }
        return Optional.empty();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomers();
    }

    public void saveCustomers() {
        fileManager.saveCustomers(customers);
    }

    public void saveBankers() {
        fileManager.saveBankers(bankers);
    }

    public void recordTransaction(Customer customer, Transaction transaction) {
        fileManager.appendTransaction(customer, transaction);
    }

    public String deposit(Customer customer, Account account, double amount) {
        String result = account.deposit(amount);

        if (result.equals("SUCCESS")) {
            Transaction transaction = account.getTransactions().get(account.getTransactions().size() - 1);
            recordTransaction(customer, transaction);
            saveCustomers();
        }

        return result;
    }

    public String withdraw(Customer customer, Account account, double amount) {
        String result = account.withdraw(amount);

        if (result.equals("SUCCESS")) {
            Transaction transaction = account.getTransactions().get(account.getTransactions().size() - 1);
            recordTransaction(customer, transaction);
            saveCustomers();
        }

        return result;
    }

    public String transfer(Customer senderCustomer, Account senderAccount,
                           Customer destinationCustomer, Account destinationAccount,
                           double amount, boolean isOwnAccount) {

        String result = senderAccount.transferTo(destinationAccount, amount, isOwnAccount);

        if (result.equals("SUCCESS")) {
            Transaction senderTransaction = senderAccount.getTransactions().get(senderAccount.getTransactions().size() - 1);
            recordTransaction(senderCustomer, senderTransaction);

            Transaction destinationTransaction = destinationAccount.getTransactions().get(destinationAccount.getTransactions().size() - 1);
            recordTransaction(destinationCustomer, destinationTransaction);

            saveCustomers();
        }

        return result;
    }
}