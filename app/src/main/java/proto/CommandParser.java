package proto;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class CommandParser {
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
        split = output.split("\s");
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

    public static Command parse(BufferedInputStream input) throws CommandException {
        String[] header = parse_header(input);
        String command_string = header[0];
        String[] args = Arrays.copyOfRange(header, 1, header.length);

        if (command_string.equals("pub") ) {
            Command command = new CommandPub(args);
            if (command.hasPayload()) {
                command.setPayload(
                    parse_payload(input, command.getPayloadSize()));
            }
            return command;
 
        }
        throw new CommandException("command not found");

    }
}
