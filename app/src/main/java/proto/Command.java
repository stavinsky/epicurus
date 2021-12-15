package proto;


public abstract class Command {
    protected String tst;

    abstract void parseArgs(String[] args) throws CommandException;


    public Command(String[] args) throws CommandException{
        parseArgs(args);
    }

    abstract public boolean hasPayload();
    abstract public int getPayloadSize();

    abstract public String getName();
    abstract public void setPayload(byte[] payload);
    abstract public byte[] getPayload();
    abstract public void run();
}
