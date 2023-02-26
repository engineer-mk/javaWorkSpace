package com.xmg.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * volatile可见效，有序性 ，无原子性，流程如下图
 * <img width="550" height="300" src="https://xmgcs.oss-cn-shenzhen.aliyuncs.com/resource/121664973965_.pic.jpg">
 *
 * @author makui
 * @created on  2022/10/5
 **/
public class VolatileTest {
    //public volatile static boolean b = true;
    public static boolean b = true;

    //可见性
    //volatile变量保证了共享变量在多个线程之间的可见性
    //非volatile 变量t1线程将无法停止(while循环中不能有任何操作)
    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " come in");
            while (b) {

            }
            System.out.println(Thread.currentThread().getName() + " end!");
        }, "t1").start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        b = false;
    }


    public static AtomicInteger aSum = new AtomicInteger(0);
    public static LongAdder la = new LongAdder();
    //public volatile static Integer sum = 0;
    public static int sum = 0;
    final static Object lock = new Object();

    /**
     * 非原子性
     * volatile 无法保证sum变量++的原子性，适用于写入值不依赖当前值的情况
     */
    @Test
    public void test1() {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    sum++;
                }
            }, String.valueOf(i)).start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(sum);
    }

    /**
     * 对上述并发问题的改进
     * 1. synchronized 加锁平均耗时135ms
     * 2. cas  AtomicInteger 平均耗时23ms
     * 3. cas  LongAdder 平均耗时19ms
     */
    @Test
    public void test2() throws InterruptedException {
        int threadCount = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        final long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
//                    synchronized (lock) {
//                        sum++;
//                    }
                    aSum.getAndAdd(1);
//                    la.increment();
                }
                countDownLatch.countDown();
            }, String.valueOf(i)).start();

        }
        countDownLatch.await();
        System.out.println((System.currentTimeMillis() - start) + ":ms");
        System.out.println(sum);
        System.out.println(aSum.get());
        System.out.println(la.sum());
    }

}
