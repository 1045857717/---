package com.springboot.catdemo.studyDemo.aop.transactional;

import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
public class Demo {

    /**
     *  spring事务注解实现原理
     *  1).TransactionInterceptor 下的 invoke方法 执行 invokeWithinTransaction方法，校验合法性，
     *  2).然后创建事务createTransactionIfNecessary，返回事务信息，如果发生异常则completeTransactionAfterThrowing回滚。
     *  3).如果没有异常则执行 commitTransactionAfterReturning 提交事务。
     *  TransactionManager该接口有两个子接口，分别是:编程式事务接口 ReactiveTransactionManager和声明式事务接口 PlatformTransactionManager
     * 主要逻辑放在invokeWithinTransaction()方法中。在该方法中，主要考虑了三类不同的编程方式的事务分别是：
     * 响应式事务（ReactiveTransactionManager）; Mono或Flux实现
     * 回调优先型事务（CallbackPreferringPlatformTransactionManager）;
     * 非回调优先型事务（非CallbackPreferringPlatformTransactionManager）;
     * 1、响应式编程常采用Mono或Flux实现，需要对两种方式选择相应适配器做适配。
     * 2、后两者从名字上可以看出差异，回调型优先的事务，会先执行回调再执行事务。而非回调优先型事务，则关注于事务的执行，至于回调的失败与否不需要影响事务的回滚。
     */
    @Transactional
    public void TestMethod() {

    }
}
