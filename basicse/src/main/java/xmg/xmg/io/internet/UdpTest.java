package xmg.xmg.io.internet;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class UdpTest {

    @Test
    public void sen() {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
            DatagramPacket datagramPacket = new DatagramPacket(data, 0, data.length, InetAddress.getLocalHost(), 8888);
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }

    @Test
    public void receive() throws IOException {
        final DatagramSocket datagramSocket = new DatagramSocket(8888);
        byte[] bytes = new byte[100];
        final DatagramPacket datagramPacket = new DatagramPacket(bytes,0,bytes.length);
        datagramSocket.receive(datagramPacket);
        String data = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        System.out.println(data);
        datagramSocket.close();
    }
}
