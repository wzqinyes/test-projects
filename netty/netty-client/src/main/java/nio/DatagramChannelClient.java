package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Date;

public class DatagramChannelClient {

    public static void main(String[] args) throws IOException {
        SelectorProvider provider = SelectorProvider.provider();
        DatagramChannel dc = provider.openDatagramChannel();
        dc.configureBlocking(false);
        ByteBuffer buf = ByteBuffer.allocate(1024);
        String str = " UDP data .......";
        buf.put((new Date().toString() + ":\n" + str).getBytes());
        buf.flip();
        dc.send(buf, new InetSocketAddress("127.0.0.1", 9998));
        buf.clear();

        dc.close();
    }
}
