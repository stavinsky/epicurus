package proto;


public class CommandPub implements Command {

    private int requiredPayload;
    public byte[] payload;
    public String topic;

    public CommandPub(String[] args) throws CommandException {
        parseArgs(args);
    }

    public void parseArgs(String[] args) throws CommandException {
        if (args.length != 2) {
            throw new ParseCommandException("wrong number of arguments");
        }
        topic = args[0];
        try {
            requiredPayload = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new CommandException("cant parse payload size", e);

        }
    }

    @Override
    public boolean hasPayload() {
        return requiredPayload > 0;
    }

    @Override
    public int getPayloadSize() {
        return requiredPayload;
    }

    @Override
    public AllowedCommands getName() {
        return AllowedCommands.pub;
    }

    @Override
    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    @Override
    public byte[] getPayload() {
        return payload;
    }
}
