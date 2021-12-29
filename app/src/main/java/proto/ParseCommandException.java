package proto;

public class ParseCommandException  extends CommandException {

    public ParseCommandException(String message) {
        super(message);
    }

    public ParseCommandException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
