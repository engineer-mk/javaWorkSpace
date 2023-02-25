package xmg.xmg.netty.example.chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient {
    final NioEventLoopGroup group = new NioEventLoopGroup();
    public ChannelFuture init() {
        //客户端启动引导
        Bootstrap bootstrap = new Bootstrap();
        final ChannelFuture channelFuture = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                //客户端通道类型
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ChatClientHandler());
                    }
                }).connect("127.0.0.1", 8888);
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                System.out.println("客户端就绪！");
            }
        });
        channelFuture.channel()
                .closeFuture()
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        System.out.println(future.channel().id() + "关闭!");
                    }
                });
        return channelFuture;
    }

    public void shutdown(){
        group.shutdownGracefully();
    }

    public static void main(String[] args) {
        final ChatClient client = new ChatClient();
        try {
            final ChannelFuture channelFuture = client.init();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("请输入:");
                final String s = reader.readLine();
                if ("exit".equalsIgnoreCase(s)) {
                    reader.close();
                    client.shutdown();
                    break;
                }
                channelFuture.channel().writeAndFlush(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
