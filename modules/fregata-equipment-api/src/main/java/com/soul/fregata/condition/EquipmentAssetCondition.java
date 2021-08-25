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
@Display("设备资源")
public class EquipmentAssetCondition implements Serializable {

    @Display("设备主键")
    @ConditionOperator(name = "equipmentId", operator = ClauseOperator.Equal)
    private String equipmentId;

}
