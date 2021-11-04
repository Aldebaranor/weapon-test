package com.soul.weapon.model.dds;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @Author: XinLai
 * @Date: 2021/10/28 15:43
 * 武器发射使用信息报文
 */
@Data
@Display("武器发射使用信息报文")
public class EquipmentLaunchStatus {
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

    @Display("武器发射时间")
    private Long time;

    @Display("目标标识")
    private String targetId;

    @Display("目标类型标识")
    private String targetTypeId;

    @Display("发射方位角")
    private Float launchAzimuth;

    @Display("发射俯仰角")
    private Float launchPitchAngle;
}
