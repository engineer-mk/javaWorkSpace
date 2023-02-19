package com.xmg.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/** 读写锁 及 锁降级
 * @author makui
 * @created on  2022/9/29
 **/
public class ReentrantReadWriteLockTest {

    final static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public static void main(String[] args) {

        new Thread(() -> {
            rwLock.readLock().lock();
            System.out.println(Thread.currentThread().getName() + " 获取到读锁...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rwLock.readLock().unlock();
        }, "ThreadB").start();

        new Thread(() -> {
            rwLock.writeLock().lock();
            System.out.println(Thread.currentThread().getName() + " 获取到写锁...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rwLock.writeLock().unlock();
        }, "ThreadA").start();

    }

    //锁降级
    @Test
    public void test2(){
        rwLock.writeLock().lock();
        System.out.println(Thread.currentThread().getName() + " 获取到写锁...");
        rwLock.readLock().lock();
        System.out.println(Thread.currentThread().getName() + " 获取到读锁...");
        rwLock.writeLock().unlock();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rwLock.readLock().unlock();
    }

    //锁不可以升级
    @Test
    public void test3(){
        rwLock.readLock().lock();
        System.out.println(Thread.currentThread().getName() + " 获取到读锁...");
        rwLock.writeLock().lock();
        System.out.println(Thread.currentThread().getName() + " 获取到写锁...");
        rwLock.readLock().unlock();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rwLock.writeLock().unlock();
    }
}
