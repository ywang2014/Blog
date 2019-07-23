package com.example.casestudy.algorithm.bytedance.topk;

import java.util.List;

/**
 * 堆排序思想实现topK
 *
 * @author wangyong.1991
 * @version 创建时间：2019/7/16 下午5:58
 */
public class HeapSortTopK extends BaseTopK {
    @Override
    public void findTopK(int[] arr, int low, int hi, int k, List<Integer> result) {
        if (arr == null || low < 0 || hi > arr.length || low >= hi || k > hi - low) {
            throw new IllegalArgumentException("Arguments error.");
        }

        int[] heap = new int[k];
        for (int i = 0; i < k; i++) {
            heap[i] = arr[start+i];
        }
        buildHead(heap);

        for (int i = start+k; i < end; i++) {
            if (arr[i] > heap[0]) {
                heap[0] = arr[i];
                downloadFilter(heap, 0);
            }
        }

        for (int i = 0; i < k; i++) {
            result.add(heap[i]);
        }
    }

    private void buildHead(int[] arr) {
        for (int i = arr.length/2; i >= 0; i--) {
            downloadFilter(arr, i);
        }
    }

    private void downloadFilter(int[] arr, int index) {
        int lChild = 2*index + 1;
        int rChild = 2*index + 2;
        if (lChild >= arr.length) {
            return;
        }

        if (rChild >= arr.length) {
            if (lChild < arr.length && arr[lChild] < arr[index]) {
                swap(arr, lChild, index);
                downloadFilter(arr, lChild);
            }
        }

        int child = lChild;
        if (arr[lChild] > arr[rChild]) {
            // 取更小的一个
            child = rChild;
        }

        if (child < arr.length && arr[child] < arr[index]) {
            swap(arr, child, index);
            downloadFilter(arr, child);
        }
    }

    private void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    public static void main(String[] args) {
        HeapSortTopK topK = new HeapSortTopK();
        int[] arr = {1, 2, 3, 4, 5, 8, 5, 4, 6};
        topK.findTopK(arr, 0, arr.length, 3);
    }
}
