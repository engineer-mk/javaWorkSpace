package xmg.xmg.io.internet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

@DisplayName("TCP通讯")
public class TcpTest {
    private static final Integer point = 8888;

    //客户端
    public static void sendMessage(String s) {
        final InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            return;
        }
        try (final Socket socket = new Socket(inetAddress, point);
             final OutputStream outputStream = socket.getOutputStream();
        ) {
            outputStream.write(s.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        try (final InputStreamReader inputStreamReader = new InputStreamReader(System.in);
//             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//        ) {
//            while (true) {
//                System.out.println("请输入消息:");
//                final String s = bufferedReader.readLine();
//                sendMessage(s);
//                if ("exit".equalsIgnoreCase(s)) {
//                    break;
//                }
//            }
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        try (final InputStreamReader inputStreamReader = new InputStreamReader(System.in);
             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        ) {
            while (true) {
                System.out.println("请输入消息:");
                final String s = bufferedReader.readLine();
                if ("exit".equalsIgnoreCase(s)) {
                    break;
                }
                InetAddress inetAddress = InetAddress.getByName("localhost");
                final Socket socket = new Socket(inetAddress, point);
                final OutputStream outputStream = socket.getOutputStream();
                final InputStream inputStream = socket.getInputStream();
                outputStream.write(s.getBytes(StandardCharsets.UTF_8));
                //关闭输出流
                socket.shutdownOutput();
                //或字节流转换为字符流
                final InputStreamReader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                char[] ch = new char[10];
                int len;
                while ((len = in.read(ch)) != -1) {
                    System.out.println(new String(ch, 0, len));
                }
                in.close();
                outputStream.close();
                socket.close();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    @DisplayName("服务端")
    @Test
    public void server() throws IOException {
        final ServerSocket serverSocket = new ServerSocket(point);
        while (true) {
            try {
                //等待链接，程序进入阻塞状态
                final Socket socket = serverSocket.accept();
                System.out.println("新的链接...");
                final InputStream inputStream = socket.getInputStream();
                final OutputStream outputStream = socket.getOutputStream();
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bytes = new byte[100];
                int len;
                //read 会阻塞
                while ((len = inputStream.read(bytes)) != -1) {
                    byteArrayOutputStream.write(bytes, 0, len);
                }
                final String s = byteArrayOutputStream.toString();
                System.out.print(Thread.currentThread().getName());
                System.out.println(s);

                outputStream.write("收到！".getBytes(StandardCharsets.UTF_8));
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
