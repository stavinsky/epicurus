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

    @Inject
    public TcpServerHandle(@Assisted Socket socket) {
        this.clientSocket = socket;

    }
    private void handle_commands() throws Exception{
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedInputStream(clientSocket.getInputStream());
        Command command = CommandParser.parse(in);
        System.out.println(command.getName());
        System.out.println(new String(command.getPayload(), "US-ASCII"));
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
