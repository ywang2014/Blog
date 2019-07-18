package com.example.casestudy.algorithm.bytedance.topk;

import java.util.Arrays;
import java.util.List;

/**
 * 直接排序输出
 *
 * @author wangyong.1991
 * @version 创建时间：2019/7/16 下午6:01
 */
public class SortTopK extends BaseTopK {
    @Override
    public void findTopK(int[] arr, int low, int hi, int k, List<Integer> result) {
        Arrays.sort(arr);
        int index = arr.length - 1;
        while (k-- > 0) {
            result.add(arr[index--]);
        }
    }

    public static void main(String[] args) {
        BaseTopK obj = new SortTopK();
        int[] arr = {1, 2, 3, 4, 8, 5, 6, 7};
        int k = 2;
        obj.printTopK(arr, 2);
    }
}
