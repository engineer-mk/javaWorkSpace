package xmg.xmg.jvm;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

/**
 * 内存泄漏几种情况分析
 *
 * @author makui
 * @created on  2022/10/16
 **/
public class MemoryLeakTest {

    static class A {
        private int age;
        private String name;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            A a = (A) o;
            return age == a.age && Objects.equals(name, a.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(age, name);
        }
    }

    //哈希值改变
    @Test
    public void test0() {
        HashMap<A,String> hashMap = new HashMap<>();
        final A a = new A();
        a.age = 1;
        a.name = "zhang";
        hashMap.put(a, a.name);
        for (Map.Entry<A, String> entry : hashMap.entrySet()) {
            final A key = entry.getKey();
            key.age = 2;
        }
        //上边修改了a对象的hash，此时会再放入一个对象到map中
        hashMap.putIfAbsent(a, "wang");
        System.out.println(hashMap.size());
    }
}
