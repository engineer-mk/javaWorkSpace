package com.xmg.juc;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 以线程安全的方式操作非线程安全对象的某些字段
 *
 * @author makui
 * @created on  2022/10/6
 **/
public class AtomReferenceFieldUpdaterTest {


    public static void main(String[] args) throws InterruptedException {
        final long start = System.currentTimeMillis();
        Account zhang = new Account(new BigDecimal("1000000"));
        final AtomicReferenceFieldUpdater<Account, BigDecimal> arfu =
                AtomicReferenceFieldUpdater.newUpdater(Account.class, BigDecimal.class, "balance");
        final CountDownLatch countDownLatch = new CountDownLatch(100);
        final BigDecimal sub = new BigDecimal("1");
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
//                    synchronized (zhang) {
//                        zhang.balance = zhang.balance.subtract(sub);
//                    }
                    while (!arfu.compareAndSet(zhang, zhang.balance, zhang.balance.subtract(sub))) {

                    }
                }
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println(zhang.balance.toString());
        System.out.println((System.currentTimeMillis() - start) + ":ms");
    }

    @NoArgsConstructor
    static class Account {
        public volatile BigDecimal balance;

        public Account(BigDecimal balance) {
            this.balance = balance;
        }
    }

}


