package com.soul.fregata.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import lombok.Data;

import java.io.Serializable;

/**
 * created by 迷途小码农
 */
@Data
@Condition
@Display("某个装备可搭载其他装备的关联关系表")
public class EquipmentRelationCondition implements Serializable {

    @Display("平台分类")
    @ConditionOperator(name = "categoryType", operator = ClauseOperator.Equal)
    private String categoryType;

    @Display("设备主键")
    @ConditionOperator(name = "equipmentId", operator = ClauseOperator.Equal)
    private String equipmentId;

    @Display("可搭载设备主键")
    @ConditionOperator(name = "carryEquipmentId", operator = ClauseOperator.Equal)
    private String carryEquipmentId;

}
