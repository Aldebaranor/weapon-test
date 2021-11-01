package com.soul.weapon.model;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @Author: XinLai
 * @Date: 2021/10/28 15:51
 */
@Data
public class TargetFireControlInfo {
    @Display("发送方编号")
    private String sender;

    @Display("信息发送时间")
    private Long msgTime;

    @Display("火控系统输出诸元时间")
    private Long time;

    @Display("目标标识")
    private String targetId;

    @Display("目标类型标识")
    private String targetTypeId;

    @Display("火控系统标识")
    private String fireControlSystemId;

    @Display("火控系统类型标识")
    private String fireControlSystemTypeId;

    @Display("火控系统输出诸元对应目标距离")
    private Float distance;

    @Display("火控系统输出诸元对应目标速率")
    private Float speed;

    @Display("火控系统输出诸元对应目标俯仰角")
    private Float azimuth;

    @Display("火控系统输出诸元对应目标方位角")
    private Float pitchAngle;

    @Display("火控系统输出诸元对应目标深度")
    private Float depth;
}
