package com.timmy.classloader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiskClassLoader extends ClassLoader {

    private String filePath;

    public DiskClassLoader(String path) {
        this.filePath = path;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        String classPath = filePath + className + ".class";
        System.out.println(classPath);
        File classFile = new File(classPath);
        if (classFile.exists()){
            System.out.println("file exitst");
        }else{
            System.out.println("file not exist");
        }
        byte[] classBytes = null;
        try {
            Path path = Paths.get(new URI(classPath));
            classBytes = Files.readAllBytes(path);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return defineClass(className, classBytes, 0, classBytes.length);
    }
}
