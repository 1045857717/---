package com.springboot.catdemo.studyDemo.polymorphisn;

/**
 * 上下转型
 */
public class UpDownTransformation {

    public static void main(String[] args) {
        System.out.println("----------------------------");
        // 1.执行了parent无参构造
        Parent parent1 = new Parent();
        // 1.执行了parent无参构造
        // 2.执行了child1无参构造
        Parent child1 = new Child1();
        // 1.执行了parent有参构造
        Parent parent2 = new Parent("父亲");
        // 1.执行了parent有参构造
        // 2.执行了child1有参构造
        Parent child2 = new Child1("哥哥");
        // 1.ClassCastException，向下转型异常
//        Child1 child11 = (Child1) new Parent();
        // 1.执行了父类，null正在吃东西
        parent1.eat();
        // 1.执行了子类child1，null正在吃面
        child1.eat();
        // 1.执行了父类，父亲正在吃东西
        parent2.eat();
        // 1.执行了子类child1，哥哥正在吃面
        child2.eat();
        // 向下转型instanceof安全转换
        // 1.哥哥出门了
        Child1 child12 = null;
        if (child2 instanceof Child1) {
            System.out.println("Child1 instanceof Child1 = true");
            child12 = (Child1) child2;
        }
//        child2.out();
        child12.out();
        Parent child13 = null;

        // 1.System.out.println("Child1 instanceof Parent = true");
        if (child2 instanceof Parent) {
            System.out.println("Child1 instanceof Parent = true");
            child13 = child2;
        }

        // 1.System.out.println("parent1 instanceof Child1 = true");
        if (parent1 instanceof Child1) {
            System.out.println("parent1 instanceof Child1 = true");
        } else {
            System.out.println("parent1 instanceof Child1 = false");
        }

        System.out.println("----------------------------");
    }

}

class Parent {

    public String name;

    static {
        System.out.println("执行了parent静态块");
    }

    public Parent() {
        System.out.println("执行了parent无参构造");
    }

    public Parent(String name) {
        this.name = name;
        System.out.println("执行了parent有参构造");
    }

    public void eat() {
        System.out.println("执行了父类，" + name + "正在吃东西");
    }

}

class Child1 extends Parent {

    static {
        System.out.println("执行了Child1静态块");
    }

    public Child1() {
        System.out.println("执行了child1无参构造");
    }

    public Child1(String name) {
        super(name);
        System.out.println("执行了child1有参构造");
    }

    @Override
    public void eat() {
        System.out.println("执行了子类child1，" + name + "正在吃面");
    }

    public void out() {
        System.out.println(name + "出门了");
    }
}

class Child2 extends Parent {

    static {
        System.out.println("执行了Child2静态块");
    }

    public Child2() {
        System.out.println("执行了child2无参构造");
    }

    public Child2(String name) {
        super(name);
        System.out.println("执行了child2有参构造");
    }

    @Override
    public void eat() {
        System.out.println("执行了子类child2，" + name + "正在吃饭");
    }

    public void out() {
        System.out.println(name + "出门了");
    }
}
