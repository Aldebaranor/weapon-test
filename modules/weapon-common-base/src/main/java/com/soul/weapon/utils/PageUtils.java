package com.soul.weapon.utils;


import com.soul.weapon.influxdb.Page;

/**
 * 分页工具类
 *
 * @author 奔波儿灞
 * @version 1.0.0
 */
public final class PageUtils {

    private PageUtils() {
        throw new IllegalStateException("Utils");
    }

    public static boolean enablePage(Page<?> page) {
        // 为null或者index小于等于0，则不分页
        if (page == null) {
            return false;
        }
        return page.getPageIndex() >= 1;
    }

    public static <T> Page<T> copy(Page<T> old) {
        if (old == null) {
            return new Page<>();
        }
        return new Page<>(old.getPageIndex(), old.getPageSize());
    }

}
