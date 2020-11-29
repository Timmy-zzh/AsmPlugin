package com.timmy.myplugin;

import org.objectweb.asm.*;

public class LifecycleClassVistor extends ClassVisitor {

    private String className;
    private String superName;

    public LifecycleClassVistor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.superName = superName;
        System.out.println("--LifecycleClassVistor--visit--className:" + className + " ,superName:" + superName);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println("--LifecycleClassVistor--visitMethod--methodName:" + name + " ,desc:" + desc);
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
//        if (superName.equals("android/support/v7/app/AppCompatActivity")) {
        if (superName.equals("androidx/appcompat/app/AppCompatActivity")) {
            if (name.startsWith("onCreate")) {
                return new LifecycleMethodVisitor(methodVisitor,className,name);
            }
        }
        return methodVisitor;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
