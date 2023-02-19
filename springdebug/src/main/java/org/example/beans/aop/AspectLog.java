package org.example.beans.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author makui
 * @created on  2022/9/28
 **/
@Component
@Aspect
public class AspectLog {
    @Pointcut("execution(public * org.example.beans.UserA.a())")
    private void point() {

    }

    @Before("point()")
    public void before(JoinPoint joinPoint) {
        final String name = joinPoint.getSignature().getName();
        System.out.println("切面:" + name);
    }
}
