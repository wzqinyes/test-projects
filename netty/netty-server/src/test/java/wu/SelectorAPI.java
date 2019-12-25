package wu;

import io.netty.channel.ChannelException;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;

public class SelectorAPI {

    public static void main(String[] args){

        SelectorProvider provider = SelectorProvider.provider();

        try {
            Selector selector = provider.openSelector();
            ServerSocketChannel serverSocketChannel = provider.openServerSocketChannel();

//            SocketChannel socketChannel = provider.openSocketChannel();

        } catch (IOException e) {
            throw new ChannelException("Failed to open a socket.", e);
        }


    }
}
