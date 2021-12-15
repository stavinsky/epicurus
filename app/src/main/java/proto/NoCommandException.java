package proto;

public class NoCommandException extends CommandException {

    public NoCommandException(String message) {
        super(message);
    }

    public NoCommandException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
