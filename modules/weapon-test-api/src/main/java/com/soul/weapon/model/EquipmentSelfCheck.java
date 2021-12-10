package com.soul.weapon.model;


import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Display("装备自检表")
public class EquipmentSelfCheck {

    @Display("跟踪雷达")
    Boolean TrackingRadarState;

    @Display("对空防御指控设备")
    Boolean AirDefenseChargesState;

    @Display("舰空导弹发射装置")
    Boolean ShipSirMissileLaunchState;

    @Display("舰空导弹武器")
    Boolean ShipAirMissileWeaponState;

    @Display("反倒舰炮")
    Boolean InvertedNavalGunState;

    @Display("舰壳声纳")
    Boolean HullSonarState;

    @Display("水下防御指控设备")
    Boolean UnderwaterDefenseChargesState;

/*    @Display("鱼类防御发射装置")
    Boolean TrackingRadarState;

    @Display("鱼类防御武器")
    Boolean TrackingRadarState;

    @Display("多功能发射装置")
    Boolean TrackingRadarState;

    @Display("电子对抗器材")
    Boolean TrackingRadarState;

    @Display("水声对抗器材")
    Boolean TrackingRadarState;*/


}
