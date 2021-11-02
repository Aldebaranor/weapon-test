package com.soul.weapon.model;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @Author: XinLai
 * @Date: 2021/10/28 15:58
 */
@Data
@Display("海洋战场环境信息")
public class EnvironmentInfo {
    @Display("发送方编号")
    private String sender;

    @Display("信息发送时间")
    private Long msgTime;

    @Display("信息采集时间")
    private Long time;

    @Display("经度")
    private Float longitude;

    @Display("纬度")
    private Float latitude;
}
