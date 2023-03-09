package com.springboot.catdemo.studyDemo.annotaion;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.Arrays;

/**
 * 自定义注解
 * Java注解又称Java标注，是JDK5.0版本开始支持加入源代码的特殊语法元数据
 *
 * Java语言中的类、方法、变量、参数和包等都可以被标注。和Javadoc不同，Java标注可以通过反射获取标注内容。
 * 在编译器生成类文件时，标注可以被嵌入到字节码中。Java虚拟机可以保留标注内容，在运行时可以获取到标注内容。 当然它也支持自定义Java标注。
 * 标准的元注解：
 * @Target 专门用来限定某个自定义注解能够被应用在哪些Java元素上面的，标明作用范围；取值在java.lang.annotation.ElementType 进行定义的。
 * @Retention 持久力、保持力。即用来修饰自定义注解的生命周期{Java源文件阶段|编译到class文件阶段|运行期阶段}
 * @Documented 指定自定义注解是否能随着被定义的java文件生成到JavaDoc文档当中
 * @Inherited 是指定某个自定义注解如果写在了父类的声明部分，那么子类的声明部分也能自动拥有该注解,只对那些@Target被定义为 ElementType.TYPE 的自定义注解起作用
 * 在工作中会经常使用到的 @Autowired 注解 此注解中使用到了@Target、@Retention、@Documented 这三个元注解
 *
 */
public class MetaAnnotation {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        // 获取类的class对象的三种方式
//        Class<?> clazz = Class.forName("com.springboot.catdemo.studyDemo.annotaion.Demo");
//        Class clazz = demo.getClass();
        Class<Demo> clazz = Demo.class;
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getMethods();
        Constructor<Demo> cons = clazz.getConstructor(new Class[]{});

