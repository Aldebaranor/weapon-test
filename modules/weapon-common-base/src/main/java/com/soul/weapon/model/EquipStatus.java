package com.soul.weapon.model;

import com.egova.model.annotation.Display;
import lombok.Data;

@Data
@Display("监控设备状态")
public class  EquipStatus {

    @Display("设备Id")
    private String id;

    @Display("设备名称")
    private String name;

    @Display("是否开机")
    private Boolean beWork;

    @Display("经度")
    private Double lon;

    @Display("纬度")
    private Double lat;

    @Display("最大工作频率")
    private Double maxFreq;

    @Display("最小工作频率")
    private Double minFreq;
}
