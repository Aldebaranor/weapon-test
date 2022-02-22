package com.soul.weapon.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.egova.model.PropertyDescriptor;

/**
 * @author: nash5
 * @date: 2021-11-22 16:48
 */

@Getter
@RequiredArgsConstructor
public enum PipeWeaponIndices implements PropertyDescriptor {
    /** 航空导弹武器测试 **/
    AirMissileRadar(1, "舰空导弹跟踪雷达"),
    AirMissileFireControl(2, "舰空导弹火控系统"),
    AirMissileLauncher(3, "舰空导弹发射系统"),
    AirMissileShortRange(4,  "近程防空导弹"),
    AirMissileMediumRange(5,  "中程防空导弹"),
    AirMissileLongRange(6,  "远程防空导弹"),


    /** 反导舰炮武器测试 **/
    AntiMissileShipGunRadar(12, "反导舰炮跟踪雷达"),
    AntiMissileShipGunControl(13, "反导舰炮火控系统"),
    AntiMissileShipGun(14, "反导舰炮"),

    /** 鱼雷防御武器测试 **/
    Sonar(16, "舰壳声纳"),
    TorpedoFireControl(18, "鱼雷防御武器火控系统"),
    TorpedoLauncher(19, "鱼雷防御武器发射系统"),
    Torpedo(20, "鱼雷防御武器"),

    /** 电子对抗武器测试 **/
    ElectronicDetection(21, "电子侦察设备"),
    ElectronicCountermeasure(22, "电子对抗武器火控系统"),
    MultiUsageLaunch(23, "多功能发射系统"),
    OutBoardElectronicCountermeasure(24, "舷外电子对抗武器"),
    InBoardElectronicCountermeasure(26, "舷内电子对抗武器"),

    /** 水声对抗武器测试 **/
    UnderwaterAcousticCountermeasureControl(28, "水声对抗武器火控系统"),
    UnderwaterAcousticCountermeasure(29, "水声对抗武器");

    @Override
    public String getValue() {
        return value.toString();
    }

    /** 每一个枚举为： <id, 名称> **/
    private final Integer value;
    private final String text;
}
