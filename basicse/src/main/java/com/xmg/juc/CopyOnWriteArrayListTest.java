package com.xmg.juc;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author makui
 * @created 2022/3/26
 * CopyOnWriteArrayList
 **/
public class CopyOnWriteArrayListTest {

    /**
     * 弱一致性问题
     */
    @Test
    public void iteratorTest() throws InterruptedException {
        final CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("aa");
        list.add("bb");
        list.add("cc");
        list.add("dd");
        list.add("ee");
        final Thread writeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                list.set(1, "AA");
                list.remove(4);
            }
        });
        //此处遍历的是CopyOnWriteArrayList 中 array副本
        final Iterator<String> iterator = list.iterator();
        writeThread.start();
        writeThread.join();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
