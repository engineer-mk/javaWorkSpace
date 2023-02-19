package com.xmg.juc;

import java.util.concurrent.Semaphore;

/**
 * 信号量维持一组许可证。
 * 如果有必要，每个acquire()都会阻塞，直到许可证可用，然后才能使用它。
 * 每个release()添加许可证，潜在地释放阻塞获取方
 * @author makui
 * @created on  2022/9/29
 **/
public class SemaphoreTest {
    public static void main(String[] args) {
        //例:6辆车停三个停车位
        final Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i <= 6; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+ " 抢到了车位...");
                    Thread.sleep(1000 * finalI);
                    System.out.println(Thread.currentThread().getName()+ " >>>>>>>>>>>>>离开了车位...");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }finally {
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}
