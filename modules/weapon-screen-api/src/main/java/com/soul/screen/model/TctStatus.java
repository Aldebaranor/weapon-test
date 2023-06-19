package com.soul.screen.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TctStatus
 * @Description 任务通道环节状态
 * @Author ShiZuan
 * @Date 2022/7/21 10:15
 * @Version
 **/
@Data
public class TctStatus implements Serializable {
    /**
     * 类型
     */
    private String type;
    /**
     * 武器名称
     */
    private String name;
    /**
     * 时间
     */
    private Long time;
    /**
     * 距离
     */
    private Double distance;

}
