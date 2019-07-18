package com.example.casestudy.algorithm.bytedance.topk;

import java.util.List;

/**
 * 快速排序思想实现topK
 *      逆序实现快速排序
 *
 * @author wangyong.1991
 * @version 创建时间：2019/7/14 下午4:10
 */
public class InverseOrderTopK extends BaseTopK {
    @Override
    public void findTopK(int[] arr, int low, int hi, int k, List<Integer> result) {
        if (low >= hi || k > (hi+1 - low)) {
            return;
        }
        int pivot = arr[low];
        int start = low;
        int end = hi-1;
        // 逆序处理
        while (start < end) {
            while (start < end && pivot >= arr[end]) {
                end--;
            }
            if (start < end && pivot < arr[end]) {
                arr[start++] = arr[end];
            }
            while (start < end && pivot <= arr[start]) {
                start++;
            }
            if (start < end && pivot > arr[start]) {
                arr[end--] = arr[start];
            }
        }

        arr[start] = pivot;
        int lLen = start - low + 1;
        if (lLen == k) {
            getItem(arr, low, start+1, result);
        } else if (lLen > k) {
            findTopK(arr, low, start, k, result);
        } else if (lLen < k) {
            getItem(arr, low, start+1, result);
            findTopK(arr, start+1, hi, k-lLen, result);
        }
    }

    public static void main(String[] args) {
        BaseTopK obj = new InverseOrderTopK();
        int[] arr = {1, 3, 4, 5, 5, 0, 5};
        obj.printTopK(arr, 3);

        int[] arr2 = {6, 1, 4, 5, 5, 0, 5};
        obj.printTopK(arr2, 3);

        int[] arr3 = {5, 6, 7, 1, 2, 0, 5};
        obj.printTopK(arr3, 3);
    }
}
