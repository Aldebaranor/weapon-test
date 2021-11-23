package com.soul.weapon.influxdb;

import lombok.Data;

import java.util.List;

/**
 * 分页查询
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Data
public class PageQuery<T> {

    /**
     * 查询
     */
    private List<Query> queries;

    /**
     * 分页
     */
    private Page<T> paging;

}
