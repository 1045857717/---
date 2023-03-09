package com.springboot.catdemo.cotrolleraop;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import com.springboot.catdemo.CatdemoApplication;
import com.springboot.catdemo.config.MyInterceptorConfig;
import org.apache.poi.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.hutool.extra.spring.SpringUtil.getApplicationContext;

/**
 * 放行的请求url的切面
 *
 * 修饰符匹配（modifier-pattern?）
 * 返回值匹配（ret-type-pattern）可以为*表示任何返回值,全路径的类名等
 * 类路径匹配（declaring-type-pattern?）
 * 方法名匹配（name-pattern）可以指定方法名 或者 *代表所有, set* 代表以set开头的所有方法
 * 参数匹配（(param-pattern)）可以指定具体的参数类型，多个参数间用“,”隔开，各个参数也可以用“*”来表示匹配任意类型的参数，如(String)表示匹配一个String参数的方法；(*,String) 表示匹配有两个参数的方法，第一个参数可以是任意类型，而第二个参数是String类型；可以用(..)表示零个或多个任意参数
 * 异常类型匹配（throws-pattern?）
 * 其中后面跟着“?”的是可选项
 */
@Component
@Aspect
public class ExcludeInterUrlAop {

    private static final Log LOG = Log.get();

    @Pointcut("execution(* com.springboot.catdemo.controller.ExcludeInterUrlController.save(..)) ||" +
            " execution(* com.springboot.catdemo.controller.ExcludeInterUrlController.delete(..)) || " +
            "execution(* com.springboot.catdemo.controller.ExcludeInterUrlController.deleteBatch(..))")
    public void pointcutUpdateExcludeInterURL() {
    }

    /**
     * 切面更新拦截的URL
     */
    @After("pointcutUpdateExcludeInterURL()")
    public void adviceExcludeInterUrlController() {
        // TODO 更新URL拦截器(更新拦截的URL，就重新初始化Bean，最后还是选择发送请求方法重启项目，在刷新Bean)
        // 为了避免不必要的问题和不必要的麻烦，在Spring Boot项目启动过后重新初始化bean并覆盖之前的bean是不可取的。
        // 重新初始化bean可能会导致各种问题，例如线程冲突，内存泄漏等。因此，建议在项目运行期间尽量避免重新初始化bean。
        // 如果必须这样做，则应该非常小心，慎重考虑并测试代码。
        // 单例bean的初始化是在容器初始化时就执行的，容器启动后你再所谓的删除并修改这个bean的bean definition的话只是修改了spring容器底层那个con..map，你其他的依赖的bean指向的堆中的对象并没有变化
        // 拦截器自动配置原理：https://blog.csdn.net/Eaeyson/article/details/124849082
//        String[] beanDefinitionNames = getApplicationContext().getBeanDefinitionNames();
//        ListUtil.list(false, beanDefinitionNames).forEach(System.err::println);
//
//        MyInterceptorConfig interceptorConfig = SpringUtil.getBean(MyInterceptorConfig.class);
//        HandlerMapping resourceHandlerMapping = SpringUtil.getBean(HandlerMapping.class);
//        DispatcherServlet  dispatcherServlet = SpringUtil.getBean(DispatcherServlet.class);
//        interceptorConfig.addInterceptors(new InterceptorRegistry());
//        SpringUtil.unregisterBean("myInterceptorConfig");
//        SpringUtil.unregisterBean("resourceHandlerMapping");
//        SpringUtil.unregisterBean("dispatcherServlet");
//
//        SpringUtil.registerBean("myInterceptorConfig", interceptorConfig);
//        SpringUtil.registerBean("resourceHandlerMapping", resourceHandlerMapping);
//        SpringUtil.registerBean("dispatcherServlet", dispatcherServlet);
//        ListUtil.list(false, beanDefinitionNames).forEach(System.err::println);
    }

}
