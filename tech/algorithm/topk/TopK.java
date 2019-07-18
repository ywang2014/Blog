package com.example.casestudy.algorithm.bytedance.topk;

import java.util.List;

/**
 * @author wangyong.1991
 * @version 创建时间：2019/7/14 下午3:57
 */
public interface TopK {
    /**
     *
     * @param arr 元数据
     * @param low 起点
     * @param hi 终点
     * @param k 前k个元素
     * @param result 结果
     */
    void findTopK(int[] arr, int low, int hi, int k, List<Integer> result);
}
