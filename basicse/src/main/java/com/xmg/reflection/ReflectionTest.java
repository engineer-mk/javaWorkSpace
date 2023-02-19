package com.xmg.reflection;

import com.xmg.reflection.support.Info;
import com.xmg.reflection.support.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.event.WindowFocusListener;
import java.lang.reflect.*;
import java.util.Arrays;

public class ReflectionTest {

    @DisplayName("Class实例的获取")
    @Test
    public void test1() throws ClassNotFoundException {
        //1。运行时类的属性
        final Class<User> c1 = User.class;
        //2.运行时类的对象
        final User user = new User();
        final Class<? extends User> c2 = user.getClass();
        //3.Class静态方法
        final Class<?> c3 = Class.forName("com.xmg.reflection.support.User");
        //4.类加载器
        final ClassLoader classLoader = ReflectionTest.class.getClassLoader();
        final Class<?> c4 = classLoader.loadClass("com.xmg.reflection.support.User");

        System.out.println(c1 == c2 && c1 == c3 && c1 == c4); //true
    }

    @DisplayName("反射获取类属性")
    @Test
    public void test2() throws ClassNotFoundException {
        final Class<?> aClass = Class.forName("com.xmg.reflection.support.User");
        //获取类及其父类中public属性
        final Field[] fields = aClass.getFields();
        for (Field field : fields) {
            System.out.print(field.getName() + "\t");
            System.out.print(Modifier.toString(field.getModifiers()) + "\t");
            System.out.println(field.getType().getName());
        }
        System.out.println();
        //获取当前类中所有属性(不包含父类)
        final Field[] declaredFields = aClass.getDeclaredFields();
        for (Field df : declaredFields) {
            System.out.print(df.getName() + "\t"); //属性名称
            System.out.print(Modifier.toString(df.getModifiers()) + "\t"); //访问权限
            System.out.println(df.getType().getName() + "\t");//属性数据类型
        }
        System.out.println();
        try {
            final Field username = aClass.getDeclaredField("username");
            final Info annotation = username.getAnnotation(Info.class);
            if (annotation != null) {
                System.out.println("注解信息:" + annotation.value());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("反射获取类方法")
    @Test
    public void test3() throws ClassNotFoundException {
        final Class<?> aClass = Class.forName("com.xmg.reflection.support.User");
        //获取类及其父类中public方法
        final Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            System.out.print(method.getName() + "\t");
            //获取声明该方法的类对象
            final Class<?> c = method.getDeclaringClass();
            System.out.print(Modifier.toString(method.getModifiers()) + "\t");
            System.out.println(method.getReturnType());
        }
        System.out.println();
        //获取当前类中所有方法(不包含父类)
        final Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            System.out.print(method.getName() + "\t");
            System.out.print(Modifier.toString(method.getModifiers()) + "\t");
            System.out.println(method.getReturnType());
        }
        System.out.println();
        try {
            final Method hello = aClass.getDeclaredMethod("hello", String.class, Integer.class, boolean.class);
            //注解
            final Info annotation = hello.getAnnotation(Info.class);
            if (annotation != null) {
                System.out.println("注解信息:" + annotation.value());
            }
            //参数
            final Parameter[] parameters = hello.getParameters();
            for (Parameter parameter : parameters) {
                System.out.print(parameter.getName() + "\t");
                System.out.println(parameter.getType().getName());
            }
            //参数类型
            final Class<?>[] parameterTypes = hello.getParameterTypes();
            System.out.println(Arrays.toString(parameterTypes));
            //异常
            final Class<?>[] exceptionTypes = hello.getExceptionTypes();
            for (Class<?> exceptionType : exceptionTypes) {
                System.out.println(exceptionType.getTypeName());
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("反射获取类构造方法")
    @Test
    public void test4() throws ClassNotFoundException {
        final Class<?> aClass = Class.forName("com.xmg.reflection.support.User");
        //获取类的public构造方法
        final Constructor<?>[] constructors = aClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println(constructor);
        }
        System.out.println();
        //获取类中的所有构造方法
        final Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            System.out.println(declaredConstructor);
        }

        try {
            final Constructor<?> constructor = aClass.getConstructor(String.class, String.class);
            final User laoW = (User) constructor.newInstance("laoW", "666");
            System.out.println(laoW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DisplayName("获取父类,接口,范型")
    @Test
    public void test5() throws ClassNotFoundException {
        final Class<?> aClass = Class.forName("com.xmg.reflection.support.User");
        //获取父类
        final Class<?> superclass = aClass.getSuperclass();
        System.out.println(superclass);
        //获取接口
        final Class<?>[] interfaces = aClass.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            System.out.println(anInterface);
        }
        //获取接口泛型类型
        final Type[] genericInterfaces = aClass.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            System.out.println(genericInterface);
            ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
            System.out.println(Arrays.toString(parameterizedType.getActualTypeArguments()));
        }
        //获取带范型父类
        final Type genericSuperclass = aClass.getGenericSuperclass();
        System.out.println(genericSuperclass);
        //获取父类范型类型
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        System.out.println(Arrays.toString(actualTypeArguments));
    }

    @DisplayName("综合测试")
    @Test
    public void test6() throws Exception {
        final Class<?> aClass = Class.forName("com.xmg.reflection.support.User");
        //构造器
        final Constructor<?> constructor = aClass.getConstructor(String.class, String.class);
        final User user = (User) constructor.newInstance("laoW", "666");
        //公有方法
        final Method setUsername = aClass.getMethod("setUsername", String.class);
        final Method getUsername = aClass.getMethod("getUsername");
        setUsername.invoke(user, "w");
        //私有属性
        final Field phone = aClass.getDeclaredField("phone");
        //设置为可访问的 否则操作私有类型抛异常
        phone.setAccessible(true);
        phone.set(user,"777");
        //调用私有静态方法
        final Method ahh = aClass.getDeclaredMethod("ahh");
        ahh.setAccessible(true);
        ahh.invoke(null);
        System.out.println(getUsername.invoke(user));
        System.out.println(user);
    }
}
