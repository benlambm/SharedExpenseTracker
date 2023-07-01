package splitter.commands;

import splitter.model.Ledger;
import splitter.model.Transaction;
import splitter.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BorrowCommand implements Command {
    private final Ledger ledger;

    public BorrowCommand() {
        this.ledger = Ledger.getInstance();
    }

    @Override
    public boolean execute(LocalDate date, String[] args) {
        if (!argsAreValid(args)) return true;
        Transaction t = new Transaction(date, args[0], args[1], new BigDecimal(args[2]), TransactionType.LOAN);
        ledger.addTransaction(t);
        return true;
    }
}

