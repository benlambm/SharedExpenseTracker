package splitter;

import splitter.commands.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new Application().run();
    }
}

class Application {
    private final Map<String, Command> commands;

    public Application() {
        this.commands = new HashMap<>();
        commands.put("balance", new BalanceCommand());
        commands.put("borrow", new BorrowCommand());
        commands.put("exit", new ExitCommand());
        commands.put("group", new EnhancedGroupCommand());
        commands.put("help", new HelpCommand(commands));
        commands.put("purchase", new EnhancedPurchaseCommand());
        commands.put("repay", new RepayCommand());
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");
            if (parts.length < 1) continue;

            // Extract date if present
            LocalDate date = LocalDate.now();
            int argIndex = 0;
            if (parts[0].contains(".")) {
                // Attempt to parse date
                try {
                    date = LocalDate.parse(parts[0], DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                    argIndex = 1;
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Expected yyyy.MM.dd");
                    continue;
                }

                if (argIndex >= parts.length) {
                    System.out.println("Command not provided. Print help to show commands list");
                    continue;
                }
            }

            // Execute command
            String cmdName = parts[argIndex];
            Command cmd = commands.get(cmdName);
            if (cmd == null) {
                System.out.println("Unknown command. Print help to show commands list");
            } else {
                String[] args = parts.length == 1 ? new String[0] : Arrays.copyOfRange(parts, argIndex + 1, parts.length);
                running = cmd.execute(date, args);
            }
        }
    }

}
