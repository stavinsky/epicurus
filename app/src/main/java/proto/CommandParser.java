package proto;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.google.inject.Inject;

public class CommandParser {
    private final CommandFactory cmdFactory;

    @Inject
    public CommandParser(CommandFactory cmdFactory){
        this.cmdFactory = cmdFactory;
    }
    public static String readLine(BufferedInputStream input) throws IOException{
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int ch; 
        while ((ch = input.read()) != '\n') {
            if (ch == -1 ) {
                throw new IOException("enf of buffer");   
            }
            buf.write(ch);
        }
        return new String(buf.toByteArray(), "US-ASCII");
    }

    public static String[] parse_header(BufferedInputStream input) throws CommandException{
        String output;
        try {
            output = readLine(input);
        }
        catch (IOException e) {
            throw new ParseCommandException("cant read header line", e);
        }
        String[] split;
        split = output.split("\\s");
        return split;
    }

    public static byte[] parse_payload(BufferedInputStream input, int size) throws CommandException {
        byte[] buff =  new byte[size];
        int res;

        try{
            res = input.read(buff);
        }
        catch (IOException e){
            throw new ParseCommandException("can't read payload", e);
        }
        if (res != size){
            throw new ParseCommandException("Message payload has wrong wize");
        } 
        return buff;

    }

    public Command parse(BufferedInputStream input) throws CommandException {
        String[] header = parse_header(input);
        String command_name = header[0];
        String[] args = Arrays.copyOfRange(header, 1, header.length);

        Command command = cmdFactory.create(command_name, args);
        if (command.hasPayload()) {
            command.setPayload(
                parse_payload(input, command.getPayloadSize()));
        }
        return command;

    }
}
