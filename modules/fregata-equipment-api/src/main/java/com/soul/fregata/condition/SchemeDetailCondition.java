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
@Display("装备方案与装备关联表")
public class SchemeDetailCondition implements Serializable {

    @Display("方案ID")
    @ConditionOperator(name = "schemeId", operator = ClauseOperator.Equal)
    private String schemeId;

    @Display("方案搭载的设备ID")
    @ConditionOperator(name = "equipmentId", operator = ClauseOperator.Equal)
    private String equipmentId;

    @Display("是否禁用")
    @ConditionOperator(name = "disabled", operator = ClauseOperator.Equal)
    private Boolean disabled;

}
