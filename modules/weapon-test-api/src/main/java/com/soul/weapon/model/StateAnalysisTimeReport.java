package com.soul.weapon.model;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @Author: XinLai
 * @Date: 2021/12/14 14:59
 */
@Data
@Display("状态评估时间节点报文")
public class StateAnalysisTimeReport {

    @Display("来袭目标id")
    private String id;

    @Display("探测获取目标信息时间")
    private Long instructionTime;

    @Display("火控系统输出诸元信息时间")
    private Long fireControlTime;

    @Display("发射架调转时间")
    private Long launcherRotationTime;

    @Display("武器发射时间")
    private Long fireTime;

}
