package epicurus;

import java.net.Socket;

public interface TcpHandleFactory {
    TcpServerHandle create(Socket socket);
}
