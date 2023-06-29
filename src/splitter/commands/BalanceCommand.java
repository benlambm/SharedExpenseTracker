package splitter.commands;

import splitter.model.Ledger;
import splitter.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

public class BalanceCommand implements Command {
    boolean open = false;

    @Override
    public boolean argsAreValid(String[] args) {
        return args.length == 0 || args.length == 1 && ("open".equals(args[0]) || "close".equals(args[0]));
    }

    @Override
    public boolean execute(LocalDate date, String[] args) {
        if (!argsAreValid(args)) {
            System.out.println("Illegal command arguments");
            return true;
        }
        open = args.length > 0 && ("open".equals(args[0]));

        // Use TreeSet to keep the lines sorted and unique
        TreeSet<String> outputLines = new TreeSet<>();

        List<Transaction> transactions = Ledger.getInstance().getTransactions();
        for (Transaction t : transactions) {
            String agent = t.from();
            String friend = t.to();

            int agentBalance;
            int friendBalance;

            if (open) {
                agentBalance = Ledger.getInstance().getOpeningBalance(date, agent, friend);
                friendBalance = Ledger.getInstance().getOpeningBalance(date, friend, agent);
            } else {
                agentBalance = Ledger.getInstance().getClosingBalance(date, agent, friend);
                friendBalance = Ledger.getInstance().getClosingBalance(date, friend, agent);
            }

            if (agentBalance > friendBalance) {
                outputLines.add(String.format("%s owes %s %d", agent, friend, agentBalance));
            } else if (friendBalance > agentBalance) {
                outputLines.add(String.format("%s owes %s %d", friend, agent, friendBalance));
            } else {
                outputLines.add("No repayments");
            }
        }

        // Print the lines
        for (String line : outputLines) {
            System.out.println(line);
        }
        return true;
    }


}
