package com.xmg.jvm;

import org.junit.jupiter.api.Test;

/**
 * @author mk
 * @created on 2022/5/18
 * 栈上分配测试
 **/
//-Xms600m -Xmx600m  -XX:-DoEscapeAnalysis -XX: MetaspaceSize=21m -XX: MaxMetaspaceSize=21m
public class EscapeAnalysis {


    public static void main(String[] args) throws InterruptedException {
        final long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            run();
        }
        final long end = System.currentTimeMillis();
        System.out.println(end - start + "ms");
        Thread.sleep(1000000);

    }


    private static void run() {
        final User user = new User();
    }

    static class User {
    }

}
