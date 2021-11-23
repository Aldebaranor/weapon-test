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
    AirMissileRadar(0, "舰空导弹武器跟踪雷达"),
    AirMissileFireControl(1, "舰空导弹武器火控系统"),
    AirMissileLauncher(2, "舰空导弹武器发射系统"),
    AirMissileShortRange(3,  "舰空导弹武器近程"),
    AirMissileMediumRange(4,  "舰空导弹武器中程"),
    AirMissileLongRange(5,  "舰空导弹武器远程"),

    /** 反导舰炮武器测试 **/
    AntiMissileShipGunRadar(6, "反导舰炮武器跟踪雷达"),
    AntiMissileShipGunControl(7, "反导舰炮武器火控系统"),
    AntiMissileShipGun(8, "反导舰炮武器"),

    /** 鱼雷防御武器测试 **/
    Sonar(9, "声呐"),
    TorpedoFireControl(10, "鱼雷防御武器火控系统"),
    TorpedoLauncher(11, "鱼雷防御武器发射系统"),
    Torpedo(12, "鱼雷防御武器"),

    /** 电子对抗武器测试 **/
    ElectronicDetection(13, "电子侦察设备"),
    ElectronicCountermeasure(14, "电子对抗武器火控系统"),
    MultiUsageLaunch(15, "多功能发射系统"),
    OutBoardElectronicCountermeasure(16, "舷外电子对抗武器"),
    InBoardElectronicCountermeasure(17, "舷内电子对抗武器"),

    /** 水声对抗武器测试 **/
    UnderwaterAcousticCountermeasureControl(18, "水声对抗武器火控系统"),
    UnderwaterAcousticCountermeasure(19, "水声对抗武器");

    @Override
    public String getValue() {
        return value.toString();
    }

    /** 每一个枚举为： <id, 名称> **/
    private final Integer value;
    private final String text;
}
