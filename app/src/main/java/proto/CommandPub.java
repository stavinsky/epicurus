package proto;

import java.lang.annotation.Retention;
import java.nio.charset.StandardCharsets;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import javax.inject.Qualifier;

import com.google.inject.Inject;

public class CommandPub extends Command {

    private int required_payload;
    public byte[] payload;
    private String topic;
    private final TstInject tst;

    @Inject
    public CommandPub(String[] args, TstInject tst) throws CommandException {
        super(args);
        this.tst = tst;
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
        System.out.println(getName());   
        System.out.println(topic);
        System.out.println(new String(payload, StandardCharsets.US_ASCII));
        System.out.println(tst.tst);

           
        
    }

}
