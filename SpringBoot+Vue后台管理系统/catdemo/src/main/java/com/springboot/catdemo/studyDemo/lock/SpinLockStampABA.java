package com.springboot.catdemo.studyDemo.lock;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 自旋锁ABA问题
 *
 * 注意这里有Integer缓存问题(-128-187)
 * 解决方法1：根据源码我们可以设置-Djava.lang.Integer.IntegerCache.high来设置缓存最大值
 * 解决方法2：使用方法获取原对象的值，这样就可以保证操作的是同一个对象  signStamp.getReference()  signStamp.getReference()+50
 */
public class SpinLockStampABA {

    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        AtomicStampedReference<Integer> signStamp = new AtomicStampedReference<>(100, 0);
        // 版本号
        int stamp = signStamp.getStamp();

        for (int i=0;i < 500;i++) {
            new Thread(() -> {
                // 自旋
                while(!signStamp.compareAndSet(100, 127, signStamp.getStamp(), signStamp.getStamp() + 1)) {
                }
                System.out.println("当前线程是：" + Thread.currentThread());
                System.out.println("当前期望值：" + signStamp.getReference());
                System.out.println("当前活跃线程数：" + Thread.activeCount());
                System.out.println("当前版本号：" + signStamp.getStamp());
                System.out.println("------------------------------------------------");
                count++;
                if (new Integer(127).equals(signStamp.getReference())) {
                    signStamp.compareAndSet(127, 100, signStamp.getStamp(), signStamp.getStamp() + 1);
                    System.out.println("TName更新后的版本号：" + signStamp.getStamp());
                }
            },i + " TName").start();
        }

        for (int i=0;i < 500;i++) {
            new Thread(() -> {
                // 自旋
                while(!signStamp.compareAndSet(100, 88, signStamp.getStamp(), signStamp.getStamp()+1)) {
                }
                System.out.println("当前线程是：" + Thread.currentThread());
                System.out.println("当前期望值：" + signStamp.getReference());
                System.out.println("当前活跃线程数：" + Thread.activeCount());
                System.out.println("当前版本号：" + signStamp.getStamp());
                System.out.println("------------------------------------------------");
                count++;
                if (new Integer(88).equals(signStamp.getReference())) {
                    signStamp.compareAndSet(88, 100, signStamp.getStamp(), signStamp.getStamp()+1);
                    System.out.println("TName2更新后的版本号：" + signStamp.getStamp());
                }
            },i + " TName2").start();
        }

        // 开始时间
        long startTime = System.currentTimeMillis();

        // 主线程就会一直在此循环里，知道子线程执行完并且Thread.activeCount() 小于2了，就会执行完
        // eclipse或java执行是1，idea用run模式执行是2|debug模式执行是1（以run模式启动输出时，除了main方法的主线程外还有，还多了一个预期外的 Monitor Ctrl-Break 线程）
        // IDEA执行用户代码的时候，实际是通过反射方式去调用，而与此同时会创建一个Monitor Ctrl-Break 用于监控目的。
        while (Thread.activeCount() > 2) {
            // yield:当一个线程使用了这个方法之后，它就会把自己CPU执行的时间让掉，让自己或者其它的线程运行，注意是让自己或者其他线程运行，并不是单纯的让给其他线程
            Thread.yield();
        }
        System.out.println("count:" + count);
        // 结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("AtomicStampedReference自旋锁执行时长：" + (endTime - startTime) + " 毫秒.");

    }
}
