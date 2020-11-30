package com.timmy.myplugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import groovy.io.FileType
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.apache.commons.io.FileUtils

public class LifeCycleTransform extends Transform {

    /**
     * 设置自定义Transform对应的Task名称
     */
    @Override
    String getName() {
        return "LifeCycleTransform"
    }

    /**
     * 设置自定义Transform接收的文件类型；可选如下：
     * CLASSES(1),   -- 代表只检索.class文件
     * RESOURCES(2); -- 代表检索java标准资源文件
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * 代表自定义Transform检索的范围：
     * PROJECT(1),   -- 只有项目内容
     * SUB_PROJECTS(4), -- 只有子项目内容
     * EXTERNAL_LIBRARIES(16), --只有外部类
     * TESTED_CODE(32), -- 由当前变量（包括依赖项）测试的代码
     * PROVIDED_ONLY(64), -- 只提供本地或远程依赖项
     * PROJECT_LOCAL_DEPS(2) --只有项目的本地依赖项（本地jar）
     * SUB_PROJECTS_LOCAL_DEPS(8); --只有子项目的本地依赖项（本地jar）
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.PROJECT_ONLY
    }

    /**
     * 当前Transform是否支持增量编译
     */
    @Override
    boolean isIncremental() {
        return false
    }

    /**
     * 获取所有的.class文件，
     * inputs: 传过来的输入流
     * outputProvider： 输出的目录，必须将修改后的文件复制到输出目录
     * Collection<TransformInput> inputs = transformInvocation.inputs
     * TransformOutputProvider transformOutputProvider = transformInvocation.outputProvider
     */
    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
//        super.transform(transformInvocation)
        System.out.println("=========transform=========")

        Collection<TransformInput> transformInputs = transformInvocation.inputs
        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        if (outputProvider != null) {
            outputProvider.deleteAll()
        }

        transformInputs.each { TransformInput transformInput ->

            transformInput.directoryInputs.each { DirectoryInput directoryInput ->
                File dir = directoryInput.file
                if (dir) {
                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File file ->
                        System.out.println("find class: " + file.name)

                        //对class文件进行读取与解析
                        ClassReader classReader = new ClassReader(file.bytes)
                        //对class文件的写入
                        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                        //访问class文件相应的内容，解析到某一个结构就会通知到ClassVisitor的相应方法
                        ClassVisitor classVisitor = new LifecycleClassVistor(classWriter)
                        //依次调用 ClassVisitor接口的各个方法
                        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                        //toByteArray方法会将最终修改的字节码以 byte 数组形式返回。
                        byte[] bytes = classWriter.toByteArray()

                        //通过文件流写入方式覆盖掉原先的内容，实现class文件的改写。
                        //FileOutputStream outputStream = new FileOutputStream( file.parentFile.absolutePath + File.separator + fileName)
                        FileOutputStream outputStream = new FileOutputStream(file.path)
                        outputStream.write(bytes)
                        outputStream.close()
                    }
                }

                //处理完输入文件后把输出传给下一个文件
                def dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes,
                        directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }

//        transformInputs.each { TransformInput transformInput ->
//
//            transformInput.jarInputs.each { JarInput jarInput ->
//                File dir = jarInput.file
//                if (dir) {
//                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File file ->
//                        System.out.println(" -- jarInputs find class: " + file.name)
//                    }
//                }
//            }
//        }
    }

}

