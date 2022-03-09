package com.soul.weapon.model;

import com.egova.model.annotation.Display;
import lombok.Data;

/**
 * @ClassName StatusAnalysisEvaluation
 * @Description 状态评估分析实体
 * @Author ShiZuan
 * @Date 2022/3/8 16:34
 * @Version
 **/
@Data
@Display("状态评估分析")
public class StatusAnalysisEvaluation {

    @Display("目标标识")
    private String targetId;

    @Display("传感器名称")
    private String sensorName;

    @Display("传感器探测时间")
    private Long sensorDetectionTime;

    @Display("火控装置名称")
    private String fireControlName;

    @Display("火控装置输出诸元时间")
    private Long fireControlTime;

    @Display("发射架名称")
    private String launchPadName;

    @Display("发射架调转时间")
    private Long launcherTurnaroundTime;

    @Display("武器名称")
    private String weaponName;

    @Display("武器发射时间")
    private Long weaponLaunchTime;

    public StatusAnalysisEvaluation(String targetId) {
        this.targetId = targetId;
    }

    public StatusAnalysisEvaluation(String targetId, String sensorName, Long sensorDetectionTime) {
        this.targetId = targetId;
        this.sensorName = sensorName;
        this.sensorDetectionTime = sensorDetectionTime;
    }

    public StatusAnalysisEvaluation(String targetId, String sensorName, Long sensorDetectionTime, String fireControlName, Long fireControlTime) {
        this.targetId = targetId;
        this.sensorName = sensorName;
        this.sensorDetectionTime = sensorDetectionTime;
        this.fireControlName = fireControlName;
        this.fireControlTime = fireControlTime;
    }

    public StatusAnalysisEvaluation(String targetId, String sensorName, Long sensorDetectionTime, String fireControlName, Long fireControlTime, String launchPadName, Long launcherTurnaroundTime) {
        this.targetId = targetId;
        this.sensorName = sensorName;
        this.sensorDetectionTime = sensorDetectionTime;
        this.fireControlName = fireControlName;
        this.fireControlTime = fireControlTime;
        this.launchPadName = launchPadName;
        this.launcherTurnaroundTime = launcherTurnaroundTime;
    }

    public StatusAnalysisEvaluation(String targetId, String sensorName, Long sensorDetectionTime, String fireControlName, Long fireControlTime, String launchPadName, Long launcherTurnaroundTime, String weaponName, Long weaponLaunchTime) {
        this.targetId = targetId;
        this.sensorName = sensorName;
        this.sensorDetectionTime = sensorDetectionTime;
        this.fireControlName = fireControlName;
        this.fireControlTime = fireControlTime;
        this.launchPadName = launchPadName;
        this.launcherTurnaroundTime = launcherTurnaroundTime;
        this.weaponName = weaponName;
        this.weaponLaunchTime = weaponLaunchTime;
    }
}
