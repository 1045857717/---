//package com.springboot.catdemo.studyDemo.aop.springaop;
//
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
///**
// * 申明一个@Aspect注释类，并且定义成一个bean交给Spring管理。
// * execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)
// * 这里问号表示当前项可以有也可以没有，其中各项的语义如下
// * modifiers-pattern：方法的可见性，如public，protected；
// * ret-type-pattern：方法的返回值类型，如int，void等；
// * declaring-type-pattern：方法所在类的全路径名，如com.spring.Aspect；
// * name-pattern：方法名名称，如buisinessService()；
// * param-pattern：方法的参数类型，如java.lang.String；
// * throws-pattern：方法抛出的异常类型，如java.lang.Exception；
// * example:
// * @Pointcut("execution(* com.chenss.dao.*.*(..))")//匹配com.chenss.dao包下的任意接口和类的任意方法
// * @Pointcut("execution(public * com.chenss.dao.*.*(..))")//匹配com.chenss.dao包下的任意接口和类的public方法
// * @Pointcut("execution(public * com.chenss.dao.*.*())")//匹配com.chenss.dao包下的任意接口和类的public 无方法参数的方法
// * @Pointcut("execution(* com.chenss.dao.*.*(java.lang.String, ..))")//匹配com.chenss.dao包下的任意接口和类的第一个参数为String类型的方法
// * @Pointcut("execution(* com.chenss.dao.*.*(java.lang.String))")//匹配com.chenss.dao包下的任意接口和类的只有一个参数，且参数为String类型的方法
// * @Pointcut("execution(* com.chenss.dao.*.*(java.lang.String))")//匹配com.chenss.dao包下的任意接口和类的只有一个参数，且参数为String类型的方法
// * @Pointcut("execution(public * *(..))")//匹配任意的public方法
// * @Pointcut("execution(* te*(..))")//匹配任意的以te开头的方法
// * @Pointcut("execution(* com.chenss.dao.IndexDao.*(..))")//匹配com.chenss.dao.IndexDao接口中任意的方法
// * @Pointcut("execution(* com.chenss.dao..*.*(..))")//匹配com.chenss.dao包及其子包中任意的方法
// *
// * 关于这个表达式的详细写法,可以脑补也可以参考官网很容易的,可以作为一个看spring官网文档的入门,打破你害怕看官方文档的心理,其实你会发觉官方文档也是很容易的
// * https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#aop-pointcuts-examples
// */
//@Component
//@Aspect
//public class MyAspectJ {
//
//    /**
//     * 切入点表达式由@Pointcut注释表示。切入点声明由两部分组成:一个签名包含名称和任何参数，以及一个切入点表达式，该表达式确定我们对哪个方法执行感兴趣
//     * 申明切入点，匹配UserDao所有方法调用
//     * execution：匹配方法执行连接点
//     * within：将匹配限制为特定类型中的连接点
//     * args：参数
//     * target：目标对象 （服指向接口和子类）
//     * this：代理对象 (类) .this JDK代理时，指向接口和代理类proxy，cglib代理时 指向接口和子类(不使用proxy) 此处需要注意的是，如果配置设置proxyTargetClass=false，或默认为false，则是用JDK代理，否则使用的是CGLIB代理
//     */
//    @Pointcut("execution(* com.springboot.catdemo.controller.UserController.login())")
//    public void pointcutUserLogin() {
//
//    }
//
//    @Pointcut("execution(* com.springboot.catdemo.controller.ArticleController.findArticleDetail(java.lang.Integer))")
//    public void pointcutArticle() {
//
//    }
//
//    /**
//     * 申明before通知,在pintCut切入点前执行
//     * 通知与切入点表达式相关联，
//     * 并在切入点匹配的方法执行之前、之后或前后运行。
//     * 切入点表达式可以是对指定切入点的简单引用，也可以是在适当位置声明的切入点表达式。
//     */
//    @Before("pointcutUserLogin()")
//    public void adviceUserLogin() {
//        System.out.println("Aop Before-----------UserLogin-----------log");
//    }
//
//    /**
//     * 申明After通知,在pintCut切入点执行后执行
//     */
//    @After("pointcutArticle()")
//    public void adviceArticle() {
//        System.out.println("Aop After-----------Article-----------log");
//    }
//}
