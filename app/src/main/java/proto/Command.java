package proto;


public interface Command {

    void parseArgs(String[] args) throws CommandException;

    public boolean hasPayload();
    public int getPayloadSize();

    public AllowedCommands getName();
    public void setPayload(byte[] payload);
    public byte[] getPayload();
}
