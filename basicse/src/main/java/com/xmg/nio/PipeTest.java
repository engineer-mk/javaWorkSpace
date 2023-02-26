package com.xmg.nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Pipe 管道是 2 个线程之间的单向数据连接。Pipe 有一个 source 通道和一个 sink 通道。数据会被写到 sink 通道，从 source 通道读取
 *
 * @author makui
 * @created on  2022/10/14
 **/
public class PipeTest {
    static Pipe pipe;

    static {
        try {
            pipe = Pipe.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            final Pipe.SinkChannel sink = pipe.sink();
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            byteBuffer.put("aa".getBytes(StandardCharsets.UTF_8));
            try {
                sink.write(byteBuffer);
                sink.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Thread.sleep(10);

        new Thread(() -> {
            final Pipe.SourceChannel source = pipe.source();
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            int len= 0;
            try {
                len = source.read(byteBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(new String(byteBuffer.array(),0,len));
        }).start();
    }


}
