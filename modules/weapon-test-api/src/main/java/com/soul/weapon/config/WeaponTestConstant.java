package com.soul.weapon.config;

import com.egova.model.PropertyItem;
import com.flagwind.lang.CodeType;
import com.soul.weapon.entity.enums.PipeWeaponIndices;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: nash5
 * @date: 2021-11-11 9:14
 */
public class WeaponTestConstant {

    /** 设备id -> 前端显示名称 from 0 to 19 **/
    public static Map<String, String> MAP_ID_TO_SHOW = Stream.of(
            new SimpleEntry<>(PipeWeaponIndices.AirMissileRadar.getValue(), "舰载雷达传感器"),
            new SimpleEntry<>(PipeWeaponIndices.AirMissileFireControl.getValue(), "舰空导弹火控装置"),
            new SimpleEntry<>(PipeWeaponIndices.AirMissileLauncher.getValue(), "舰空导弹发射装置"),
            new SimpleEntry<>(PipeWeaponIndices.AirMissileShortRange.getValue(), "近程防空导弹"),
            new SimpleEntry<>(PipeWeaponIndices.AirMissileMediumRange.getValue(), "中程防空导弹"),
            new SimpleEntry<>(PipeWeaponIndices.AirMissileLongRange.getValue(), "远程防空导弹"),
            new SimpleEntry<>(PipeWeaponIndices.AntiMissileShipGunRadar.getValue(), "反导舰炮跟踪雷达"),
            new SimpleEntry<>(PipeWeaponIndices.AntiMissileShipGunControl.getValue(), "反导舰炮火控装置"),
            new SimpleEntry<>(PipeWeaponIndices.AntiMissileShipGun.getValue(), "反导舰炮"),
            new SimpleEntry<>(PipeWeaponIndices.Sonar.getValue(), "舰壳声纳"),
            new SimpleEntry<>(PipeWeaponIndices.TowedSonar.getValue(), "拖曳声纳"),
            new SimpleEntry<>(PipeWeaponIndices.TorpedoFireControl.getValue(), "鱼雷防御武器火控装置"),
            new SimpleEntry<>(PipeWeaponIndices.TorpedoLauncher.getValue(), "鱼雷防御武器发射装置"),
            new SimpleEntry<>(PipeWeaponIndices.Torpedo.getValue(), "鱼雷防御武器"),
            new SimpleEntry<>(PipeWeaponIndices.ElectronicDetection.getValue(), "电子侦察设备"),
            new SimpleEntry<>(PipeWeaponIndices.ElectronicCountermeasure.getValue(), "电子对抗武器火控装置"),
            new SimpleEntry<>(PipeWeaponIndices.MultiUsageLaunch.getValue(), "舷外干扰发射系统"),
            new SimpleEntry<>(PipeWeaponIndices.OutBoardElectronicCountermeasure.getValue(), "舷外电子对抗武器"),
            new SimpleEntry<>(PipeWeaponIndices.InBoardElectronicCountermeasure.getValue(), "舷内电子对抗武器"),
            new SimpleEntry<>(PipeWeaponIndices.UnderwaterAcousticCountermeasureControl.getValue(), "水声对抗武器火控装置"),
            new SimpleEntry<>(PipeWeaponIndices.UnderwaterAcousticCountermeasure.getValue(), "水声对抗武器")
            // TODO:
            // 搜索雷达和拖曳声呐单独从dds总线采集，等真实报文，将其存入redis，而后修改getPipeShow即可
    ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    Map<Integer, String> map = Stream.of(new Object[][]{
            {1, "hello"},
            {2, "world"},
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (String) data[1]));

    public static Map<String, List<PropertyItem<Double>>>  WEAPON_THRESHOLD = Stream.of(new Object[][]{
            { "1",Stream.of(
                    new PropertyItem("pipeTest_cycle_threshold","测试周期时间阈值(s)",3D)
                    ).collect(Collectors.toList())},
            { "2",Stream.of(
                    new PropertyItem("pipeTest_cycle_threshold","测试周期时间阈值(s)",3D)
            ).collect(Collectors.toList())},
            { "3",Stream.of(
                    new PropertyItem("pipeTest_cycle_threshold","测试周期时间阈值(s)",3D)
            ).collect(Collectors.toList())},
            { "4",Stream.of(
                    new PropertyItem("pipeTest_cycle_threshold","测试周期时间阈值(s)",3D)
            ).collect(Collectors.toList())},
            { "5",Stream.of(
                    new PropertyItem("pipeTest_cycle_threshold","测试周期时间阈值(s)",3D)
            ).collect(Collectors.toList())},
            { "6",Stream.of(
                    new PropertyItem("progress_time_threshold","信息流程时间阈值(s)",60D)
            ).collect(Collectors.toList())},
            { "7",Stream.of(
                    new PropertyItem("detector_time_threshold","传感器探测时间阈值(s)",3D),
                    new PropertyItem("distanceOffset","目标距离判断偏差阈值(m)",10D),
                    new PropertyItem("speedOffset","目标速率判断偏差阈值(m/s)",50D),
                    new PropertyItem("pitchOffset","目标方位角判断偏差阈值(rad)",0.03D)
            ).collect(Collectors.toList())},
            { "8",Stream.of(
                    new PropertyItem("instruction_time_threshold","指示处理时间阈值(s)",2D),
                    new PropertyItem("distanceAccuracy","目标距离指示处理精度阈值(m)",10D),
                    new PropertyItem("pitchAccuracy","目标方位角指示处理精度阈值(rad)",0.03D),
                    new PropertyItem("azimuthAccuracy","目标俯仰角指示处理精度阈值(rad)",0.03D),
                    new PropertyItem("depthAccuracy","目标深度指示处理精度阈值(m)",5D)
            ).collect(Collectors.toList())},
            { "9",Stream.of(
                    new PropertyItem("execution_time_threshold","执行情况阈值(s)",60D)
            ).collect(Collectors.toList())},
            { "10",Stream.of(
                    new PropertyItem("radar_path_time_threshold","雷达航迹测试时间阈值时间阈值(s)",2D),
                    new PropertyItem("targetDistance","雷达显示目标距离偏差阈值(m)",10D),
                    new PropertyItem("targetPitch","雷达显示目标方位角阈值(rad)",0.03D)
            ).collect(Collectors.toList())},
            { "11",Stream.of(
                    new PropertyItem("air_interception_distance","对空目标最小拦截距离阈值阈值(m)",300D),
                    new PropertyItem("water_interception_distance","水下目标最小拦截距离阈值(m)",200D)
            ).collect(Collectors.toList())},
            { "12",Stream.of(
                    new PropertyItem("fire_control_time_threshold","火控解算时间阈值(s)",2D),
                    new PropertyItem("targetDistance","目标距离火控解算精度阈值(m)",10D),
                    new PropertyItem("targetPitch","目标方位角火控解算精度阈值(rad)",0.03D),
                    new PropertyItem("targetAzimuth","目标俯仰角火控解算精度阈值(rad)",0.03D),
                    new PropertyItem("targetDepth","目标深度火控解算精度阈值(m)",5D)
            ).collect(Collectors.toList())},
            { "13",Stream.of(
                    new PropertyItem("airReactionTime","对空防御作战反应时间阈值(s)",20D),
                    new PropertyItem("waterReactionTime","水下防御作战反应时间阈值(s)",30D)
            ).collect(Collectors.toList())},
            { "14",Stream.of(
                    new PropertyItem("launcher_rotation_time_threshold","发射架调转时间阈值(s)",5D),
                    new PropertyItem("targetDistance","发射架方位角调转精度阈值(rad)",0.03D),
                    new PropertyItem("targetPitch","发射架俯仰角调转精度阈值(rad)",0.03D)
            ).collect(Collectors.toList())},
            { "15",Stream.of(
                    new PropertyItem("min_interception_distance","最小有效拦截距离(m)",50D)
            ).collect(Collectors.toList())}
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (List<PropertyItem<Double>>) data[1]));

}
