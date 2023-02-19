package com.xmg.juc;

import java.util.concurrent.locks.LockSupport;

/**
 * @author makui
 * @created 2022/3/26
 * LockSupport工具类
 **/
public class LockSupportTest {

    //park() 阻塞挂起 禁止当前线程进行线程调度，除非许可证可用。
    //park(Object blocker) 推荐使用此方法，方便查看那个类被阻塞
    //LockSupport.unpark(thread)为给定的线程提供许可证(如果尚未提供) park()阻塞的线程立即返回
    //其他线程调用该线程的interrupt()方法时返回, 且不会抛出InterruptedException线程中断异常
    public static void main(String[] args) {
        final Object o = new Object();
        final long start = System.currentTimeMillis();
        final Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (o) {
                    try {
                        //wait之前必须先获得o对象的锁，wait调用后阻塞当前线程，释放持有的o对象的锁
                        o.wait(1000);
//                        LockSupport.park();
                        Thread.currentThread().interrupt();
                        System.out.println(System.currentTimeMillis() - start);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
//        LockSupport.unpark(thread);
        //添加延迟让notifyAll()在wait()之后执行
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (o) {
            o.notifyAll();
        }
    }
}
