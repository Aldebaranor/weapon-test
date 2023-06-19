package com.soul.screen.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ScreenDetectorAccuracyData
 * @Description 探测器精度
 * @Author ShiZuan
 * @Date 2022/7/22 16:41
 * @Version
 **/
@Data
public class ScreenAccuracyData implements Serializable {
    /**
     * 目标批号
     */
    private String targetId;

    /**
     * 精度数据
     */
    private List<AccuracyData> accuracyData;
}
