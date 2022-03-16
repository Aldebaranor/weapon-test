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
    AirMissileRadar(1, "舰载雷达传感器"),
    AirMissileFireControl(2, "舰空导弹火控装置"),
    AirMissileLauncher(3, "舰空导弹发射装置"),
    AirMissileShortRange(4,  "近程防空导弹"),
    AirMissileMediumRange(5,  "中程防空导弹"),
    AirMissileLongRange(6,  "远程防空导弹"),
    AntiShipMissile(7,"反舰导弹"),
    AntiSubmarineMissile(8,"反潜导弹"),
    MainGun(9,"主炮"),
    MainGunTrackingRadar(10,"主炮跟踪雷达"),
    AntiMissileShipGunRadar(11, "反导舰炮跟踪雷达"),
    AntiMissileShipGunControl(12, "反导舰炮火控装置"),
    AntiMissileShipGun(13, "反导舰炮"),
    CarrierAircraft(14, "舰载机"),
    Sonar(15, "舰壳声纳"),
    TowedSonar(16,"拖曳声纳"),
    TorpedoFireControl(17, "鱼雷防御武器火控装置"),
    TorpedoLauncher(18, "鱼雷防御武器发射装置"),
    Torpedo(19, "鱼雷防御武器"),
    ElectronicDetection(20, "电子侦察设备"),
    ElectronicCountermeasure(21, "电子对抗武器火控装置"),
    MultiUsageLaunch(22, "舷外干扰发射系统"),
    OutBoardElectronicCountermeasure(23, "舷外电子对抗武器"),
    OutboardPassiveElectronicJammingBomb(24, "舷外无源电子干扰弹"),
    InBoardElectronicCountermeasure(25, "舷内电子对抗武器"),
    ShipborneElectronicReconnaissanceEquipment(26, "舰载电子侦察设备"),
    UnderwaterAcousticCountermeasureControl(27, "水声对抗武器火控装置"),
    UnderwaterAcousticCountermeasure(28, "水声对抗武器");

    @Override
    public String getValue() {
        return value.toString();
    }

    /** 每一个枚举为： <id, 名称> **/
    private final Integer value;
    private final String text;
}
