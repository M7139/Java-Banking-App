package com.ga.project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionFilterService {

    public List<Transaction> filter(List<Transaction> transactions, TransactionFilter condition) {
        List<Transaction> filtered = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (condition.matches(transaction)) {
                filtered.add(transaction);
            }
        }

        return filtered;
    }

    public List<Transaction> filterToday(List<Transaction> transactions) {
        LocalDateTime start = LocalDate.now().atStartOfDay();

        return filter(transactions,
                transaction -> !transaction.getTimestamp().isBefore(start));
    }

    public List<Transaction> filterYesterday(List<Transaction> transactions) {
        LocalDateTime start = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime end = LocalDate.now().atStartOfDay();

        return filter(transactions,
                transaction -> !transaction.getTimestamp().isBefore(start)
                        && transaction.getTimestamp().isBefore(end));
    }

    public List<Transaction> filterLastWeek(List<Transaction> transactions) {
        LocalDate today = LocalDate.now();

        LocalDateTime start = today.minusWeeks(1)
                .with(java.time.DayOfWeek.MONDAY)
                .atStartOfDay();

        LocalDateTime end = start.plusWeeks(1);

        return filter(transactions,
                transaction -> !transaction.getTimestamp().isBefore(start)
                        && transaction.getTimestamp().isBefore(end));
    }

    public List<Transaction> filterLast7Days(List<Transaction> transactions) {
        LocalDateTime start = LocalDateTime.now().minusDays(7);

        return filter(transactions,
                transaction -> !transaction.getTimestamp().isBefore(start));
    }

    public List<Transaction> filterLastMonth(List<Transaction> transactions) {
        LocalDate firstDayThisMonth = LocalDate.now().withDayOfMonth(1);

        LocalDateTime start = firstDayThisMonth
                .minusMonths(1)
                .atStartOfDay();

        LocalDateTime end = firstDayThisMonth.atStartOfDay();

        return filter(transactions,
                transaction -> !transaction.getTimestamp().isBefore(start)
                        && transaction.getTimestamp().isBefore(end));
    }

    public List<Transaction> filterLast30Days(List<Transaction> transactions) {
        LocalDateTime start = LocalDateTime.now().minusDays(30);

        return filter(transactions,
                transaction -> !transaction.getTimestamp().isBefore(start));
    }

    public List<Transaction> filterByDateRange(
            List<Transaction> transactions,
            LocalDateTime start,
            LocalDateTime end) {

        return filter(transactions,
                transaction -> !transaction.getTimestamp().isBefore(start)
                        && !transaction.getTimestamp().isAfter(end));
    }
}