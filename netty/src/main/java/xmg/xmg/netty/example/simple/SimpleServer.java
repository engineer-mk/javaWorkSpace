package xmg.xmg.netty.example.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.internal.SystemPropertyUtil;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SimpleServer {
    private final static Executor ex = Executors.newCachedThreadPool();
    private final static EventExecutorGroup executor = new DefaultEventExecutorGroup(20);

    private static final Set<Channel> onLineClients = new CopyOnWriteArraySet<>();
    //未指定时默认的NioEventLoopGroup中child数量
    final int DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt(
            "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
    //cpu核心数量
    final int cpuCoreCount = NettyRuntime.availableProcessors();

    public void initServer() {

        //仅处理链接 默认线程数量cpu核心*2
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup(2);
        //处理业务
        final NioEventLoopGroup workGroup = new NioEventLoopGroup(3);
        //服务器启动引导 参数配置
        final ServerBootstrap bootstrap = new ServerBootstrap();
        final ChannelFuture channelFuture = bootstrap.group(bossGroup, workGroup)
                .handler(new ChannelInitializer<ServerSocketChannel>() {
                    @Override
                    protected void initChannel(ServerSocketChannel ch) {
                        ch.pipeline().addLast(new ServerAuthHandler());
                    }
                })
                .channel(NioServerSocketChannel.class) //指定服务器通道类型
                .option(ChannelOption.SO_BACKLOG, 100) //bossGroup参数配置
                .childOption(ChannelOption.SO_KEEPALIVE, true)  //workGroup参数配置
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    //SocketChannel注册时调用
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //workerGroup 的 EventLoop 对应的管道设置处理器
                        ch.pipeline().addLast(new ServerEventHandler());
                    }
                })
                .bind(7000);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("服务器就绪！");
                }
            }
        });
    }

    public static void main(String[] args) {
        final SimpleServer simpleServer = new SimpleServer();
        simpleServer.initServer();
    }

    public static class ServerAuthHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg == null) {
                System.out.println("没有认证信息，断开连接");
                ctx.close();
            }
            super.channelRead(ctx,msg);
        }
    }

    public static class ServerEventHandler extends ChannelInboundHandlerAdapter {
        //通道注册
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            final Channel channel = ctx.channel();
            onLineClients.add(channel);
            System.out.println(ctx.channel().remoteAddress() + "注册成功！" + Thread.currentThread().getName());
            System.out.println("当前在线客户端数量:" + onLineClients.size());
        }

        //通道注销
        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println(ctx.channel().remoteAddress() + "注销成功！");
            onLineClients.remove(ctx.channel());
            System.out.println("当前在线客户端数量:" + onLineClients.size());
        }

        //通道活跃
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
        }

        //可读事件
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ex.execute(() -> {
                System.out.println(ctx.channel().remoteAddress() + "队列中的任务！" + Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer(("🤔" + "?").getBytes(StandardCharsets.UTF_8)));
            });
            System.out.println(ctx.channel().remoteAddress() + "可读！" + Thread.currentThread().getName());
            ByteBuf buf = (ByteBuf) msg;
            final String s = buf.toString(StandardCharsets.UTF_8);
            ctx.writeAndFlush(Unpooled.copiedBuffer((s + "?").getBytes(StandardCharsets.UTF_8)));
        }

        //读完成
        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            System.out.println(ctx.channel().remoteAddress() + "读完成！");
        }

        //异常
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println(ctx.channel().remoteAddress() + "发生异常！");
            ctx.close();
        }
    }

}
