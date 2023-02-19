package com.xmg.juc;

/**
 * 死锁演示
 * @author makui
 * @created on  2022/9/29
 **/
public class DeadLockTest {
    static final Object a = new Object();
    static final Object b = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (a) {
                System.out.println("A线程 获取到资源a....");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (b) {
                    System.out.println("A线程获取到资源b...");
                }
            }
        }, "A").start();

        new Thread(() -> {
            synchronized (b) {
                System.out.println("B线程 获取到资源b....");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (a) {
                    System.out.println("B线程获取到资源a...");
                }
            }
        }, "B").start();
    }
}
