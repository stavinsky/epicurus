package proto;

public class CommandFactory {
    public Command create(String command_name, String[] args) throws CommandException{

        AllowedCommands commandType = AllowedCommands.valueOf(command_name);

        switch (commandType){
            case pub: 
                return new CommandPub(args);
            default:
                throw new CommandException("No command found");
        }
    }
    
}
