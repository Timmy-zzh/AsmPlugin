package com.timmy.classloader;

import java.lang.reflect.Method;

public class ClassLoaderUse {

    public static void main(String[] args) {
        System.out.println("111");
        String filePath = "E:/Study/";
        DiskClassLoader diskClassLoader = new DiskClassLoader(filePath);
        try {
            Class<?> aClass = diskClassLoader.loadClass("ClassTest");
            if (aClass != null) {
                Object obj = aClass.newInstance();
                Method method = aClass.getMethod("printTest", null);
                method.invoke(obj, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
