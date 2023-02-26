package xmg.xmg.netty.example.chat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ChatServer {

    public static void main(String[] args) {
        final ChatServer chatServer = new ChatServer();
        chatServer.init();
    }

    public void init() {
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);
        final NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            final ChannelFuture cf = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new ChannelInitializer<ServerSocketChannel>() {
                        @Override
                        protected void initChannel(ServerSocketChannel ch) {
                            //                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        }
                    })
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new StringEncoder());
                            //ChannelOutboundHandler 在注册的时候需要放在最后一个ChannelInboundHandler之前，否则将无法传递到ChannelOutboundHandler
                            ch.pipeline().addLast(new ChatServerHandler());
                            //5s没有读事件,就发送一个心跳检测
                            //10s没有写事件，就发送一个心跳检测
                            //30s没有读写，就发送一个心跳检测
                            //IdleStateEvent触发后传递给下一个handler的userEventTriggered()处理
                            ch.pipeline().addLast(new IdleStateHandler(5, 10, 30, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new ChatIdleStateHandler());
                        }
                    })
                    .bind(8888).sync();
            cf.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    System.out.println("服务就绪!");
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
