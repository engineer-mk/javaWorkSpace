package org.example;

import java.lang.instrument.Instrumentation;

/**
 * @author makui
 * @created 2023/4/24
 **/
public class MyAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Agent premain method is called with args: " + agentArgs);
        // 在premain方法中实现代理逻辑
        inst.addTransformer(new MyTransformer());
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("Agent agentmain method is called with args: " + agentArgs);
        // 在agentmain方法中实现代理逻辑
    }
}
