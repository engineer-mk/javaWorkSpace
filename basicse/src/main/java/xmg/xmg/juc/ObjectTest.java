package xmg.xmg.juc;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.locks.LockSupport;

/**
 * @author makui
 * @created on  2022/10/7
 **/
public class ObjectTest {
    private int id=1;
    private String name = "wangaage";
    private Object object = new Object();
    private byte b;


    //-XX:+UseCompressedClassPointers 开启对象头类型指针压缩
    public static void main(String[] args) {

        final Object o = new ObjectTest();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        LockSupport.park();
    }
}
