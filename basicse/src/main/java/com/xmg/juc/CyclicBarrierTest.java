package com.xmg.juc;

import java.awt.*;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier 循环栅栏
 * 多个线程互相等待，直到到
 * 达同一个同步点，再继续一起执行
 * @author makui
 * @created on  2022/9/29
 **/
public class CyclicBarrierTest {
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->{
            System.out.println("完成...");
        });

        //cyclicBarrier.await() 每达到7次所有线程解除阻塞状态，未达到7次则一直阻塞下去
        for (int i = 0; i < 14; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    Thread.sleep(1000* finalI);
                    cyclicBarrier.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName());
                System.out.println((System.currentTimeMillis()-start)/1000 +"s");
            },String.valueOf(i)).start();
        }
    }
}
