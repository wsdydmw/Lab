package com.jerry.lab.jvm.loader;

public class CodeBlockTest {
    public static void main(String[] args) {
        System.out.println("------run new SubClass()------");
        new SubClass();
        System.out.println("------run new SubClass()------");
        new SubClass();
    }
}

class Parent {
    public static String p_StaticField = "父类--静态变量";

    //父类的静态初始化块
    static {
        System.out.println(p_StaticField);
        System.out.println("父类--静态初始化块");
    }

    public String p_Field = "父类--变量";

    //父类的初始化块
    {
        System.out.println(p_Field);
        System.out.println("父类--初始化块");
    }
    //父类的构造函数

    public Parent() {
        System.out.println("父类--构造器");
    }
}

class SubClass extends Parent {
    public static String s_StaticField = "子类--静态变量";

    // 子类的静态初始化块
    static {
        System.out.println(s_StaticField);
        System.out.println("子类--静态初始化块");
    }

    public String s_Field = "子类--变量";

    //子类的初始化块
    {
        System.out.println(s_Field);
        System.out.println("子类--初始化块");
    }

    //子类的构造函数
    public SubClass() {
        System.out.println("子类--构造器");
    }
}
