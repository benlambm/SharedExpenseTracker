package splitter.commands;

import java.time.LocalDate;

public interface Command {
    boolean execute(LocalDate date, String[] args);

    default boolean argsAreValid(String[] args) {
        if (args.length != 3) {
            System.out.println("Illegal command arguments");
            return false;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Illegal command arguments");
            return false;
        }
        return true;
    }
}


