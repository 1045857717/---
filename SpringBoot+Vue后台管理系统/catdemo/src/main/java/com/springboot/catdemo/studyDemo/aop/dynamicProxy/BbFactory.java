package com.springboot.catdemo.studyDemo.aop.dynamicProxy;

/**
 * 真实对象b
 * 真正提供服务的类
 * 静态代理模式的缺陷，每次需求扩展需求时，就要新增不同的服务类，代理对象也要实现不同的方法。这样不利于扩展，如果将来需要修改原来的需求，那么原来的接口以及实现都要修改；不利于维护
 */
public class BbFactory implements WomanToolsFactory {
    @Override
    public void saleWomanTools(float length) {
        System.out.println("根据您的需求，为您定制了一个高度为：" + length + "的情趣用品");
    }
}
