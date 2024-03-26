package server.serverModules;

import client.Request;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class RequestReadModule {

    private final CommandExecutionModule executor;
    private final Logger logger;

    public RequestReadModule(CommandExecutionModule executor, Logger logger) {
        this.executor = executor;
        this.logger = logger;
    }

    public void handleRead(SelectionKey key) throws IOException, ClassNotFoundException {
        SocketChannel client = (SocketChannel) key.channel(); // получаем канал для работы
        client.configureBlocking(false); // неблокирующий режим

        ByteBuffer fromClientBuffer = (ByteBuffer) key.attachment();
        client.read(fromClientBuffer);
        try {

            ObjectInputStream fromClient = new ObjectInputStream(new ByteArrayInputStream(fromClientBuffer.array()));

            Request request = (Request) fromClient.readObject();
            fromClientBuffer.clear();
            System.out.println(request);
            byte[] response = executor.processCommand(request.getCommand(), request.getArgs(), request.getRoute()).getBytes(StandardCharsets.UTF_8);
            ByteBuffer responseBuffer = ByteBuffer.wrap(response);
            client.register(key.selector(), SelectionKey.OP_WRITE, responseBuffer);
        } catch (StreamCorruptedException e) {
            logger.info("Client disconnected");
            key.cancel();
        }
    }

}
