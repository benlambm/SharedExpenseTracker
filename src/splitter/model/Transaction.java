package splitter.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(int id, LocalDate date, String from, String to, BigDecimal amount, TransactionType type) {
    private static int nextId = 1;

    public Transaction(LocalDate date, String from, String to, BigDecimal amount, TransactionType type) {
        this(nextId++, date, from, to, amount, type);
    }
}
