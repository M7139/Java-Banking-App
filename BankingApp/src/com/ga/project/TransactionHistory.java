package com.ga.project;

import java.util.List;

public class TransactionHistory {

    public String display(Customer customer, List<Transaction> transactions) {
        StringBuilder result = new StringBuilder();
        result.append("Transaction History for ").append(customer.getFullName()).append("\n");

        if (transactions.isEmpty()) {
            result.append("No transactions found.\n");
            return result.toString();
        }

        for (Transaction t : transactions) {
            result.append(t.getTimestamp())
                    .append(" | ").append(t.getAccountType())
                    .append(" | ").append(t.getType())
                    .append(" | Amount: ").append(t.getAmount())
                    .append(" | Balance after: ").append(t.getPostBalance())
                    .append("\n");
        }

        return result.toString();
    }
}