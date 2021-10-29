package com.soul.weapon.model;

import com.egova.model.annotation.Display;

/**
 * @Author: XinLai
 * @Date: 2021/10/28 15:32
 */
public class EquipmentStatus {

    @Display("发送方编号")
    private String sender;

    @Display("信息发送时间")
    private Long msgTime;

    @Display("装备标识")
    private String equipmentId;

    @Display("装备类型标识")
    private String equipmentTypeId;

    @Display("装备型号")
    private String equipmentMode;

    @Display("信息采集时间")
    private Long time;

    @Display("开关机状态")
    private Boolean beWork;

    @Display("发射方位角")
    private Float launchAzimuth;

    @Display("发射俯仰角")
    private Float launchPitchAngle;

    @Display("最小工作频率")
    private Float MinFrequency;

    @Display("最大工作频率")
    private Float MaxFrequency;
}
