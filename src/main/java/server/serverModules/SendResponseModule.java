package server.serverModules;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class SendResponseModule {

    public void handleWrite(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel(); // получаем канал для работы
        client.configureBlocking(false); // неблокирующий режим
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        client.write(buffer);
        client.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(4096));
    }

}
