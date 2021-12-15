package proto;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import javax.inject.Qualifier;

import com.google.inject.Inject;

public class CommandPub extends Command {

    @Qualifier
    @Retention(RUNTIME)
    @interface Tst {}





    private int required_payload;
    public byte[] payload;
    private String topic;

    @Inject
    public CommandPub(@Tst String tst, String[] args) throws CommandException {
        super(args);
        this.tst = tst;
    }

    public CommandPub(String[] args) throws CommandException {
        super(args);
    }

    @Override
    void parseArgs(String[] args) throws CommandException{
        if (args.length != 2) {
            throw new ParseCommandException("wrong number of arguments");
        }
        topic = args[0];
        try {
            required_payload = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e){
            throw new CommandException("cant parse payload size", e);

        }
    }

    @Override
    public boolean hasPayload() {
        return required_payload > 0;
    }

    @Override
    public int getPayloadSize() {
        return required_payload;
    }

    @Override
    public String getName() {
        return "pub";
    }

    @Override
    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    @Override
    public byte[] getPayload() {
        return payload;
    }

    @Override
    public void run() {
        System.out.println(tst);   
        
    }

}
