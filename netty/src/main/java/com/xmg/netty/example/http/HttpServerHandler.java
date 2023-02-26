package com.xmg.netty.example.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            System.out.println("hashCode: " + this.hashCode());
            System.out.println("uti: " + ((HttpRequest) msg).uri());
            System.out.println("客户端地址: " + ctx.channel().remoteAddress());
            String result = "hello!";
            ByteBuf buf = Unpooled.buffer();
            buf.writeBytes(result.getBytes(StandardCharsets.UTF_8));
            HttpVersion version = HttpVersion.HTTP_1_1;
            HttpResponseStatus status = HttpResponseStatus.OK;
            HttpResponse response = new DefaultFullHttpResponse(version, status, buf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
            ctx.writeAndFlush(response);
        } else {
            System.out.println("不支持的类型: " + msg.getClass());
        }
    }
}
