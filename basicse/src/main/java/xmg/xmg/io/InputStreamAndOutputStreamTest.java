package xmg.xmg.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

@DisplayName("字节输入输出流")
public class InputStreamAndOutputStreamTest {

    @DisplayName("字节输入输出流->InputStream,OutputStream")
    @Test
    public void  test1(){
        final long start = System.currentTimeMillis();
        File file = new File("/Users/mk/Downloads/Parallels_Desktop_Business_Edition_v16.5.0-49183__TNT.dmg");
        File file2 = new File("/Users/mk/Downloads/Parallels_Desktop_Business_Edition_v16.5.0-49183__TNT2.dmg");
        try(InputStream inputStream=new FileInputStream(file);
        OutputStream outputStream=new FileOutputStream(file2)) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final long end = System.currentTimeMillis();
        System.out.println("用时："+(end-start));
    }

    @DisplayName("字节输入输出包装处理流->BufferedInputStream,BufferedOutputStream")
    @Test
    public void  test2(){
        final long start = System.currentTimeMillis();
        File file = new File("/Users/mk/Downloads/Parallels_Desktop_Business_Edition_v16.5.0-49183__TNT.dmg");
        File file2 = new File("/Users/mk/Downloads/Parallels_Desktop_Business_Edition_v16.5.0-49183__TNT2.dmg");
        try(InputStream inputStream=new FileInputStream(file);
            BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream);
            OutputStream outputStream=new FileOutputStream(file2);
            BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(outputStream);
            ) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final long end = System.currentTimeMillis();
        System.out.println("用时："+(end-start));
    }
}
