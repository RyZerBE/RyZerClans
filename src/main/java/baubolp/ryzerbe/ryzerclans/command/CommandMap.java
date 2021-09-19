package baubolp.ryzerbe.ryzerclans.command;

import java.util.ArrayList;
import java.util.List;

public class CommandMap {

    private List<Command> commands;

    public CommandMap() {
        this.commands = new ArrayList<>();
    }

    public void registerCommand(Command command) {
        this.commands.add(command);
    }

    public void registerCommands(Command ...commands) {
        for(Command command : commands) {
            registerCommand(command);
        }
    }

    public boolean isRegistered(Command command) { return this.commands.contains(command); }

    public void unregisterCommand(Command command) { this.commands.remove(command); }

    public List<Command> getCommands() { return commands; }
}
