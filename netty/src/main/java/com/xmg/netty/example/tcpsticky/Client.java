package com.xmg.netty.example.tcpsticky;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.nio.charset.StandardCharsets;

public class Client {

    public static void main(String[] args) {
        final Client client = new Client();
        try {
            client.init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void init() throws InterruptedException {
        final NioEventLoopGroup group = new NioEventLoopGroup();
        //客户端启动引导
        Bootstrap bootstrap = new Bootstrap();
        final ChannelFuture channelFuture = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientHandler());
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    }
                })
                .connect("127.0.0.1", 8888)
                .sync();
        channelFuture.channel().closeFuture().sync();
    }


    static class ClientHandler extends ChannelInboundHandlerAdapter {
        //通道就绪
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            //此处服务器读取时候会发生tcp粘包拆包问题
            for (int i = 0; i < 10; i++) {
                ctx.writeAndFlush(Unpooled.copiedBuffer(("哈喽" + i).getBytes(StandardCharsets.UTF_8)));
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf buf = (ByteBuf) msg;
            final String s = buf.toString(StandardCharsets.UTF_8);
            System.out.println(s);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.close();
        }
    }

}
