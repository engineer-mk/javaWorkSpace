package com.xmg.netty.example.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class SimpleClient {

    public ChannelFuture init() throws InterruptedException {
        final NioEventLoopGroup group = new NioEventLoopGroup();
        //客户端启动引导
        Bootstrap bootstrap = new Bootstrap();
        final ChannelFuture channelFuture = bootstrap.group(group).channel(NioSocketChannel.class)
                //客户端通道类型
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientHandler());
                    }
                }).connect("127.0.0.1", 7000);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("客户端就绪！");
                }
            }
        });
        channelFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println(future.channel().id() + "关闭!");
                }
            }
        });
        return channelFuture;
    }

    public static void main(String[] args) {
        final SimpleClient client = new SimpleClient();
        try {
            final ChannelFuture channelFuture = client.init();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                final String s = reader.readLine();
                channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(s.getBytes(StandardCharsets.UTF_8)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends ChannelInboundHandlerAdapter {
        //通道就绪
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.copiedBuffer("哈喽".getBytes(StandardCharsets.UTF_8)));
            ctx.channel().eventLoop().schedule(new Runnable() {
                @Override
                public void run() {
                    ctx.writeAndFlush(Unpooled.copiedBuffer("哈喽".getBytes(StandardCharsets.UTF_8)));
                }
            }, 10, TimeUnit.SECONDS);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            final String s = buf.toString(StandardCharsets.UTF_8);
            System.out.println(s);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }

}
