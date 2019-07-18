package com.example.casestudy.algorithm.bytedance.topk;

import java.util.List;

/**
 * 快速排序思想实现topK
 *
 * @author wangyong.1991
 * @version 创建时间：2019/7/14 下午4:00
 */
public class QuickSortOrderTopK extends BaseTopK {

    @Override
    public void findTopK(int[] arr, int low, int hi, int k, List<Integer> result) {
        if (low >= hi || k > (hi+1 - low)) {
            return;
        }
        int pivot = arr[low];
        int start = low;
        int end = hi;
        while (start < end) {
            while (start < end && pivot <= arr[end]) {
                end--;
            }
            if (start < end && pivot > arr[end]) {
                arr[start++] = arr[end];
            }
            while (start < end && pivot >= arr[start]) {
                start++;
            }
            if (start < end && pivot < arr[start]) {
                arr[end--] = arr[start];
            }
        }

        arr[start] = pivot;
        int rLen = hi - start;
        if (rLen == k - 1) {
            getItem(arr, start, hi+1, result);
        } else if (rLen > k - 1) {
            findTopK(arr, start+1, hi, k, result);
        } else if (rLen < k - 1) {
            getItem(arr, start, hi+1, result);
            findTopK(arr, low, start-1, k-hi-1+start, result);
        }
    }

    public static void main(String[] args) {
        BaseTopK obj = new QuickSortOrderTopK();
        int[] arr = {1, 3, 4, 5, 5, 0, 5};
        obj.printTopK(arr, 3);
    }
}
