package com.xmg.jvm;

/**
 * @author mk
 * @created 2022/4/22
 **/
public class Test {
    @Override
    public String toString() {
        return "Test{" +
                "i=" + i +
                '}';
    }

    private int i = 1;

    public static void main(String[] args) {
        int i = 1;
        int j = 2;
        int k = i + j;
        final Test test = new Test();
        test.test0();
    }

    public void test0() {
        //非静态方法局部变量表中有一个this变量
        this.i = 2;
        int i = 1;
        int j = 2;
        //double long占据两个局部变量槽(slot)
        double d = 3d;
        float f = 5f;
        long l = 3L;
        int k = i + j;
        byte b = -128;
        System.out.println(this.toString());
    }

    @org.junit.jupiter.api.Test
    public void test1() {
        long l = 1123131;
        double d = 111;
        float f = 111;
        int i = 111;
        Long l0 = 1123131L;
        Double d0 = 111D;
        Float f0 = 111F;
        Integer i0 = 111;
    }
}
