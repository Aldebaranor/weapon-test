package com.soul.weapon.model;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @Author: XinLai
 * @Date: 2021/10/28 15:54
 */
@Data
public class LauncherRotationInfo {

    @Display("发送方编号")
    private String sender;

    @Display("信息发送时间")
    private Long msgTime;

    @Display("发射架调转信息采集时间")
    private Long time;

    @Display("目标标识")
    private String targetId;

    @Display("目标类型标识")
    private String targetTypeId;

    @Display("发射架标识")
    private String launcherId;

    @Display("发射架类型标识")
    private String launcherTypeId;

    @Display("发射架仰角")
    private Float azimuth;

    @Display("发射架旋回角")
    private Float pitchAngle;

}
