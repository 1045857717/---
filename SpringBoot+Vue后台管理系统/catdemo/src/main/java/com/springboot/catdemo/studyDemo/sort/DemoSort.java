package com.springboot.catdemo.studyDemo.sort;

import java.util.Arrays;

/**
 * @Author: can
 * @Description:
 * @Date: Create in 1:33 2022/4/10
 */
public class DemoSort {


    public static void main(String[] args) throws InterruptedException {
        // 添加1万个随机int数据
        int[] sums = new int[10000];
        for (int i = 0; i < sums.length; i++) {
            sums[i] = (int) (Math.random() * 10000);
        }
        int[] sums2 = new int[10000];
        int[] sums3 = new int[10000];
        // 对于一维数组来说，这种复制属性值传递，修改副本 不会影响原来的值。
        // 对于二维或者一维数组中存放的是对象时，复制结果是：一维的引用变量 传递给 副本的一维数组。修改副本时，会影响原来的数组
        System.arraycopy(sums, 0 , sums2, 0, sums.length);
        System.arraycopy(sums, 0 , sums3, 0, sums.length);
        new Thread(() -> insertionSort(sums)).start();
        new Thread(() -> bubbleSort(sums2)).start();
        new Thread(() -> binarySort(sums3)).start();
        System.out.println(sums == sums2);
        System.out.println(sums == sums3);
        System.out.println(sums3 == sums2);
        Thread.sleep(1000);
        sums2[0] = 100;
        System.out.println("-----------------");
        System.out.println(Arrays.toString(sums));
        System.out.println(Arrays.toString(sums3));
        System.out.println(Arrays.toString(sums2));


    }

    /**
     * 二分查找插入排序
     * 通过将较大的元素都向右移动而不总是交换两个元素
     */
    private static int[] binarySort(int[] sums) {
        // 开始时间
        long startTime = System.currentTimeMillis();
        for (int i = 1; i < sums.length; i++) {
            int temp = sums[i];
            int j;
            for (j = i; j > 0 && sums[j - 1] > temp; j-- ) {
                sums[j] = sums[j - 1];
            }
            sums[j] = temp;
        }
        // 结束时间
        long endTime = System.currentTimeMillis();
        // 计算执行时间
        System.out.println("二分查找插入排序binarySort执行时长：" + (endTime - startTime) + " 毫秒.");
        System.out.println(Arrays.toString(sums));
        return sums;
    }

    /**
     * 冒泡排序
     */
    private static int[] bubbleSort(int[] sums) {
        // 开始时间
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < sums.length; i++) {
            for (int j = i + 1; j < sums.length; j++) {
                if (sums[i] > sums[j]) {
                    int temp = sums[i];
                    sums[i] = sums[j];
                    sums[j] = temp;
                }
            }
        }
        // 结束时间
        long endTime = System.currentTimeMillis();
        // 计算执行时间
        System.out.println("冒泡排序bubbleSort执行时长：" + (endTime - startTime) + " 毫秒.");
        System.out.println(Arrays.toString(sums));
        return sums;
    }


    /**
     * 通过交换进行插入排序，借鉴冒泡排序
     * @param sums
     */
    public static int[] insertionSort(int[] sums) {
        // 开始时间
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < sums.length - 1; i++) {
            for (int j = i + 1; j > 0; j--) {
                if (sums[j] < sums[j - 1]) {
                    int temp = sums[j];
                    sums[j] = sums[j - 1];
                    sums[j - 1] = temp;
                }
            }
        }
        // 结束时间
        long endTime = System.currentTimeMillis();
        // 计算执行时间
        System.out.println("通过交换进行插入排序，借鉴冒泡排序insertionSort执行时长：" + (endTime - startTime) + " 毫秒.");
        System.out.println(Arrays.toString(sums));
        return sums;
    }
}
