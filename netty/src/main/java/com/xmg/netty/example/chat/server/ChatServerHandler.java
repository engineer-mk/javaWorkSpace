package com.xmg.netty.example.chat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
    private static final Set<Channel> onLineClients = new CopyOnWriteArraySet<>();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        onLineClients.add(ctx.channel());
        for (Channel ch : onLineClients) {
            if (ch != channel) {
                ch.writeAndFlush(ctx.channel().remoteAddress() + ":加入聊天室! 当前在线人数" + onLineClients.size());
            } else {
                channel.writeAndFlush("加入成功！当前在线人数" + onLineClients.size());
            }
        }

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        onLineClients.remove(channel);
        for (Channel ch : onLineClients) {
            ch.writeAndFlush(channel.remoteAddress() + ":离开聊天室! 当前在线人数" + onLineClients.size());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        final Channel channel = ctx.channel();
        String message = channel.remoteAddress().toString() + ":" + msg;
        for (Channel ch : onLineClients) {
            if (ch != channel) {
                ch.writeAndFlush(message);
            } else {
                channel.writeAndFlush("消息发送成功!");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
