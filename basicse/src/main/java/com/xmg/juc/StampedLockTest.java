package com.xmg.juc;

import java.util.concurrent.locks.StampedLock;

/**
 * 邮戳锁  乐观锁
 * 为了解决ReentrantReadWriteLock存在锁饥饿问题
 *
 * @author makui
 * @created on  2022/10/8
 **/
public class StampedLockTest {
   static   int i = 0;
    public static void main(String[] args) throws InterruptedException {

        final StampedLock stampedLock = new StampedLock();

//        new Thread(() -> {
//            System.out.println("悲观读");
//            final long stamp = stampedLock.readLock();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            int result = i;
//            System.out.println("读取完成结果:" + result);
//            stampedLock.unlock(stamp);
//        },"读线程").start();

        //乐观读
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "进入");
            final long stamp = stampedLock.tryOptimisticRead();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int result = i;
            if (!stampedLock.validate(stamp)) {
                System.out.println("数据被修改了...");
                //后续可尝试悲观读
            }else {
                System.out.println("读取完成结果:" + result);
            }
        },"读线程").start();

        Thread.sleep(10);

        new Thread(() -> {
            final long stamp = stampedLock.writeLock();
            System.out.println(Thread.currentThread().getName() + "进入");
            i++;
            stampedLock.unlock(stamp);
        }, "写线程").start();
    }
}
