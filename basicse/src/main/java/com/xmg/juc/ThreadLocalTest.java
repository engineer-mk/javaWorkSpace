package com.xmg.juc;

/**
 * 创建一个ThreadLocal变量后每个线程都会复制一个变量到自己的本地内存
 * 多个线程操作这个变量时实际操作的是自己的本地内存变量，从而避免了线程安全的问题
 */
public class ThreadLocalTest {
    /*ThreadLocal不支持继承，既主线程中的threadLocal变量子线程中无法访问 */
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();
    /*InheritableThreadLocal支持继承*/
    private static final InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();


    public static void main(String[] args) {
        threadLocal.set("hello");
        inheritableThreadLocal.set("hello");
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("子线程threadLocal变量值:" + threadLocal.get());
                System.out.println("子线程inheritableThreadLocal变量值:" + inheritableThreadLocal.get());
            }
        });

        thread.start();
        System.out.println("主线程threadLocal变量值:" + threadLocal.get());
        System.out.println("主线程inheritableThreadLocal变量值:" + inheritableThreadLocal.get());
    }
}
