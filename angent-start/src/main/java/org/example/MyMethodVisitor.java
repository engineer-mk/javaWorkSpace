package org.example;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import org.objectweb.asm.MethodVisitor;

/**
 * @author makui
 * @created 2023/4/24
 **/
public class MyMethodVisitor extends MethodVisitor {
    private final String name;
    private final String desc;

    public MyMethodVisitor(MethodVisitor mv, int access, String name, String desc) {
        super(Opcodes.ASM5, mv);
        this.name = name;
        this.desc = desc;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("Method " + name + " called with arguments:");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        Type[] argumentTypes = Type.getArgumentTypes(desc);
        for (int i = 0; i < argumentTypes.length; i++) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitVarInsn(argumentTypes[i].getOpcode(Opcodes.ILOAD), i + 1);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                    "(" + argumentTypes[i].getDescriptor() + ")V", false);
        }
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN || opcode == Opcodes.ATHROW) {
            Type returnType = Type.getReturnType(desc);
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("Method " + name + " returned with result:");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitInsn(returnType.getOpcode(Opcodes.IRETURN));
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                    "(" + returnType.getDescriptor() + ")V", false);
        }
        super.visitInsn(opcode);
    }
}
