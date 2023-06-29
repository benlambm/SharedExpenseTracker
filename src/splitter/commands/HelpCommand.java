package splitter.commands;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeSet;

public class HelpCommand implements Command {
    private final Map<String, Command> commands;

    public HelpCommand(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public boolean execute(LocalDate date, String[] args) {
        TreeSet<String> sortedCommands = new TreeSet<>(commands.keySet());
        for (String cmdName : sortedCommands) {
            System.out.println(cmdName);
        }
        return true; // Continue running
    }
}
