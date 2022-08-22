package com.soul.screen.model;

import com.egova.model.PropertyItem;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ScreenCountData
 * @Description 大屏计数数据
 * @Author ShiZuan
 * @Date 2022/7/19 10:35
 * @Version
 **/
@Data
public class ScreenUniversalData implements Serializable {

    /**
     * 名称
     */
    private String name;
    /**
     * 类型编号
     */
    private String type;
    /**
     * 文本
     */
    private String text;
    /**
     * 值
     */
    private Object value;
    /**
     * 平均值
     */
    private Double ave;

}
