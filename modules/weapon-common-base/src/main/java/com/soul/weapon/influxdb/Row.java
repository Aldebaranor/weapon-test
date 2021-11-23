package com.soul.weapon.influxdb;

import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Map;

/**
 * @Auther: 码头工人
 * @Date: 2021/09/18/2:49 下午
 * @Description:
 */
public class Row extends LinkedCaseInsensitiveMap<Object> {
    public Row() {
    }

    public Row(Map<? extends String, ?> map) {
        this.putAll(map);
    }
}
