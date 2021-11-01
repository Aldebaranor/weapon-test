package com.soul.weapon.model;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @Author: XinLai
 * @Date: 2021/10/28 16:38
 */
@Data
public class FireConflictInfo {

    @Display("预判冲突类型")
    private Boolean conflictType;

    @Display("火力冲突事件")
    private Long time;

    @Display("A装备标识")
    private String equipmentIdA;

    @Display("A装备类型标识")
    private String equipmentTypeIdA;

    @Display("A装备型号")
    private String equipmentModeA;

    @Display("A发射方位角")
    private Float launchAzimuthA;

    @Display("A发射俯仰角")
    private Float launchPitchAngleA;


    @Display("B装备标识")
    private String equipmentIdB;

    @Display("B装备类型标识")
    private String equipmentTypeIdB;

    @Display("B装备型号")
    private String equipmentModeB;

    @Display("B发射方位角")
    private Float launchAzimuthB;

    @Display("B发射俯仰角")
    private Float launchPitchAngleB;

}
