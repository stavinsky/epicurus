package epicurus;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import proto.Command;
import proto.CommandParser;
import proto.CommandResult;


public class TcpServerHandle extends Thread {
    private PrintWriter out;
    private BufferedInputStream in;
    private Socket clientSocket;
    private final CommandParser cmdParser;
    private final CommandExecutor cmdExecutor;

    @Inject
    public TcpServerHandle(
            @Assisted Socket socket, 
            CommandParser cmdParser,
            CommandExecutor cmdExecutor) {
        this.clientSocket = socket;
        this.cmdParser = cmdParser;
        this.cmdExecutor = cmdExecutor;

    }

    private void handleCommands() throws Exception {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedInputStream(clientSocket.getInputStream());
        Command command = cmdParser.parse(in);
        CommandResult result = cmdExecutor.execute(command);
        out.println(result);
    }

    @Override
    public void run() {
        try {
            handleCommands();
        } catch (Exception e) {
            System.out.println(
                String.format("error in user session %s", e.toString()));
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
