package com.soul.weapon.model;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @ClassName FireStatus
 * @Description TODO
 * @Author ShiZuan
 * @Date 2023/8/1 17:14
 * @Version
 **/
@Data
@Display("火力兼容状态")
public class FireStatus {
    @Display("设备Id")
    private String id;

    @Display("发射时间")
    private Long time;

    @Display("方位角")
    private Double launchAzimuth;

    @Display("俯仰角")
    private Double launchPitchAngle;

    @Display("发射装置")
    private String name;
}
