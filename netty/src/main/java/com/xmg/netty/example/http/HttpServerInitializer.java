package com.xmg.netty.example.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    //新的SocketChannel通道加入时调用
    @Override
    protected void initChannel(SocketChannel ch) {
        final ChannelPipeline pipeline = ch.pipeline();
        //netty提供的处理http的 编解码器
        pipeline.addLast("httpServerHandler", new HttpServerCodec());
        pipeline.addLast("myHttpServerHandler", new HttpServerHandler());
//        pipeline.addLast(new SimpleServer.ServerEventHandler());
//        pipeline.addLast(new StringDecoder());
    }
}
