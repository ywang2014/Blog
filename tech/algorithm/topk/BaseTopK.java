package com.example.casestudy.algorithm.bytedance.topk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangyong.1991
 * @version 创建时间：2019/7/14 下午4:02
 */
public abstract class BaseTopK implements TopK {
    /**
     * 逆序打印数组
     *
     */
    private void printArray(Object[] arr) {
        Arrays.sort(arr);
        for (int i = arr.length - 1; i >= 0; i--) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    private void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public void getItem(int[] arr, int start, int end, List<Integer> result) {
        for (int i = start; i < end; i++) {
            result.add(arr[i]);
        }
    }

    public void printTopK(int[] arr, int k) {
        List<Integer> result = new ArrayList<>();
        findTopK(arr, 0, arr.length, k, result);
        printArray(arr);
        printArray(result.toArray());
    }

    @Override
    public abstract void findTopK(int[] arr, int low, int hi, int k, List<Integer> result);
}
