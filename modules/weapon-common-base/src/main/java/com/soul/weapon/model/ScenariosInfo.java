package com.soul.weapon.model;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @Author: XinLai
 * @Date: 2021/10/29 16:38
 */
@Data
@Display("作战方案信息实体")
public class ScenariosInfo {

    @Display("装备标识")
    private String equipmentId;

    @Display("装备类型标识")
    private String equipmentTypeId;

    @Display("装备型号")
    private String equipmentMode;

    @Display("计划发射/开机时间")
    private Long beginTime;

    @Display("计划开机时长")
    private Integer duration;

    @Display("计划发射方位角")
    private Float launchAzimuth;

    @Display("计划发射俯仰角")
    private Float launchPitchAngle;

    @Display("电子装备计划工作频率")
    private Float electromagneticFrequency;

    @Display("水声设备计划最小工作频率")
    private Float minHydroacousticFrequency;

    @Display("水声设备计划最大工作频率")
    private Float maxHydroacousticFrequency;

}
