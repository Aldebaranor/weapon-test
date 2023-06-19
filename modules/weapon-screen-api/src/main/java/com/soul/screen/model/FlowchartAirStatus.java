package com.soul.screen.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;

/**
 * @ClassName FlowchartAirStatus
 * @Description 树状图-空中
 * @Author: Song
 * @Date 2023/6/19 15:37
 *
 */
@Data
public class FlowchartAirStatus implements Serializable {
    /**
     * 任务Id
     */
    public String targetId;
    /**
     * 传感器状态
     */
    private Boolean sensorStatus;

    /**
     * 传感器类型 1.搜索雷达 2.相控阵雷达
     */
    private Integer sensorType;

    /**
     * 指控设备状态
     */
    private Boolean chargeDeviceStatus;

    /**
     * 态势类型 1.态势生成
     */
    private Integer situationType;

    /**
     * 空中类型 1.空中方案
     */
    private Integer airType;

    /**
     * 作战执行状态
     */
    private Boolean combatExecutionStatus;

    /**
     * 武器类型
     * 执行类型 1. 2. 3. 4.
     * 发射装置 对应执行类别 1: 1.装置1,2.装置2 等等  2: 1. 2. 等等
     * 武器类型 对应执行类别 1: 1,声干扰 2,声诱饵
     * 例如:1-1-1  1-1-2  等等
     */
    private HashSet<String> weaponType = new HashSet<>();


}
