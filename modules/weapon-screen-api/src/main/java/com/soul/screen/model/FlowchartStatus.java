package com.soul.screen.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;

/**
 * @ClassName FlowchartStatus
 * @Description 树状图状态
 * @Author ShiZuan
 * @Date 2022/8/11 16:43
 * @Version
 **/
@Data
public class FlowchartStatus implements Serializable {
    /**
     * 任务Id
     */
    public String targetId;
    /**
     * 传感器状态
     */
    private Boolean sensorStatus;

    /**
     * 传感器类型 水下：1.探雷声呐 2.探潜声呐 空中：1.搜索雷达 2.相控阵雷达
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
     * 任务类型 1.水下方案 2.空中方案
     */
    private Integer missionType;

    /**
     * 作战执行状态
     */
    private Boolean combatExecutionStatus;

    /**
     * 武器类型
     * 执行类型 1.舷外干扰 2.鱼雷防御 3.管装鱼雷 4.助飞鱼雷
     * 发射装置 对应执行类别 1: 1.装置1,2.装置2 等等  2: 1.左舷 2.右舷 等等
     * 武器类型 对应执行类别 1: 1,声干扰 2,声诱饵
     * 例如:1-1-1  1-1-2  等等
     */
    private HashSet<String> weaponType = new HashSet<>();

}
