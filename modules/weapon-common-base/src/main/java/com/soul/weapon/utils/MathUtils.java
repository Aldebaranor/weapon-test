package com.soul.weapon.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author: nash5
 * @date: 2021-11-22 20:46
 */
public class MathUtils {

    /**
     * 根据 Collections 查找最大值
     * @param arr 待查询数组
     * @return 最大值
     */
    public static long findMaxByCollections(long[] arr) {
        return Arrays.stream(arr).max().getAsLong();
    }

    /**
     * 根据 Collections 查找最大值
     * @param arr 待查询数组
     * @return 最大值
     */
    public static long findMinByCollections(long[] arr) {
        return Arrays.stream(arr).min().getAsLong();
    }
}
