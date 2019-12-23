package wu;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler2 extends ChannelInboundHandlerAdapter {

    //该方法将会在连接被建立并且准备进行通信时被调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        UnixTime t = (UnixTime) msg;
        System.out.println(t);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
