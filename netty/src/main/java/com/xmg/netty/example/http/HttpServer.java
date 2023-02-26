package com.xmg.netty.example.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HttpServer {
    public static void main(String[] args) {
        final HttpServer httpServer = new HttpServer();
        try {
            httpServer.init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void init() throws InterruptedException {
        //链接处理
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);
        //处理业务
        final NioEventLoopGroup workGroup = new NioEventLoopGroup(3);
        //服务器启动引导 参数配置
        final ServerBootstrap bootstrap = new ServerBootstrap();
        final ChannelFuture cf = bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new HttpServerInitializer())
                .bind(8888);
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("服务器就绪！");
                }
            }
        });
    }
}
