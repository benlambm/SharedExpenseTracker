package splitter.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

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

    public BigDecimal getOpeningBalance(LocalDate date, String from, String to) {
        BigDecimal balance = BigDecimal.ZERO;
        List<Transaction> filteredTransactions = ledger.stream()
                .filter(t -> t.date().isBefore(LocalDate.of(date.getYear(), date.getMonth(), 1)))
                .toList();
        for (Transaction t : filteredTransactions) {
            if (t.from().equals(from) && t.to().equals(to)) {
                balance = balance.add(t.amount().multiply(t.type() == TransactionType.LOAN ? BigDecimal.ONE : BigDecimal.ONE.negate()));
            }
            if (t.to().equals(from) && t.from().equals(to)) {
                balance = balance.subtract(t.amount().multiply(t.type() == TransactionType.LOAN ? BigDecimal.ONE : BigDecimal.ONE.negate()));
            }
        }
        return balance;
    }

    public BigDecimal getClosingBalance(LocalDate date, String from, String to) {
        BigDecimal balance = BigDecimal.ZERO;
        List<Transaction> filteredTransactions = ledger.stream()
                .filter(t -> t.date().isEqual(date) || t.date().isBefore(date))
                .toList();
        for (Transaction t : filteredTransactions) {
            if (t.from().equals(from) && t.to().equals(to)) {
                balance = balance.add(t.amount().multiply(t.type() == TransactionType.LOAN ? BigDecimal.ONE : BigDecimal.ONE.negate()));
            }
            if (t.to().equals(from) && t.from().equals(to)) {
                balance = balance.subtract(t.amount().multiply(t.type() == TransactionType.LOAN ? BigDecimal.ONE : BigDecimal.ONE.negate()));
            }
        }
        return balance;
    }


    public List<Transaction> getTransactions() {
        return new LinkedList<>(ledger);
    }

    public void addTransactionGroup(LocalDate date, String payer, List<String> includedPeople, List<String> excludedPeople, BigDecimal amount) {
        // Remove excluded people from the included people list
        includedPeople.removeAll(excludedPeople);

        BigDecimal splitAmount = amount.divide(new BigDecimal(includedPeople.size()), 2, RoundingMode.DOWN);
        BigDecimal remainder = amount.subtract(splitAmount.multiply(new BigDecimal(includedPeople.size())));

        // Create a copy of the list and sort it in alphabetical order
        List<String> sortedPeople = new ArrayList<>(includedPeople);
        sortedPeople.remove(payer); // remove payer from the list before sorting
        Collections.sort(sortedPeople);

        Map<String, BigDecimal> debts = new LinkedHashMap<>();
        for (String person : includedPeople) {
            if (!person.equals(payer)) {
                debts.put(person, splitAmount);
            }
        }

        int remainderCents = remainder.multiply(new BigDecimal("100")).intValue();
        for (int i = 0; i < remainderCents; i++) {
            String person = sortedPeople.get(i % sortedPeople.size());
            debts.put(person, debts.get(person).add(new BigDecimal("0.01")));
        }

        for (Map.Entry<String, BigDecimal> entry : debts.entrySet()) {
            addTransaction(new Transaction(date, entry.getKey(), payer, entry.getValue(), TransactionType.LOAN));
        }
    }


}
