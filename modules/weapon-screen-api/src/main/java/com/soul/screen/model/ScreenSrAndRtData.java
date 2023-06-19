package com.soul.screen.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ScreenStatusReportData
 * @Description 装备状态周期报文以及通道反应时间报文
 * @Author ShiZuan
 * @Date 2022/7/20 10:19
 * @Version
 **/

@Data
public class ScreenSrAndRtData implements Serializable {

    /**
     * 目标编号
     */
    private String targetId;


    /**
     * 周期报/通道反应时间
     */
    private List<ScreenUniversalData> reportData;
}
