
package epicurus;

import com.google.inject.Inject;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class TcpServer {

    private ServerSocket serverSocket;
    private final TcpHandleFactory handleFactory;

    @Inject
    TcpServer(TcpHandleFactory handleFactory) {
        this.handleFactory = handleFactory;

    }
    
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            handleFactory.create(clientSocket).start();

        }

    }

    public void stop() throws IOException {
        serverSocket.close();
    }


}
