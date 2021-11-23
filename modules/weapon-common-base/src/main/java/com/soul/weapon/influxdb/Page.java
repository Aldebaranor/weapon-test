package com.soul.weapon.influxdb;

import lombok.Data;

import java.util.List;

/**
 * 分页
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Data
public class Page<T> {

    public static final Integer DEFAULT_PAGE_INDEX = 1;
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    private Integer pageIndex;

    private Integer pageSize;

    private Long totalCount;

    private List<T> data;

    public Page() {
        this.pageIndex = DEFAULT_PAGE_INDEX;
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.totalCount = 0L;
    }

    public Page(Integer pageIndex, Integer pageSize) {
        if (pageIndex <= 0) {
            pageIndex = DEFAULT_PAGE_INDEX;
        }
        this.pageIndex = pageIndex;
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        this.pageSize = pageSize;
    }

    public Integer getOffset() {
        return (pageIndex - 1) * pageSize;
    }

}
