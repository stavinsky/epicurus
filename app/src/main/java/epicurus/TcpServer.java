package epicurus;
import java.net.*;

import com.google.inject.Inject;

import java.io.*;



public class TcpServer {

    private ServerSocket serverSocket;
    private final TcpHandleFactory handle_creator;

    @Inject
    TcpServer(TcpHandleFactory handle_creator){
        this.handle_creator = handle_creator;

    }
    
    public void start(int port) throws IOException{
        serverSocket = new ServerSocket(port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            handle_creator.create(clientSocket).start();

        }

    }
    public void stop() throws IOException{

        serverSocket.close();

    }


}
