package com.springboot.catdemo.studyDemo.aop.dynamicProxy;


import com.springboot.catdemo.studyDemo.aop.staticproxy.BbFactory;
import com.springboot.catdemo.studyDemo.aop.staticproxy.WomanToolsFactory;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;

/**
 * 静态代理
 * 访问者
 * 代理模式需要满足三个条件：服务提供者的行为(抽象对象)，真实的服务提供者（真实对象），代理对象（服务的搬运工，不生产服务）
 *
 * 静态代模式存在的问题：违反了开闭原则（扩展能力差，可维护性差）,简单来说就是如果我修改抽象接口的需求，那么它的实现类就也要跟着修改，就严重影响了代码的维护性。
 * 6个设计模式原则
 * 单一职责模式：一个类或者一个接口只负责唯一项职责，尽量设计出功能单一的接口；
 * 依赖倒转原则：高层模块不应该依赖低层模块具体实现，解耦高层与低层。即面向接口编程，当实现发生变化时，只需提供新的实现类，不需要修改高层模块代码；
 * 开放-封闭原则(最重要的原则)：程序对外扩展开放，对修改关闭；换句话说，就是当需求发生变化时，我们可以通过添加新模块来满足需求，而不是通过修改原来的实现代码来满足新需求；
 */
public class ZhangSanMain {

    public static void main(String[] args) {
//        // 1.从前有个日本公司生产情趣用品，质量不错
//        ManToolsFactory factory = new AaFactory();
//        // 2.lison老师代理这个公司的产品
//        Lison lison = new Lison(factory);
//        // 3.张三有需求来找我，买了一个18 size的情趣用品
//        lison.saleManTools("18");

        // 1.日本有个A公司生产男性用品
        ManToolsFactory aFactory = new AaFactory();
        // 2.日本有个B公司生产女性用品
        WomanToolsFactory bFactory = new BbFactory();
        // 3.lison成立了一家代购公司
        LisonCompany lisonCompany = new LisonCompany();
        // 4.张三来找我，说要我代购男性用品
        lisonCompany.setFactory(aFactory);
        // 5.一号员工对这个行业很熟悉，我委派一号员工为他服务。
        ManToolsFactory manToolsFactory = (ManToolsFactory) lisonCompany.getProxyInstance();
        // 6.一号员工为他服务，完成代购。
        manToolsFactory.saleManTools("18");
        System.out.println("-------------------------------");

        // 7.张三老婆来找我，说她要代购女性用品
        lisonCompany.setFactory(bFactory);
        // 8.二号员工对这个行业熟悉，于是我委派二号员工为他服务；
        WomanToolsFactory womanToolsFactory = (WomanToolsFactory) lisonCompany.getProxyInstance();
        // 9.二号员工为他服务，完成代购。
        womanToolsFactory.saleWomanTools(1.8f);

        System.out.println("============================");
        ManToolsFactory aaFactory = lisonCompany.callHandler(new AaFactory());
        aaFactory.saleManTools("123");
    }


}
