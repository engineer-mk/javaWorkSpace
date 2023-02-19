package com.xmg.juc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子引用类
 *
 * @author makui
 * @created on  2022/10/6
 **/
public class AtomicReferenceTest {


    public static void main(String[] args) throws InterruptedException {
        //引用类型的原子类
        final User zhang = new User();
        zhang.setAge(25);
        final AtomicReferenceUser atomicReference = new AtomicReferenceUser(zhang);
        final User li = new User();
        System.out.println(atomicReference.compareAndSet(zhang, li));
        System.out.println(atomicReference.compareAndSet(zhang, li));


        final SpinLockTest spinLock = new SpinLockTest();
        final Thread t1 = new Thread(() -> {
            spinLock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " 获取到锁");
                Thread.sleep(2000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println(Thread.currentThread().getName() + " 释放锁");
                spinLock.unlock();
            }
        }, "t1");
        final Thread t2 = new Thread(() -> {
            spinLock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " 获 取 到 锁");
                Thread.sleep(2000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println(Thread.currentThread().getName() + " 释 放 锁");
                spinLock.unlock();
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    /**
     * 自旋锁实现
     */
    static class SpinLockTest extends AtomicReference<Thread> {

        public void lock() {
            final Thread currentThread = Thread.currentThread();
            final Thread thread = get();
            if (thread != null && currentThread == thread) {
                return;
            }
            while (!compareAndSet(null, currentThread)) {

            }
        }

        public void unlock() {
            final Thread currentThread = Thread.currentThread();
            final Thread thread = get();
            if (thread == null || currentThread != thread) {
                throw new RuntimeException("当前线程未持有锁。");
            }
            while (!compareAndSet(currentThread, null)) {

            }
        }

    }


    @Getter
    @Setter
    static class AtomicReferenceUser extends AtomicReference<User> {
        private User user;

        public AtomicReferenceUser(User user) {
            this.set(user);
            this.user = user;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class User {
        private volatile int age;
    }

}
