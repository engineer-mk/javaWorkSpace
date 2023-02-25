package xmg.xmg.reflection.support;

import java.time.LocalTime;

public class PersonImpl implements Person {
    @Override
    public LocalTime sleep() {
        System.out.println("睡觉了...");
        return LocalTime.now();
    }

    @Override
    public LocalTime getUp() {
        System.out.println("起床了");
        return LocalTime.now();
    }
}
