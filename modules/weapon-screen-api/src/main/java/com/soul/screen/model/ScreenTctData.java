package com.soul.screen.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ScreenTctData
 * @Description 任务通道状态
 * @Author ShiZuan
 * @Date 2022/7/21 10:07
 * @Version
 **/
@Data
public class ScreenTctData implements Serializable {
    /**
     * 目标批号
     */
    private String targetId;

    /**
     * 状态集合
     */
    private List<TctStatus> tctStatusList;
}
