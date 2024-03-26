package theory_prep.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Server {

    public static void main(String[] args) throws IOException {
        byte[] arr = new byte[10];
        SocketAddress addr = new InetSocketAddress(6789);
        DatagramChannel dc = DatagramChannel.open();
        dc.bind(addr);

        ByteBuffer buffer = ByteBuffer.wrap(arr);
        addr = dc.receive(buffer);

        for (int j = 0; j < arr.length; ++j) arr[j] *= 2;

        buffer.flip();
        dc.send(buffer, addr);
    }
}
