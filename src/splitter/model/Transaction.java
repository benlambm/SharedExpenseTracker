package splitter.model;

import java.time.LocalDate;

public record Transaction(int id, LocalDate date, String from, String to, int amount, TransactionType type) {
    private static int nextId = 1;

    public Transaction(LocalDate date, String from, String to, int amount, TransactionType type) {
        this(nextId++, date, from, to, amount, type);
    }
}