package com.soul.weapon.config;

import com.soul.weapon.entity.enums.PipeWeaponIndices;

import java.util.AbstractMap.SimpleEntry;
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
            new SimpleEntry<>(PipeWeaponIndices.AirMissileRadar.getValue(), "跟踪雷达"),
            new SimpleEntry<>(PipeWeaponIndices.AirMissileFireControl.getValue(), "对空防御指控设备"),
            new SimpleEntry<>(PipeWeaponIndices.AirMissileLauncher.getValue(), "航空导弹发射装置"),
            new SimpleEntry<>(PipeWeaponIndices.AirMissileShortRange.getValue(), "航空导弹武器"),
            new SimpleEntry<>(PipeWeaponIndices.AirMissileMediumRange.getValue(), "航空导弹武器"),
            new SimpleEntry<>(PipeWeaponIndices.AirMissileLongRange.getValue(), "航空导弹武器"),
            new SimpleEntry<>(PipeWeaponIndices.AntiMissileShipGunRadar.getValue(), "跟踪雷达"),
            new SimpleEntry<>(PipeWeaponIndices.AntiMissileShipGunControl.getValue(), "对空防御指挥设备"),
            new SimpleEntry<>(PipeWeaponIndices.AntiMissileShipGun.getValue(), "反导舰炮"),
            new SimpleEntry<>(PipeWeaponIndices.Sonar.getValue(), "舰壳声呐"),
            new SimpleEntry<>(PipeWeaponIndices.TorpedoFireControl.getValue(), "水下防御指控设备"),
            new SimpleEntry<>(PipeWeaponIndices.TorpedoLauncher.getValue(), "鱼雷房屋武器发射装备"),
            new SimpleEntry<>(PipeWeaponIndices.Torpedo.getValue(), "鱼雷防御武器"),
            // new SimpleEntry<>(PipeWeaponIndices.ElectronicDetection.getValue(), ""), 不参与显示
            new SimpleEntry<>(PipeWeaponIndices.ElectronicCountermeasure.getValue(), "对空防御指控设备"),
            new SimpleEntry<>(PipeWeaponIndices.MultiUsageLaunch.getValue(), "多功能发射装置"),
            new SimpleEntry<>(PipeWeaponIndices.OutBoardElectronicCountermeasure.getValue(), "电子对抗器材"),
            new SimpleEntry<>(PipeWeaponIndices.InBoardElectronicCountermeasure.getValue(), "电子对抗器材"),
            new SimpleEntry<>(PipeWeaponIndices.UnderwaterAcousticCountermeasureControl.getValue(), "水下防御指控设备"),
            new SimpleEntry<>(PipeWeaponIndices.UnderwaterAcousticCountermeasure.getValue(), "水声对抗器材")
            // TODO:
            // 搜索雷达和拖曳声呐单独从dds总线采集，等真实报文，将其存入redis，而后修改getPipeShow即可
    ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
}
