package com.soul.weapon.model.dds;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @Author: XinLai
 * @Date: 2021/10/28 15:46
 * 目标真值信息报文
 */
@Data
@Display("目标真值信息")
public class TargetInfo {
    @Display("发送方编号")
    private String sender;

    @Display("信息发送时间")
    private Long msgTime;

    @Display("信息采集时间")
    private Long time;

    @Display("目标标识")
    private String targetId;

    @Display("目标类型标识")
    private String targetTypeId;

    @Display("目标距离真值")
    private Float distance;

    @Display("目标速率真值")
    private Float speed;

    @Display("目标俯仰角真值")
    private Float azimuth;

    @Display("目标方位角真值")
    private Float pitchAngle;

    @Display("目标深度真值")
    private Float depth;

}
