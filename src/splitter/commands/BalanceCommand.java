package splitter.commands;

import splitter.model.Ledger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BalanceCommand implements Command {
    private final Ledger ledger;
    private boolean open;

    public BalanceCommand() {
        this.ledger = Ledger.getInstance();
    }

    @Override
    public boolean execute(LocalDate date, String[] args) {
        open = args.length > 0 && "open".equals(args[0]);

        Set<String> people = ledger.getTransactions().stream()
                .flatMap(t -> Stream.of(t.from(), t.to()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<String> outputLines = new LinkedHashSet<>();
        for (String personFrom : people) {
            for (String personTo : people) {
                if (!personFrom.equals(personTo)) {
                    BigDecimal balance;
                    if (open) {
                        balance = ledger.getOpeningBalance(date, personFrom, personTo);
                    } else {
                        balance = ledger.getClosingBalance(date, personFrom, personTo);
                    }
                    if (balance.compareTo(BigDecimal.ZERO) > 0) {
                        outputLines.add(String.format("%s owes %s %.2f", personFrom, personTo, balance.doubleValue()));
                    }
                }
            }
        }

        // Output the balances
        if (outputLines.isEmpty()) {
            System.out.println("No repayments");
        } else {
            outputLines.stream().sorted()
                    .forEach(System.out::println);
        }
        return true;
    }
}