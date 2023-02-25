package xmg.xmg.nio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class FileChannelTest {

    @DisplayName("FileChannel读数据")
    @Test
    public void test1() {
        File file = new File("test1.txt");
        try (FileChannel channel = new FileInputStream(file).getChannel();
             OutputStream outputStream = new ByteArrayOutputStream();) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            int len;
            //从文件18个字节位置开始读
            channel.position(18);
            while ((len = channel.read(byteBuffer)) != -1) {
                byte[] bytes = new byte[10];
                byteBuffer.flip();
                byteBuffer.get(bytes, 0, len);
                byteBuffer.clear();
                outputStream.write(bytes, 0, len);
            }
            System.out.println(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("FileChannel写数据")
    @Test
    public void test2() {
        File file = new File("test1.txt");
        try (FileOutputStream outputStream = new FileOutputStream(file);
             FileChannel fileChannel = outputStream.getChannel()
        ) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            byte[] data = "啊哈哈！".getBytes(StandardCharsets.UTF_8);
            byteBuffer.put(data);
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                //从文件15个字节位置开始写
                fileChannel.position(15);
                fileChannel.write(byteBuffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
