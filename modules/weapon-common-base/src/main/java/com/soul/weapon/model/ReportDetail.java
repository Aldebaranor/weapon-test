package com.soul.weapon.model;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @Author: XinLai
 * @Date: 2021/10/29 16:22
 */
@Data
@Display("冲突管控对象信息")
public class ReportDetail {

    @Display("序号")
    private String id;

    @Display("装备标识")
    private String equipmentId;

    @Display("A装备类型标识")
    private String equipmentTypeId;

    @Display("A装备型号")
    private String equipmentMode;

    @Display("A发射方位角")
    private Float launchAzimuth;

    @Display("A发射俯仰角")
    private Float launchPitchAngle;

    @Display("A电子装备计划工作频率")
    private Float minFrequency;

    @Display("A装备最小工作频率")
    private Float electromagneticFrequency;

    @Display("A装备最大工作频率")
    private Float maxFrequency;


}
