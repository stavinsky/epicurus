package epicurus;

import proto.Command;
import proto.CommandPub;
import proto.CommandResult;

public class CommandExecutor {
    public CommandResult execute(Command command) {
        switch (command.getName()) {
            case pub: {
                return execute((CommandPub) command);
            }
            default:
                return CommandResult.error("unknown command");
        }
    }

    private CommandResult execute(CommandPub command) {
        return CommandResult.error("not implemented");
    }
}
