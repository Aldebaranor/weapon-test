package com.soul.screen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName AccuracyData
 * @Description 精度数据
 * @Author ShiZuan
 * @Date 2022/8/3 10:15
 * @Version
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccuracyData implements Serializable {

    /**
     * 传感器:1:声呐 2:雷达   发射架:管号
     */
    private String type;
    /**
     * 偏转角精度偏差
     */
    private Double deflectionAngleAccuracy;
    /**
     * 俯仰角精度偏差
     */
    private Double pitchAngleAccuracy;
    /**
     * 距离精度偏差
     */
    private Double distanceAccuracy;
    /**
     * 生成时间
     */
    private Long time;

    public AccuracyData(String type, Double deflectionAngleAccuracy, Double distanceAccuracy, Long time) {
        this.type = type;
        this.deflectionAngleAccuracy = deflectionAngleAccuracy;
        this.distanceAccuracy = distanceAccuracy;
        this.time = time;
    }
}
