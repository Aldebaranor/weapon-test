package com.soul.model;

import com.egova.model.annotation.Display;

/**
 * @Author: XinLai
 * @Date: 2021/10/28 15:50
 */
public class TargetInstructionsInfo {
    @Display("发送方编号")
    private String sender;

    @Display("信息发送时间")
    private Long msgTime;

    @Display("传感器探测到目标时间")
    private Long time;

    @Display("目标标识")
    private String targetId;

    @Display("目标类型标识")
    private String targetTypeId;

    @Display("传感器标识")
    private String equipmentId;

    @Display("传感器类型标识")
    private String equipmentTypeId;

    @Display("探测目标距离")
    private Float distance;

    @Display("探测目标速率")
    private Float speed;

    @Display("探测目标俯仰角")
    private Float azimuth;

    @Display("探测目标方位角")
    private Float pitchAngle;

    @Display("探测目标深度")
    private Float depth;
}
