package com.timmy.asmstudy;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_7;


//package com.timmy.asmstudy;
//
//public class AsmDemo {
//
//    public static void main(String[] args) {
//        System.out.println("Hello ASM");
//    }
//}

/**
 * 使用ASM 动态生成如下 .class文件
 */
public class MyClass extends ClassLoader implements Opcodes {

    public static void main(String[] args) throws Exception {

        System.out.println("-----MyClass----");
        //定义一个类模版
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_7, ACC_PUBLIC, "AsmDemo11", null, "java/lang/Object", null);

        //TODO 1构造默认构造函数
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC,
                "<init>",
                "()V",
                null, null);
        //生成构造函数字节码指令 -- 加载操作
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL,
                "java/lang/Object",
                "<init>",
                "()V",
                false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        //TODO 2。构造main函数
        mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC,
                "main",
                "([Ljava/lang/String;)V",
                null,
                null);

        //TODO 3。main方法中生成 System.out.println("Hello ASM");
        //获取System类中的属性  System.out --    public static final PrintStream out;
        mv.visitFieldInsn(GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;");

        //栈帧中 属性入栈
        mv.visitLdcInsn("Hello ASM 123");
        //加载 println 方法
        mv.visitMethodInsn(INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V",
                false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();

        //获取生成的class 文件对应的二进制流
        byte[] codes = cw.toByteArray();

        //将二进制流写入到本地磁盘上
        FileOutputStream fos = new FileOutputStream("AsmDemo11.class");
        fos.write(codes);
        fos.close();

        //反射调用
        MyClass loader = new MyClass();
        Class<?> defineClass = loader.defineClass("AsmDemo11", codes, 0, codes.length);
        defineClass.getMethods()[0].invoke(null, new Object[]{null});

    }
}














