package com.springboot.catdemo.studyDemo.aop.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理对象
 * 包含真实的对象，为真实对象的服务进行增强，和真实对象继承于同一个接口
 * Lison只是服务的搬运工，不生产服务
 * 动态代理
 * 有新的需求，代理对象一行代码都不需要改，只需要增加新的抽象接口（服务提供者行为）和真实对象（真正提供服务的类）
 */
public class LisonCompany implements InvocationHandler {

    // 被包含的真实对象
    public Object factory;

    public Object getFactory() {
        return factory;
    }

    public void setFactory(Object factory) {
        this.factory = factory;
    }

    // 通过proxy获取动态代理的对象（调度员工）
    // this代表 LisonCompany
    public Object getProxyInstance() {
        return Proxy.newProxyInstance(factory.getClass().getClassLoader(), factory.getClass().getInterfaces(), this);
    }

    /**
     * 通过动态代理对象对方法进行增强
     * @param proxy 代理类
     * @param method 拦截的方法
     * @param args 方法参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        doSomeThingBefore();
        Object ret = method.invoke(factory, args);
        doSomeThingEnd();
        return ret;
    }

    // 售前服务
    private void doSomeThingBefore() {
        System.out.println("根据您的需要，进行市场的调研和产品分析！");
    }

    // 售后服务
    private void doSomeThingEnd() {
        System.out.println("精美包装，快递一条龙服务！");
    }

    public <T> T callHandler(T factory) {
        LisonCompany lisonCompany = new LisonCompany();
        lisonCompany.setFactory(factory);
        T proxyInstance = (T) lisonCompany.getProxyInstance();
        return proxyInstance;
    }

}
