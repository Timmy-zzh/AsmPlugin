package com.timmy.myplugin;

import org.objectweb.asm.*;

public class LifecycleMethodVisitor extends MethodVisitor {

    private String className;
    private String methodName;

    public LifecycleMethodVisitor(MethodVisitor mv, String className, String methodName) {
        super(Opcodes.ASM5, mv);
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        System.out.println("==LifecycleMethodVisitor==visitCode==className:" + className + " ,methodName:" + methodName);
        mv.visitLdcInsn("TAG");
        mv.visitLdcInsn(className + "---->" + methodName);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i",
                "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);
    }

}
