package com.xmg.juc;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch(减少计数器)用给定的计数初始化。
 * await方法阻塞，直到由于countDown()方法的调用而导致当前计数达到零，
 * 之后所有等待线程被释放，并且任何后续的await 调用立即返回。
 * 这是一个一次性的现象 - 计数无法重置。
 * 如果您需要重置计数的版本，请考虑使用CyclicBarrier 。
 * 一个或者多个线程，等待其他多个线程完成某件事情之后才能执
 * @author makui
 * @created on  2022/9/29
 **/
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(finalI + "完成...");
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println("主线线程结束...");
    }
}
