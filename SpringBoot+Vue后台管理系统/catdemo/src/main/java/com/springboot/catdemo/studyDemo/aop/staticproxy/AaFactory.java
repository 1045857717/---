package com.springboot.catdemo.studyDemo.aop.staticproxy;


/**
 * 真实对象
 * 真正提供服务的类
 */
public class AaFactory implements ManToolsFactory{
    @Override
    public void saleManTools(String size) {
        System.out.println("根据您的需求，为您定制了一个size为：" + size + "的情趣用品");
    }
}
