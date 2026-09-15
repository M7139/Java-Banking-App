package com.ga.project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionFilterService {

    public List<Transaction> filter(List<Transaction> transactions, TransactionFilter condition) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (condition.matches(t)) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Transaction> filterToday(List<Transaction> transactions) {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        return filter(transactions, t -> !t.getTimestamp().isBefore(startOfToday));
    }

    public List<Transaction> filterYesterday(List<Transaction> transactions) {
        LocalDateTime startOfYesterday = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        return filter(transactions, t ->
                !t.getTimestamp().isBefore(startOfYesterday) && t.getTimestamp().isBefore(startOfToday));
    }

    public List<Transaction> filterLast7Days(List<Transaction> transactions) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return filter(transactions, t -> t.getTimestamp().isAfter(sevenDaysAgo));
    }

    public List<Transaction> filterLast30Days(List<Transaction> transactions) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return filter(transactions, t -> t.getTimestamp().isAfter(thirtyDaysAgo));
    }

    public List<Transaction> filterByDateRange(List<Transaction> transactions, LocalDateTime start, LocalDateTime end) {
        return filter(transactions, t ->
                !t.getTimestamp().isBefore(start) && !t.getTimestamp().isAfter(end));
    }
}