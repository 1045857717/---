package com.springboot.catdemo.studyDemo.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁
 */
public class Spinlock {
    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {

        AtomicReference<Thread> sign = new AtomicReference<>();

        final CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i=0;i < 1000;i++) {
            new Thread(() -> {
                // 自旋
                while(!sign.compareAndSet(null, Thread.currentThread())) {
                }
                System.out.println("当前线程是：" + Thread.currentThread());
                System.out.println("当前活跃线程数：" + Thread.activeCount());
                System.out.println("countDownLatch:" + countDownLatch.getCount());
                count++;
                countDownLatch.countDown();
                if (Thread.currentThread() == sign.get()) {
                    sign.compareAndSet(Thread.currentThread(), null);
                }
            },i + " TName").start();
        }

        // 开始时间
        long startTime = System.currentTimeMillis();

        // 等待计数器变0
        countDownLatch.await();
        System.out.println("count:" + count);
        // 结束时间
        long endTime = System.currentTimeMillis();
        System.out.println("自旋锁执行时长：" + (endTime - startTime) + " 毫秒.");

    }
}
