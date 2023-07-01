package splitter.commands;

import splitter.model.GroupIndex;
import splitter.model.Ledger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class EnhancedPurchaseCommand implements Command {
    private final Ledger ledger;
    private final GroupIndex groupIndex;

    public EnhancedPurchaseCommand() {
        this.ledger = Ledger.getInstance();
        this.groupIndex = GroupIndex.getInstance();
    }

    @Override
    public boolean execute(LocalDate date, String[] args) {
        if (!argsAreValid(args)) {
            return true;
        }
        String payer = args[0];
        BigDecimal amount = new BigDecimal(args[2]);
        Set<String> includedPeople = new LinkedHashSet<>();
        Set<String> excludedPeople = new LinkedHashSet<>();

        // example args: (TEAM, Elon, -GIRLS)
        for (int i = 3; i < args.length; i++) {
            String arg = args[i].replaceAll("[(),]", "");
            List<String> groupMembers;
            if (arg.startsWith("-")) {
                arg = arg.substring(1);
                groupMembers = groupIndex.getGroup(arg);
                if (groupMembers != null) {
                    excludedPeople.addAll(groupMembers);
                } else {
                    excludedPeople.add(arg);
                }
            } else {
                if (arg.startsWith("+")) {
                    arg = arg.substring(1);
                }
                groupMembers = groupIndex.getGroup(arg);
                if (groupMembers != null) { //  if arg is a GROUP
                    if (groupMembers.isEmpty()) {
                        System.out.println("Group is empty");
                        return true;
                    }
                    includedPeople.addAll(groupMembers);
                } else {
                    includedPeople.add(arg);
                }
            }
        }

        includedPeople.removeAll(excludedPeople); // Remove excluded people from the includedPeople set

        ledger.addTransactionGroup(date, payer, new ArrayList<>(includedPeople), new ArrayList<>(excludedPeople), amount);
        return true;
    }

    @Override
    public boolean argsAreValid(String[] args) {
        if (args.length < 3) {
            System.out.println("Illegal command arguments");
            return false;
        }
        BigDecimal amount;
        try {
            amount = new BigDecimal(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Illegal command arguments");
            return false;
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Amount must be positive");
            return false;
        }
        return true;
    }
}