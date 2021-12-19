package epicurus;
import java.io.*;
import java.net.*;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import proto.Command;
import proto.CommandParser;


public class TcpServerHandle extends Thread {
    private PrintWriter out;
    private BufferedInputStream in;
    private Socket clientSocket;
    private final CommandParser cmdParser;

    @Inject
    public TcpServerHandle(@Assisted Socket socket, CommandParser cmdParser) {
        this.clientSocket = socket;
        this.cmdParser = cmdParser;

    }
    private void handle_commands() throws Exception{
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedInputStream(clientSocket.getInputStream());
        Command command = cmdParser.parse(in);
        command.run();
    }
    @Override
    public void run(){
        try {
            handle_commands();
        }
        catch (Exception e ) {
            System.out.println(
                String.format("error in user session %s", e.toString()));
        }
        finally {
            try {
                in.close();
                out.close();
            } catch (IOException e ){

            }
        }
    }
}
