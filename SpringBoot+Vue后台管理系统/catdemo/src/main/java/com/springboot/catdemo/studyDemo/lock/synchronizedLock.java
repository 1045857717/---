package com.springboot.catdemo.studyDemo.lock;

import java.util.concurrent.locks.ReentrantLock;

// Synchronized和ReentrantLock的区别
// ①ReentrantLock显示地获得，释放锁，synchronized隐式获得释放锁
//
// ②ReentrantLock可响应中断，可轮回，synchronized是不可以响应中断的
//
// ③ReentrantLock是API级别的，synchronized是JVM级别的
//
// ④ReentrantLock可以实现公平锁
//
// ⑤ReentrantLock通过Condition可以绑定多个条件
//
// ⑥底层实现不一样，synchronized是同步阻塞，使用的是悲观并发策略，lock是同步非阻塞，采用的是乐观并发策略。
//
// ⑦Lock是一个接口，而synchronized是java中的关键字，synchronized是内置的语言实现
//
// ⑧synchronized 在发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生；而 Lock 在发生异常时，如果没有主动通过 unLock()去释放锁，则很可能造成死锁现象， 因此使用 Lock 时需要在 finally 块中释放锁。
public class synchronizedLock {

    private static int count = 0;
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

        for (int j = 0; j < 100000; j++) {
            new Thread(() -> {
                try {
                    lock.lock();
                    count++;
                    System.out.println("当前线程是：" + Thread.currentThread()); // [线程名称, 线程优先级, 线程所属线程组]
                    System.out.println("当前活跃线程数：" + Thread.activeCount());
                } finally {
                    lock.unlock();
                }
            }).start();
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
        System.out.println("ReentrantLock执行时长：" + (endTime - startTime) + " 毫秒.");
    }
}
