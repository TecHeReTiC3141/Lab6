package theory_prep.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Client {
    public static void main(String[] args) throws IOException {
        byte[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        DatagramChannel dc = DatagramChannel.open();

        SocketAddress addr = new InetSocketAddress("localhost", 6789);
        dc.connect(addr);
        ByteBuffer buffer = ByteBuffer.wrap(arr);
        dc.send(buffer, addr);

        buffer.clear();
        addr = dc.receive(buffer);

        for (byte j : arr) {
            System.out.println(j);
        }
    }
}
