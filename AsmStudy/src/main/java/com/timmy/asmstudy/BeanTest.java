package com.timmy.asmstudy;

//package com.timmy.asmstudy;
//import com.sun.istack.internal.NotNull;
//
//public class Person {
//    @NotNull
//    public String name;
//}

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Field;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_7;

/**
 * 动态生成如上诉Person类
 * 1。使用ClassWrite创建定义Person类
 * 2。生成构造函数
 * 3。生成属性 name
 */
public class BeanTest extends ClassLoader {

    public static void main(String[] args) throws Exception {
        //定义类
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_7,
                ACC_PUBLIC + ACC_SUPER,
                "com/timmy/asmstudy/Person11",
                null,
                "java/lang/Object",
                null);

        //构造函数
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null);

        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL,
                "java/lang/Object",
                "<init>",
                "()V",
                false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        //构建属性name
        FieldVisitor fv = cw.visitField(ACC_PUBLIC,
                "name11",
                "Ljava/lang/String;",
                null,
                null);
        {
            //为属性添加注解
            AnnotationVisitor av0 = fv.visitAnnotation(
                    "Lcom/sun/istack/internal/NotNull;",
                    false);
            av0.visitEnd();
        }
        fv.visitEnd();

        //生成并加载
        byte[] bytes = cw.toByteArray();

        BeanTest beanTest = new BeanTest();
        Class<?> aClass = beanTest.defineClass(null, bytes, 0, bytes.length);

        //使用classLoader加载好后，存在内存中，通过反射创建对象
        Object beanObj = aClass.getConstructor().newInstance();
        //拿到属性 --并设置值
        Field field = aClass.getField("name11");
        field.set(beanObj, "zzzzzzz");

        String nameVal = (String) field.get(beanObj);
        System.out.println("name value:" + nameVal);

    }
}
