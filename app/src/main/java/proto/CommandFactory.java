package proto;


public class CommandFactory {
    public Command create(String commandName, String[] args) throws CommandException {

        AllowedCommands commandType = AllowedCommands.valueOf(commandName);

        switch (commandType) {
            case pub: 
                return new CommandPub(args);
            default:
                throw new CommandException("No command found");
        }
    }
    
}
