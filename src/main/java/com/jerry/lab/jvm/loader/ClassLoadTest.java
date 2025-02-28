package com.jerry.lab.jvm.loader;

import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * 使用自定义加载器，实现类的自定义加载和热部署
 */
public class ClassLoadTest {
    public static void main(String[] args) throws Exception {
        /** old class loader **/
        ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
        System.out.println("step1 : 当前加载器是 " + oldCl + "(父加载器是 " + oldCl.getParent() + ")");
        try {
            System.out.println("step2 : 开始加载 class com.jerry.lab.Test");
            Class clazz1 = oldCl.loadClass("com.jerry.lab.Test");
        } catch (ClassNotFoundException e) {
            System.out.println("当前加载器无法找到 com.jerry.lab.Test");
        }

        /** my class loader **/
        ClassLoader myCL = new MyClassLoader();
        System.out.println("step3 : 自定义新加载器 " + myCL + "(父加载器是 " + myCL.getParent() + ")，其修改了加载路径 ");

        Class clazz2 = null;
        try {
            System.out.println("step4 : 开始加载 class com.jerry.lab.Test");
            clazz2 = myCL.loadClass("com.jerry.lab.Test");
        } catch (ClassNotFoundException e) {
            System.out.println("当前加载器无法找到 com.jerry.lab.Test");
        }

        Object obj2 = clazz2.newInstance();
        Method helloMethod2 = clazz2.getDeclaredMethod("hello", null);
        helloMethod2.invoke(obj2, null);

        /** unload **/
        /**
         *  JVM中的Class只有满足以下三个条件，才能被GC回收：
         *    - 该类所有的实例都已经被GC。
         *    - 加载该类的ClassLoader实例已经被GC。
         *    - 该类的java.lang.Class对象没有在任何地方被引用。
         */
        myCL = null;
        clazz2 = null;
        obj2 = null;
        helloMethod2 = null;
        System.gc();
        System.out.println("卸载通过自定义加载器加载的类");

        /** reload **/
        Thread.sleep(20 * 1000);
        System.out.println("手动修改class文件");
        ClassLoader newCL = new MyClassLoader();

        Class clazz3 = null;
        try {
            System.out.println("step5 : 重新加载 class com.jerry.lab.Test");
            clazz3 = newCL.loadClass("com.jerry.lab.Test");
        } catch (ClassNotFoundException e) {
            System.out.println("当前加载器无法找到 com.jerry.lab.Test");
        }

        Object obj3 = clazz3.newInstance();
        Method helloMethod3 = clazz3.getDeclaredMethod("hello", null);
        helloMethod3.invoke(obj3, null);
    }
}

class MyClassLoader extends ClassLoader {
    private String classPath = "files\\jvm";

    private byte[] loadByte(String name) throws Exception {
        name = name.replaceAll("\\.", "/");
        FileInputStream fis = new FileInputStream(classPath + "/" + name
                + ".class");
        int len = fis.available();
        byte[] data = new byte[len];
        fis.read(data);
        fis.close();
        return data;

    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] data = loadByte(name);
            return defineClass(name, data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }

}