        parseMethod(clazz);
        parseType(clazz);

//        // 获取Class注解
//        if (clazz.isAnnotationPresent(TestAnnotation.class)) {
//            //获取TestAnnotation注解
//            TestAnnotation testAnnotation = clazz.getAnnotation(TestAnnotation.class);
//            System.out.println("testAnnotation.name= " + testAnnotation.value()); // class类注解
//        } else {
//            System.out.println("Demo上没有配置获取Class注解TestAnnotation注解");
//        }
//
//        // 获取构造方法注解
//        if (cons.isAnnotationPresent(TestAnnotation.class)) {
//            //获取TestAnnotation注解
//            TestAnnotation testAnnotation = cons.getAnnotation(TestAnnotation.class);
//            System.out.println("testAnnotation.name= " + testAnnotation.value());
//        } else {
//            System.out.println("Demo上没有配置构造方法注解TestAnnotation注解");
//        }
//
//        // 获取字段注解
//        for (Field field : fields) {
//            //判断Class对象字段属性上是否有TestAnnotation的注解
//            if (field.isAnnotationPresent(TestAnnotation.class)) {
//                //获取TestAnnotation注解
//                TestAnnotation testAnnotation = field.getAnnotation(TestAnnotation.class);
//                System.out.println("testAnnotation.name= " + testAnnotation.value()); // 字段注解
//            } else {
//                System.out.println("Demo上没有配置字段注解TestAnnotation注解");
//            }
//        }
//
//        // 获取方法注解
//        Method method = clazz.getMethod("MyMethodAnnotation");
//        //判断Class对象字段属性上是否有TestAnnotation的注解
//            if (method.isAnnotationPresent(TestAnnotation.class)) {
//                //获取TestAnnotation注解
//                TestAnnotation testAnnotation = method.getAnnotation(TestAnnotation.class);
//                System.out.println("testAnnotation.name= " + testAnnotation.value());
//            } else {
//                System.out.println("Demo上没有配置方法注解TestAnnotation注解");
//            }
        }

    /**
     * 解析方法注解
     * @param <T>
     * @param clazz
     */
    public static <T> void parseMethod(Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            for (Method method : clazz.getDeclaredMethods()) {
                TestAnnotation methodAnnotation = method.getAnnotation(TestAnnotation.class);
                if (methodAnnotation!=null) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    System.out.println(Arrays.toString(method.getParameters()));
                    if (parameterTypes.length > 0) {
                        method.invoke(obj, methodAnnotation.value(), methodAnnotation.classType());
                    } else {
                        //通过反射调用带有此注解的方法
                        method.invoke(obj);
                    }
                }
                TestAnnotation yts = (TestAnnotation) method.getAnnotation(TestAnnotation.class);
                if (yts != null) {
                    if (TestAnnotation.YtsType.util.equals(yts.classType())) {
                        System.out.println("this is a util method");
                    } else {
                        System.out.println("this is a other method");
                    }
                    System.out.println("yts.value() = " + yts.value());
                }
                System.out.println("\t\t-----------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析类注解
     * @param <T>
     * @param clazz
     */
    public static <T> void parseType(Class<T> clazz) {
        try {
            TestAnnotation yts = (TestAnnotation) clazz.getAnnotation(TestAnnotation.class);
            if (yts != null) {
                if (TestAnnotation.YtsType.util.equals(yts.classType())) {
                    System.out.println("this is a util class");
                } else {
                    System.out.println("this is a other class");
                }
            }
            TestAnnotation classAnnotation = (TestAnnotation) clazz.getAnnotation(TestAnnotation.class);
            if (classAnnotation != null) {
                System.err.println(" class info: "+classAnnotation.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * @Target注解，是专门用来限定某个自定义注解能够被应用在哪些Java元素上面的，标明作用范围；取值在java.lang.annotation.ElementType 进行定义的
 * TYPE：类，接口（包括注解类型）或枚举的声明
 * FIELD：属性的声明
 * METHOD：方法的声明
 * PARAMETER：方法形式参数声明
 * CONSTRUCTOR：构造方法的声明
 * LOCAL_VARIABLE：局部变量声明
 * ANNOTATION_TYPE：注解类型声明
 * PACKAGE：包的声明
 * JDK1.8 枚举TYPE多了两个 ELementType.TYPE_PARAMETER，ELementType.TYPE_USE
 * TYPE_PARAMETER：该注解能写在类型变量的声明语句中(如:泛型声明)。
 * TYPE_USE：该注解能写在使用类型的任何语句中。
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
/**
 * RetentionPolicy。SOURCE：（注解将被编译器忽略掉）
 * 如果被定义为 RetentionPolicy.SOURCE，则它将被限定在Java源文件中，那么这个注解即不会参与编译也不会在运行期起任何作用，这个注解就和一个注释是一样的效果，只能被阅读Java文件的人看到；
 *
 * RetentionPolicy.CLASS：（注解将被编译器记录在class文件中，但在运行时不会被虚拟机保留，这是一个默认的行为）
 * 如果被定义为 RetentionPolicy.CLASS，则它将被编译到Class文件中，那么编译器可以在编译时根据注解做一些处理动作，但是运行时JVM（Java虚拟机）会忽略它，并且在运行期也不能读取到；
 *
 * RetentionPolicy.RUNTIME：（注解将被编译器记录在class文件中，而且在运行时会被虚拟机保留，因此它们能通过反射被读取到）
 * 如果被定义为 RetentionPolicy.RUNTIME，那么这个注解可以在运行期的加载阶段被加载到Class对象中。那么在程序运行阶段，可以通过反射得到这个注解，并通过判断是否有这个注解或这个注解中属性的值，从而执行不同的程序代码段。
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@interface TestAnnotation {
    public String value() default "";

    // 定义枚举
    public enum YtsType {
        util, entity, service, model
    }

    // 设置默认值
    public YtsType classType() default YtsType.util;
}


/**
 * 测试类
 */
@TestAnnotation(value = "class类注解", classType = TestAnnotation.YtsType.entity)
class Demo {

    @TestAnnotation(value = "字段注解")
    public String name;

    @TestAnnotation(value = "构造器注解")
    public Demo() {
    }
    @TestAnnotation(value = "方法无参注解")
    public void MyMethodAnnotation() {
        System.out.println("执行方法注解 name = " + name);
    }

    @TestAnnotation(value = "方法有参注解")
    public void MyMethodAnnotationParam(String name, TestAnnotation.YtsType age) {
        System.out.println("执行方法注解 name = " + name + " age=" + age);
    }

}
