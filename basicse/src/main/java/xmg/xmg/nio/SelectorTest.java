package xmg.xmg.nio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class SelectorTest {
    //selector在进行select() 操作时会持续等待，并且会占用selector内部的队列锁。
    //当我们没有调用selector.wakeup()，直接进行register时，会出现我们register的线程持续等待的情况，因为register本质来说就是将我们的channel+key注册到selector的观察队列中。
    //但是目前队列已经被selector自身的锁给锁住了（操作内部的队列需要先得到锁才能进行，是互斥的），所以只要selector.select() 没有就绪事件产生，也就会一直占用，从而我们的新的Key也注册不进去。
    //selector.wakeup()的意义就在于强制唤醒selector.select()，不管当前有没有就绪事件都唤醒，并走后续的业务逻辑。既然会走后续的业务逻辑，所以selector.select()方法就结束了，并且释放了内部锁的占用。
    //从而让我们有机会将我们的channel+key注册到selector的观察队列中。

    @DisplayName("服务端")
    @Test
    public void test1() throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.bind(new InetSocketAddress(8888));
        Selector selector = Selector.open();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            //selector.select()此处进入阻塞状态
            if (selector.select() > 0) {
                final Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (Iterator<SelectionKey> iterator = selectionKeys.iterator(); iterator.hasNext(); ) {
                    SelectionKey next = iterator.next();
                    if (next.isAcceptable()) {
                        SocketChannel socket = serverSocket.accept();
                        socket.configureBlocking(false);
                        socket.register(selector, SelectionKey.OP_READ);
                    } else if (next.isReadable()) {
                        final SocketChannel channel = (SocketChannel) next.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        int len;
                        while ((len = channel.read(byteBuffer)) > 0) {
                            byteBuffer.flip();
                            outputStream.write(byteBuffer.array(), 0, len);
                            byteBuffer.clear();
                        }
                        final String address = channel.getLocalAddress().toString();
                        System.out.println("来自->" + address + "消息:" + outputStream);
                    }
                    iterator.remove();
                }
            }

        }
    }


    /**
     * 客户端
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        SocketChannel socketChannel = null;
        BufferedReader bufferedReader = null;
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
            socketChannel.configureBlocking(false);
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                System.out.println("请输入:");
                final String s = bufferedReader.readLine();
                buffer.put(s.getBytes(StandardCharsets.UTF_8));
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Test
    public void test3() {
        //16->0001 0000
        //34->0010 0010
        // 00011111
//        System.out.println(16&34);
//        System.out.println(31&(~16));
        System.out.println(1<<3|(1 << 4));
        System.out.println(1 << 2);
    }
}
