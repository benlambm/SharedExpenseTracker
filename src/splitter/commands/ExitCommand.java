package splitter.commands;

import java.time.LocalDate;

public class ExitCommand implements Command {
    @Override
    public boolean execute(LocalDate date, String[] args) {
        return false; // Stop application
    }
}
