package xmg.xmg.io;

import java.io.*;

/**
 * 标准输入输出流
 */
public class InAndOutStreamTest {
    public static void main(String[] args) {
        try (final InputStream in = System.in;
             InputStreamReader inputStreamReader = new InputStreamReader(in);
             BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
        ) {
            while (true){
                final PrintStream out = System.out;
                out.println("请输入:");
                final String s = bufferedReader.readLine();
                if ("exit".equalsIgnoreCase(s)) {
                    out.println("结束");
                    break;
                }
                out.println(s.toUpperCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
