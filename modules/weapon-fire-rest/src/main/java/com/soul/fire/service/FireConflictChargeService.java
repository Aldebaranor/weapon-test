package com.soul.fire.service;

import com.soul.weapon.model.ChargeReport;
import com.soul.weapon.model.ConflictReport;
import com.soul.weapon.model.dds.EquipmentStatus;
import com.soul.weapon.model.ScenariosInfo;

/**
 * @Author: XinLai
 * @Date: 2021/11/1 11:03
 */
public interface FireConflictChargeService {

    /**
     * 预判装备之间是否存在火力冲突
     * @param equipmentStatusA 装备状态A
     * @param equipmentStatusB 装备状态B
     * @return 冲突报告
     */
    ChargeReport chargeReport(EquipmentStatus equipmentStatusA , EquipmentStatus equipmentStatusB);

    /**
     * 从Redis中取出所有装备状态进行预判
     */
    void chargeTest();
}
