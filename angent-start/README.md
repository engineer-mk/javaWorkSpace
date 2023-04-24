```
Manifest-Version: 1.0
//指定Java Agent的premain方法所在的类，该类需要实现public static void premain(String agentArgs, Instrumentation inst)方法。
//在Java应用程序启动之前，JVM会调用这个方法，用于进行代理的初始化工作
Premain-Class: agent.AgentMain
指定Java Agent的agentmain方法所在的类，该类需要实现public static void agentmain(String agentArgs, Instrumentation inst)方法。
//在Java应用程序启动之后，可以通过动态加载Java Agent并调用该方法，用于对Java应用程序进行代理
Agent-Class: agent.AgentMain
```
