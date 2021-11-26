package com.soul.weapon.entity;

import com.egova.model.annotation.Display;
import lombok.Data;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: nash5
 * @Date: 2021/9/12 15:10
 * 存放历史信息的类，每一种历史信息都可以用如下类存放
 */
public class HistoryInfo implements Serializable {

    @Data
    @Display("舰空导弹武器通道测试-1")
    public static class ShipToAirMissileTestReport implements  Serializable{

        @Display("测试时间")
        private Long time;

        @Display("舰空导弹跟踪雷达标识")
        private String radarId;

        @Display("舰空导弹跟踪雷达自检")
        private Boolean radarSelfCheck;

        @Display("舰空导弹火控系统标识")
        private String fireControlId;

        @Display("舰空导弹火控系统自检")
        private Boolean fireControlSelfCheck;

        @Display("舰空导弹发射系统标识")
        private String launcherId;

        @Display("舰空导弹发射系统自检")
        private Boolean launcherSelfCheck;

        @Display("舰空导弹近程标识")
        private String missileShortId;

        @Display("舰空导弹近程自检")
        private Boolean missileSelfShortCheck;

        @Display("舰空导弹中程标识")
        private String missileMediumId;

        @Display("舰空导弹中程自检")
        private Boolean missileSelfMediumCheck;

        @Display("舰空导弹远程标识")
        private String missileLongId;

        @Display("舰空导弹远程自检")
        private Boolean missileSelfLongCheck;

        @Display("舰空导弹武器通道状态")
        private Boolean status;

    }

    @Data
    @Display("反导舰炮武器通道测试-2")
    public static class AntiMissileShipGunTestReport implements  Serializable{

        @Display("测试时间")
        private Long time;

        @Display("反导舰炮跟踪雷达标识")
        private String radarId;

        @Display("反导舰炮跟踪雷达自检")
        private Boolean radarSelfCheck;

        @Display("反导舰炮火控系统标识")
        private String fireControlId;

        @Display("反导舰炮火控系统自检")
        private Boolean fireControlSelfCheck;

        @Display("反导舰炮标识")
        private String shipGunId;

        @Display("反导舰炮自检")
        private Boolean shipGunSelfCheck;

        @Display("反导舰炮武器通道状态")
        private Boolean status;

    }

    @Data
    @Display("鱼雷防御武器通道测试-3")
    public static class TorpedoTestReport implements  Serializable{

        @Display("测试时间")
        private Long time;

        @Display("声呐标识")
        private String sonarId;

        @Display("声呐自检")
        private Boolean sonarSelfCheck;

        @Display("鱼雷防御武器火控系统标识")
        private String fireControlId;

        @Display("鱼雷防御武器火控系统自检")
        private Boolean fireControlSelfCheck;

        @Display("鱼雷防御武器发射系统标识")
        private String launcherId;

        @Display("鱼雷防御武器发射系统自检")
        private Boolean launcherSelfCheck;

        @Display("鱼雷防御武器标识")
        private String torpedoId;

        @Display("鱼雷防御武器自检")
        private Boolean torpedoSelfCheck;

        @Display("鱼雷防御武器通道状态")
        private Boolean status;

    }

    @Data
    @Display("电子对抗武器通道测试-4")
    public static class ElectronicWeaponTestReport implements  Serializable{

        @Display("测试时间")
        private Long time;

        @Display("电子侦察设备标识")
        private String electronicDetectorId;

        @Display("电子侦察设备自检")
        private Boolean electronicDetectorSelfCheck;

        @Display("电子对抗武器火控系统标识")
        private String fireControlId;

        @Display("电子对抗武器火控系统自检")
        private Boolean fireControlSelfCheck;

        @Display("多功能发射系统标识")
        private String launcherId;

        @Display("多功能发射系统自检")
        private Boolean launcherSelfCheck;

        @Display("舷外电子对抗武器标识")
        private String outerElectronicWeaponId;

        @Display("舷外电子对抗武器自检")
        private Boolean outerElectronicWeaponSelfCheck;

        @Display("舷内电子对抗武器标识")
        private String innerElectronicWeaponId;

        @Display("舷内电子对抗武器自检")
        private Boolean innerElectronicWeaponSelfCheck;

        @Display("电子对抗武器通道状态")
        private Boolean status;

    }

    @Data
    @Display("鱼雷防御武器通道测试-5")
    public static class WaterWeaponTestReport implements  Serializable{

        @Display("测试时间")
        private Long time;

        @Display("声呐标识")
        private String sonarId;

        @Display("声呐自检")
        private Boolean sonarSelfCheck;

        @Display("水声对抗武器火控系统标识")
        private String fireControlId;

        @Display("水声对抗武器火控系统自检")
        private Boolean fireControlSelfCheck;

        @Display("多功能发射系统标识")
        private String launcherId;

        @Display("多功能发射系统自检")
        private Boolean launcherSelfCheck;

        @Display("水声对抗武器标识")
        private String waterWeaponId;

