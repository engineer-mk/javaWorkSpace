package xmg.xmg.juc;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.locks.LockSupport;

/**
 * 锁升级
 * synchronized
 */
public class SynchronizedTest {
    final static Object o = new Object();
    private static int sum = 0;


    /**
     * 偏向锁 ->轻量锁->重量锁
     * 实际中 一个锁总是有同一个线程所占有
     *
     * @param args
     * -XX:BiasedLockingStartupDelay=0 偏向锁启动延迟
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        Thread.sleep(4500);
        final Object o1 = new Object();
        System.out.println(ClassLayout.parseInstance(o1).toPrintable());
        for (int i = 0; i < 300; i++) {
            synchronized (o) {
                sum++;
                if (i == 0 || i == 250) {
                    System.out.println(ClassLayout.parseInstance(o).toPrintable());
                }
            }
        }

        LockSupport.park();
    }
}
