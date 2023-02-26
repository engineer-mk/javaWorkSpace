package com.xmg.reflection;

import com.xmg.reflection.support.Person;
import com.xmg.reflection.support.PersonImpl;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalTime;


public class ProxyTest {

    static class PersonInvocationHandler implements InvocationHandler {
        private final Person person;

        PersonInvocationHandler(Person person) {
            this.person = person;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("方法执行前...");
            final Object value = method.invoke(person, args);
            System.out.println("方法执行后...");
            return value;
        }
    }

    //必须实现接口
    @DisplayName("JDK动态代理")
    @Test
    public void test1() {
        Person person = new PersonImpl();
        final Person proxyInstance = (Person) Proxy.newProxyInstance(person.getClass().getClassLoader(),
                person.getClass().getInterfaces(),
                new PersonInvocationHandler(person));
        final LocalTime sleep = proxyInstance.sleep();
        System.out.println(sleep);
        final LocalTime up = proxyInstance.getUp();
        System.out.println(up);
    }

    static class CglibPersonProxy implements MethodInterceptor {

        private final Person person;

        CglibPersonProxy(Person person) {
            this.person = person;
        }

        public Object getProxyInstance() {
            final Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(person.getClass());
            enhancer.setCallback(this);
            return enhancer.create();
        }

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            System.out.println("方法执行前...");
            final Object invoke = method.invoke(person, args);
            System.out.println("方法执行后...");
            return invoke;
        }
    }

    @DisplayName("cglib动态代理")
    @Test
    public void test2() {
        Person person = new PersonImpl();
        CglibPersonProxy cglibPersonProxy = new CglibPersonProxy(person);
        final Person obj = (Person) cglibPersonProxy.getProxyInstance();
        final LocalTime up = obj.getUp();
        obj.sleep();
    }

}
