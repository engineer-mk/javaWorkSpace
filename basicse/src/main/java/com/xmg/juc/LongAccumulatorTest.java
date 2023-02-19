package com.xmg.juc;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.LongBinaryOperator;

/**
 * @author makui
 * @created on  2022/10/6
 **/
public class LongAccumulatorTest {
    public static void main(String[] args) {
        final LongAccumulator longAccumulator = new LongAccumulator(new LongBinaryOperator() {
            @Override
            public long applyAsLong(long left, long right) {
                return left * right;
            }
        }, 1);
        longAccumulator.accumulate(2);
        System.out.println(longAccumulator.get());
        longAccumulator.accumulate(2);
        System.out.println(longAccumulator.get());

        final LongAdder longAdder = new LongAdder();
        longAdder.add(1);
        longAdder.add(2);
        System.out.println(longAdder.sum());
    }

}
