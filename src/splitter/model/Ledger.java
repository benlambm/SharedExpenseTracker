package splitter.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Ledger {
    private static Ledger instance;
    private final List<Transaction> ledger;

    private Ledger() {
        ledger = new LinkedList<>();
    }

    public static Ledger getInstance() {
        if (instance == null) {
            instance = new Ledger();
        }
        return instance;
    }

    public void addTransaction(Transaction transaction) {
        ledger.add(transaction);
    }


    /**
     * Use for getting Opening Balance (exclusive up to month value of date)
     *
     * @param from is Person making Loans or Repayments
     * @return from Person's opening balance
     */
    public int getOpeningBalance(LocalDate date, String from, String to) {
        int balance = 0;

        List<Transaction> filteredTransactions = ledger.stream()
                .filter(t -> t.date().isBefore(LocalDate.of(date.getYear(), date.getMonth(), 1)))
                .toList();

        for (Transaction t : filteredTransactions) {
            if (t.from().equals(from)) {
                balance += t.amount() * (t.type() == TransactionType.LOAN ? 1 : -1);
            }
            if (t.to().equals(from)) {
                balance -= t.amount() * (t.type() == TransactionType.LOAN ? 1 : -1);
            }
        }

        return balance;
    }

    /**
     * Use for getting Closing Balance (inclusive of date given)
     *
     * @param from is Person making Loans or Repayments
     * @return from Person's opening balance
     */
    public int getClosingBalance(LocalDate date, String from, String to) {
        int balance = 0;

        List<Transaction> filteredTransactions = ledger.stream()
                .filter(t -> (t.date().isEqual(date) || t.date().isBefore(date)))
                .toList();

        for (Transaction t : filteredTransactions) {
            if (t.from().equals(from) && t.to().equals(to)) {
                balance += t.amount() * (t.type() == TransactionType.LOAN ? 1 : -1);
            }
            if (t.to().equals(from) && t.from().equals(to)) {
                balance -= t.amount() * (t.type() == TransactionType.LOAN ? 1 : -1);
            }
        }
        return balance;
    }

    public Set<String> getAllPersons() {
        Set<String> persons = new HashSet<>();
        for (Transaction t : ledger) {
            persons.add(t.from());
            persons.add(t.to());
        }
        return persons;
    }

    public List<Transaction> getTransactions() {
        return new LinkedList<>(ledger);
    }
}

