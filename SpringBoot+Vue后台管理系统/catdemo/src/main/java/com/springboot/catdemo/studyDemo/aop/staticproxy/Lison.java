package com.springboot.catdemo.studyDemo.aop.staticproxy;

/**
 * 代理对象
 * 包含真实的对象，为真实对象的服务进行增强，和真实对象继承于同一个接口
 * Lison只是服务的搬运工，不生产服务
 */
public class Lison implements ManToolsFactory{

    // 被包含的真实对象
    public ManToolsFactory factory;

    public Lison(ManToolsFactory factory) {
        super();
        this.factory = factory;
    }

    @Override
    public void saleManTools(String size) {
        doSomeThingBefore();
        factory.saleManTools(size);
        doSomeThingEnd();
    }

    // 售前服务
    private void doSomeThingBefore() {
        System.out.println("根据您的需要，进行市场的调研和产品分析！");
    }

    // 售后服务
    private void doSomeThingEnd() {
        System.out.println("精美包装，快递一条龙服务！");
    }


}
