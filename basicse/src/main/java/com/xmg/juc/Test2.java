package com.xmg.juc;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

/**
 * @author makui
 * @created 2022/3/23
 * 缓存行 伪共享问题
 * 多个变量被放入同一个缓存行，多个线程去写入同一个缓存行中不同变量
 * 多个线程不可能同时去修改自己所使用的cpu中相同缓存行的变量
 **/
public class Test2 {
    private static int i = 1;
    public static final int line = 1024;
    public static final int colum = 1024;

    //10次平均时间14.3ms

    @Test
    @RepeatedTest(10)
    public void test0() {
        final long start = System.currentTimeMillis();
        long[][] array = new long[line][colum];
        for (int i = 0; i < line; i++) {
            for (int j = 0; j < colum; j++) {
                array[i][j] = 666;
            }
        }
        final long end = System.currentTimeMillis();
        System.out.println("time: " + (end - start));
    }

    //10次平均时间15.5ms
    @Test
    @RepeatedTest(10)
    public void test2() {
        final long start = System.currentTimeMillis();
        long[][] array = new long[line][colum];
        for (int i = 0; i < colum; i++) {
            for (int j = 0; j < line; j++) {
                array[j][i] = 666;
            }
        }
        final long end = System.currentTimeMillis();
        System.out.println("time: " + (end - start));
    }

}
