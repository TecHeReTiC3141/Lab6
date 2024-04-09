package server.serverModules;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class AcceptConnectionModule {

    ServerSocket server;

    public AcceptConnectionModule(ServerSocket server) {
        this.server = server;
    }

    public Socket handleAccept() throws IOException {
        try {
            return server.accept();
        } catch (IOException e) {
            throw new IOException("Error accepting connection", e);
        }
    }
}
