package com.soul.fire.algorithm;

import com.soul.weapon.model.ChargeReport;
import com.soul.weapon.model.ConflictReport;
import com.soul.weapon.model.dds.EquipmentStatus;
import com.soul.weapon.model.ScenariosInfo;

/**
 * @Author: XinLai
 * @Date: 2021/11/1 11:03
 */
public interface FireConflictCharge {

    /**
     * 预判作战方案之间是否存在火力冲突
     * @param equipmentStatusA 作战方案A
     * @param equipmentStatusB 作战方案B
     * @return 冲突报告
     */
    ChargeReport chargeReport(EquipmentStatus equipmentStatusA , EquipmentStatus equipmentStatusB);
}
