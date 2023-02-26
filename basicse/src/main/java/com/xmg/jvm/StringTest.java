package com.xmg.jvm;

/**
 * @author makui
 * @created on  2022/10/9
 **/
public class StringTest {
    public void a() {
        String str = "a";
    }

    public void b() {
        String str = new String("b");
    }

    public static void c(String s, Integer integer) {
        s = "1";
        integer = 1;
    }

    public static void main(String[] args) {
        String s1 = "abc";
        String s2 = "abc";
        System.out.println(s2 == s1); //true

        Integer i1 = 999;
        Integer i2 = 999;
        System.out.println(i1 == i2); //false

        Integer i3 = 99;
        Integer i4 = 99;
        System.out.println(i3 == i4); //true

        Integer i5 = new Integer(99);
        Integer i6 = new Integer(99);
        System.out.println(i5 == i6); //false

        String s3 = new String("abc");
        String s4 = new String("abc");
        System.out.println(s3 == s4); //false
        int i = 0;
        //string和基本数据类型一样，是不可变类型
        c(s1, i);
        System.out.println(s1);
        System.out.println(i);

        String s5 = "a" + "b" + "c"; //编译阶段已经优化
        System.out.println(s5 == s1); //true
        String s6="a";
        String s7 = s6 + "bc";
        System.out.println(s7 == s1); //false 底层用StringBuilder拼接

        //此处并没有在字符串常量池中放入11
        String s8 = new String("1") + new String("1");
        //此时直接把引用指向s8，并没有在字符串常量池中创建一个新对象。
        s8.intern();
        String s9 = "11";
        System.out.println(s8 == s9); //true
    }
}
