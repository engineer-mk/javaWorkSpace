package org.example;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;


public class MyTransformer implements ClassFileTransformer {
    private final String className;
    private final String methodName;

    public MyTransformer(String... args) {
        this.className = args[0];
        this.methodName = args[1];
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        try {
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

            // 检查类名是否匹配
            if (!ctClass.getName().equals(this.className)) {
//                System.out.println("Class " + className + " not matched, skip");
                return classfileBuffer;
            }
            System.out.println("Class " + className + " matched, start to modify method " + methodName);

            // 获取要修改的方法
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);

            // 修改字节码，添加日志
            ctMethod.insertBefore("System.out.println(\"Method " + methodName + " called with arguments: \" + java.util.Arrays.toString($args));");
            ctMethod.insertAfter("System.out.println(\"Method " + methodName + " returned value: \" + $_);");

            // 返回修改后的字节码
            byte[] byteCode = ctClass.toBytecode();
            ctClass.detach();
            return byteCode;
        } catch (IOException | NotFoundException | CannotCompileException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}


