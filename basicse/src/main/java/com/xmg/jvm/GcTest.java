package com.xmg.jvm;

import java.util.ArrayList;
import java.util.concurrent.locks.LockSupport;

/**
 * @author makui
 * @created on  2022/8/30
 **/
public class GcTest {
    public static void main(String[] args) throws InterruptedException {
        final ArrayList<Tobj> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(new Tobj());
        }
        LockSupport.park();
    }

    static class Tobj {

    }
}
