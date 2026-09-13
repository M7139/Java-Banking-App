package com.ga.project;

public class OverdraftPolicy {
    private static final double OVERDRAFT_FEE = 35.0;
    private static final double MAX_WITHDRAWAL_WHILE_NEGATIVE = 100.0;
    private static final int MAX_OVERDRAFTS_BEFORE_DEACTIVATION = 2;

    public String processWithdrawal(Account account, double amount) {

        if (!account.isActive()) {
            return "ACCOUNT_DEACTIVATED";
        }

        if (account.getBalance() < 0 && amount > MAX_WITHDRAWAL_WHILE_NEGATIVE) {
            return "EXCEEDS_NEGATIVE_BALANCE_LIMIT";
        }

        boolean willOverdraft = amount > account.getBalance();
        double newBalance = account.getBalance() - amount;

        if (willOverdraft) {
            newBalance -= OVERDRAFT_FEE;
            account.setOverdraftCount(account.getOverdraftCount() + 1);

            if (account.getOverdraftCount() >= MAX_OVERDRAFTS_BEFORE_DEACTIVATION) {
                account.setActive(false);
            }
        }

        account.setBalance(newBalance);
        return "SUCCESS";
    }

    public String reactivateIfEligible(Account account) {
        if (!account.isActive() && account.getBalance() >= 0) {
            account.setActive(true);
            account.setOverdraftCount(0);
            return "REACTIVATED";
        }
        return "NOT_ELIGIBLE";
    }
}