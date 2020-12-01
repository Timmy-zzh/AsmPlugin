package com.timmy.myplugin;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.RETURN;

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
        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "i",
                "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(POP);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == RETURN) {
            insertCode();
        }
        super.visitInsn(opcode);
    }

    private void insertCode() {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "com/timmy/asmplugin/MainActivity", "tag", "Z");
        Label label1 = new Label();
        mv.visitJumpInsn(IFEQ, label1);
        Label label2 = new Label();
        mv.visitLabel(label2);
        mv.visitLdcInsn("Tim");
        mv.visitLdcInsn("tag is ture  11111");
        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(POP);
        Label label3 = new Label();
        mv.visitJumpInsn(GOTO, label3);
        mv.visitLabel(label1);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitLdcInsn("Tim");
        mv.visitLdcInsn("tag is false 22222");
        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(POP);
        mv.visitLabel(label3);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitInsn(RETURN);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
