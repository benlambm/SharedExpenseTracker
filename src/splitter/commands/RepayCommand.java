package splitter.commands;

import splitter.model.Ledger;
import splitter.model.Transaction;
import splitter.model.TransactionType;

import java.time.LocalDate;

public class RepayCommand implements Command {

    @Override
    public boolean execute(LocalDate date, String[] args) {
        if (!argsAreValid(args)) return true;
        Transaction t = new Transaction(date, args[0], args[1], Integer.parseInt(args[2]), TransactionType.REPAYMENT);
        Ledger.getInstance().addTransaction(t);
        return true;
    }
}