        @Display("水声对抗武器自检")
        private Boolean waterWeaponSelfCheck;

        @Display("水声对抗武器通道状态")
        private Boolean status;

    }

    @Data
    @Display("信息流程测试-6")
    public static class InfoProcessTestReport implements  Serializable{

        @Display("测试时间")
        private Long time;

        @Display("目标标识")
        private String id;

        @Display("目标类型")
        private String type;

        @Display("信息流程状态")
        private Boolean status;

    }

    @Data
    @Display("威胁判断测试-7")
    public static class ThreatenReport implements Serializable{

        @Display("目标标识")
        private String id;

        @Display("目标类型")
        private String type;

        @Display("测试时间")
        private Long time;

        @Display("目标距离判断偏差")
        private Float distanceOffset;

        @Display("目标速率判断偏差")
        private Float speedOffset;

        @Display("目标方位角判断偏差")
        private Float pitchOffset;

    }

    @Data
    @Display("指示处理精度测试-8")
    public static class InstructionAccuracyReport implements Serializable{

        @Display("目标标识")
        private String targetId;

        @Display("目标类型")
        private String targetType;

        @Display("传感器标识")
        private String sensorId;

        @Display("传感器类型")
        private String sensorType;

        @Display("测试时间")
        private Long time;

        @Display("目标距离指示处理精度")
        private Float distanceAccuracy;

        @Display("目标方位角指示处理精度")
        private Float pitchAccuracy;

        @Display("目标俯仰角指示处理精度")
        private Float azimuthAccuracy;

        @Display("目标深度指示处理精度")
        private Float depthAccuracy;
    }


    @Data
    @Display("执行情况测试-9")
    public static class ExecutionStatusReport implements Serializable{

        @Display("目标标识")
        private String targetId;

        @Display("目标类型")
        private String targetType;

        @Display("测试时间")
        private Long time;

        @Display("执行情况状态")
        private Boolean status;

    }

    @Data
    @Display("雷达航迹测试-10")
    public static class RadarPathReport implements Serializable{

        @Display("目标标识")
        private String targetId;

        @Display("目标类型")
        private String targetType;

        @Display("传感器标识")
        private String sensorId;

        @Display("传感器类型")
        private String sensorType;

        @Display("测试时间")
        private Long time;

        @Display("雷达显示目标距离")
        private Float showedTargetDistance;

        @Display("目标距离真值")
        private Float actualTargetDistance;

        @Display("雷达显示目标方位角")
        private Float showedTargetPitch;

        @Display("目标方位角真值")
        private Float actualTargetPitch;

    }

    @Data
    @Display("拦截距离测试-11")
    public static class InterceptDistanceReport implements Serializable{

        @Display("目标标识")
        private String targetId;

        @Display("目标类型")
        private String targetType;

        @Display("测试时间")
        private Long time;

        @Display("目标拦截距离")
        private Float interceptDistance;

    }

    @Data
    @Display("火控解算精度测试-12")
    public static class FireControlReport implements Serializable{

        @Display("目标标识")
        private String targetId;

        @Display("目标类型")
        private String targetType;

        @Display("火控系统标识")
        private String fireControlId;

        @Display("火控系统类型")
        private String fireControlType;

        @Display("测试时间")
        private Long time;

        @Display("目标距离火控解算精度")
        private Float targetDistance;

        @Display("目标方位角火控解算精度")
        private Float targetPitch;

        @Display("目标俯仰角火控解算精度")
        private Float targetAzimuth;

        @Display("目标深度火控解算精度")
        private Float targetDepth;

    }

    @Data
    @Display("反应时间测试-13")
    public static class ReactionReport implements Serializable{

        @Display("目标标识")
        private String targetId;

        @Display("目标类型")
        private String targetType;

        @Display("传感器标识")
        private String sensorId;

        @Display("传感器类型")
        private String sensorType;

        @Display("测试时间")
        private Long time;

        @Display("武器标识")
        private String weaponId;

        @Display("武器类型")
        private String weaponType;

        @Display("反应时间")
        private Long reactionTime;

    }

    @Data
    @Display("发射架调转精度测试-14")
    public static class LauncherRotationReport implements Serializable{

        @Display("目标标识")
        private String targetId;

        @Display("目标类型")
        private String targetType;

        @Display("测试时间")
        private Long time;

        @Display("武器标识")
        private String weaponId;

        @Display("武器类型")
        private String weaponType;

        @Display("发射架方位角调转精度")
        private Float launcherPitchAccuracy;

        @Display("发射架俯仰角调转精度")
        private Float launcherAzimuthAccuracy;

    }

    @Data
    @Display("多目标拦截能力测试-15")
    public static class MultiTargetInterceptionReport implements Serializable{

        @Display("目标标识")
        private String targetId;

        @Display("目标类型")
        private String targetType;

        @Display("测试时间")
        private Long time;

        @Display("目标拦截时间")
        private Float interceptionTime;

        @Display("目标拦截数量")
        private Integer interceptionAccount;

    }

}
