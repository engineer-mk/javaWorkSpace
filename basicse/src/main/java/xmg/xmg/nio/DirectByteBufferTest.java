package xmg.xmg.nio;

import java.nio.ByteBuffer;

/**
 *  直接操作堆外内存
 * @author makui
 * @created on  2022/10/9
 **/
public class DirectByteBufferTest {
    public static void main(String[] args) {
        //直接从操作系统中申请1M 内存
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 1024);
    }
}
