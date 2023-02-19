package com.xmg.netty.example.tcpsticky;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.nio.charset.StandardCharsets;

public class Server {

    public static void main(String[] args) {
        final Server simpleServer = new Server();
        try {
            simpleServer.initServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initServer() throws InterruptedException {

        //仅处理链接 默认线程数量cpu核心*2
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //处理业务
        final NioEventLoopGroup workGroup = new NioEventLoopGroup(3);
        //服务器启动引导 参数配置
        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ServerEventHandler());
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    }
                })
                .bind(8888)
                .sync();
    }


    public static class ServerEventHandler extends ChannelInboundHandlerAdapter {

        //可读事件
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf buf = (ByteBuf) msg;
            final String s = buf.toString(StandardCharsets.UTF_8);
            System.out.println(s);
        }

        //异常
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println(ctx.channel().remoteAddress() + "发生异常！");
            ctx.close();
        }
    }

}
