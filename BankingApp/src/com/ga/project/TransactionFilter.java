package com.ga.project;

public interface TransactionFilter {
    boolean matches(Transaction transaction);
}
