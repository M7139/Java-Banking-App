package com.ga.project;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionFilterServiceTest {

    TransactionFilterService filterService;
    List<Transaction> transactions;

    @BeforeEach
    public void setUp() {
        filterService = new TransactionFilterService();
        transactions = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        transactions.add(new Transaction(
                "T1",
                "CHECKING",
                "DEPOSIT",
                100,
                100,
                now
        )); // today

        transactions.add(new Transaction(
                "T2",
                "CHECKING",
                "WITHDRAW",
                50,
                50,
                now.minusDays(1)
        )); // yesterday

        transactions.add(new Transaction(
                "T3",
                "SAVINGS",
                "DEPOSIT",
                200,
                200,
                now.minusDays(5)
        )); // last 7 days

        transactions.add(new Transaction(
                "T4",
                "SAVINGS",
                "DEPOSIT",
                300,
                300,
                now.minusDays(20)
        )); // last 30 days

        transactions.add(new Transaction(
                "T5",
                "CHECKING",
                "DEPOSIT",
                400,
                400,
                now.minusDays(60)
        )); // older than 30 days
    }

    @Test
    @DisplayName("When filtering today then only today's transaction is returned")
    public final void whenFilteringTodayThenOnlyTodaysTransactionReturned() {
        List<Transaction> result =
                filterService.filterToday(transactions);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(
                "T1",
                result.get(0).getTransactionId()
        );
    }

    @Test
    @DisplayName("When filtering yesterday then only yesterday's transaction is returned")
    public final void whenFilteringYesterdayThenOnlyYesterdaysTransactionReturned() {
        List<Transaction> result =
                filterService.filterYesterday(transactions);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(
                "T2",
                result.get(0).getTransactionId()
        );
    }

    @Test
    @DisplayName("When filtering last 7 days then today, yesterday, and 5-days-ago transactions are returned")
    public final void whenFilteringLast7DaysThenThreeTransactionsReturned() {
        List<Transaction> result =
                filterService.filterLast7Days(transactions);

        Assert.assertEquals(3, result.size());
    }

    @Test
    @DisplayName("When filtering last week then only transactions from the previous calendar week are returned")
    public final void whenFilteringLastWeekThenOnlyPreviousWeekTransactionsReturned() {

        LocalDate today = LocalDate.now();

        LocalDate startOfThisWeek =
                today.with(DayOfWeek.MONDAY);

        LocalDateTime lastWeek =
                startOfThisWeek
                        .minusDays(3)
                        .atTime(12, 0);

        LocalDateTime thisWeek =
                startOfThisWeek
                        .atTime(12, 0);

        List<Transaction> testTransactions =
                new ArrayList<>();

        testTransactions.add(new Transaction(
                "LW1",
                "CHECKING",
                "DEPOSIT",
                100,
                100,
                lastWeek
        ));

        testTransactions.add(new Transaction(
                "CW1",
                "CHECKING",
                "DEPOSIT",
                100,
                200,
                thisWeek
        ));

        List<Transaction> result =
                filterService.filterLastWeek(testTransactions);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(
                "LW1",
                result.get(0).getTransactionId()
        );
    }

    @Test
    @DisplayName("When filtering last 30 days then everything except the 60-day-old transaction is returned")
    public final void whenFilteringLast30DaysThenFourTransactionsReturned() {
        List<Transaction> result =
                filterService.filterLast30Days(transactions);

        Assert.assertEquals(4, result.size());
    }

    @Test
    @DisplayName("When filtering last month then only transactions from the previous calendar month are returned")
    public final void whenFilteringLastMonthThenOnlyPreviousMonthTransactionsReturned() {

        LocalDate firstDayThisMonth =
                LocalDate.now().withDayOfMonth(1);

        LocalDateTime lastMonth =
                firstDayThisMonth
                        .minusMonths(1)
                        .plusDays(5)
                        .atTime(12, 0);

        LocalDateTime thisMonth =
                firstDayThisMonth
                        .plusDays(1)
                        .atTime(12, 0);

        List<Transaction> testTransactions =
                new ArrayList<>();

        testTransactions.add(new Transaction(
                "LM1",
                "SAVINGS",
                "DEPOSIT",
                200,
                200,
                lastMonth
        ));

        testTransactions.add(new Transaction(
                "CM1",
                "SAVINGS",
                "DEPOSIT",
                200,
                400,
                thisMonth
        ));

        List<Transaction> result =
                filterService.filterLastMonth(testTransactions);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(
                "LM1",
                result.get(0).getTransactionId()
        );
    }

    @Test
    @DisplayName("When filtering by custom date range then only transactions within range are returned")
    public final void whenFilteringByCustomRangeThenOnlyMatchingTransactionsReturned() {
        LocalDateTime start =
                LocalDateTime.now().minusDays(10);

        LocalDateTime end =
                LocalDateTime.now().minusDays(3);

        List<Transaction> result =
                filterService.filterByDateRange(
                        transactions,
                        start,
                        end
                );

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(
                "T3",
                result.get(0).getTransactionId()
        );
    }

    @Test
    @DisplayName("When using a custom lambda filter then only deposits are returned")
    public final void whenUsingCustomLambdaFilterThenOnlyDepositsReturned() {
        List<Transaction> result =
                filterService.filter(
                        transactions,
                        t -> t.getType().equals("DEPOSIT")
                );

        Assert.assertEquals(4, result.size());
    }
}