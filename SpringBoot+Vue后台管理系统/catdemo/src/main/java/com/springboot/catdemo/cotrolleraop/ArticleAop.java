package com.springboot.catdemo.cotrolleraop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 文章切面
 */
@Component
@Aspect
public class ArticleAop {


    @Pointcut("execution(* com.springboot.catdemo.controller.ArticleController.findArticleDetail(..))")
    public void pointcutFindArticleDetail() {
    }

    @After("pointcutFindArticleDetail()")
    public void adviceFindArticleDetailController(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        System.err.println("aop After---log");
    }

    // e:为什么这里的参数是ProceedingJoinPoint，而不是JoinPoint？
    // a:因为ProceedingJoinPoint是JoinPoint的子类，而且它多了一个proceed()方法，这个方法就是用来执行目标方法的。
    // 也就是说，当我们在环绕通知中调用proceed()方法时，目标方法才会被执行。
    // 如果不调用proceed()方法，目标方法就不会被执行。
    // e:为什么要执行目标方法？
    // a:因为我们的环绕通知是在目标方法执行前后都执行的，如果不执行目标方法，那么目标方法就不会执行了。
    // e:为什么要在目标方法执行前后都执行环绕通知？
    // a:因为我们的环绕通知是用来记录日志的，如果只在目标方法执行前执行环绕通知，那么日志就只记录了目标方法执行前的日志，
    // 而没有记录目标方法执行后的日志。


    // e:有了拦截器还要AOP干什么
    // a:拦截器是基于java的反射机制的，而AOP是基于动态代理实现的，两者的实现原理不同，所以两者的功能也不同。
    // 拦截器只能对controller层的请求起作用，而AOP可以对service层的方法起作用。
    // e:SpringAOP也是动态代理实现的


    public void around(ProceedingJoinPoint pjp) {
        System.err.println("aop around---before---log");
        try {
            pjp.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        System.err.println("aop around---after---log");
    }
}
