package server.serverModules;

import common.Request;
import common.Response;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestReadModule implements Runnable {

    private final Socket clientSocket;

    private final CommandExecutionModule executor;
    private final Logger logger;

    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(16);

    public RequestReadModule(Socket clientSocket, CommandExecutionModule executor, Logger logger) {
        this.clientSocket = clientSocket;
        this.executor = executor;
        this.logger = logger;
    }

    public void run() {
        Request request;
        Response response;
        try {

            ObjectInputStream fromClient = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream toClient = new ObjectOutputStream(clientSocket.getOutputStream());

            request = (Request) fromClient.readObject();
            System.out.println(request);
            response = executor.processCommand(request.getCommand(), request.getArgs(), request.getRoute());
            Response responseToSend = response;
            fixedThreadPool.submit(() -> {
                try {
                    toClient.writeObject(responseToSend);
                    toClient.flush();
                } catch (IOException e) {
                    logger.error("Error sending response to client", e);
                }
            });
        } catch (IOException e) {
            logger.info("Client disconnected");
        } catch (ClassNotFoundException e) {
            logger.error("Error reading request from client", e);
        }
    }

}
