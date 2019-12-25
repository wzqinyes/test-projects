package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

public class NioClient {

    public static void main(String[] args) throws IOException {

        SelectorProvider provider = SelectorProvider.provider();
        SocketChannel socketChannel = provider.openSocketChannel();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));

        while (!socketChannel.finishConnect()) {
            System.out.println("......");
        }

        String newData = "New String to write to file..." + System.currentTimeMillis();
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());
        buf.flip();
        while (buf.hasRemaining()) {
            socketChannel.write(buf);
        }

        socketChannel.close();

    }
}
