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
@Display("装备方案表")
public class SchemeCondition implements Serializable {

    @Display("方案名称")
    @ConditionOperator(name = "name", operator = ClauseOperator.Like)
    private String name;

    @Display("方案编号")
    @ConditionOperator(name = "code", operator = ClauseOperator.Equal)
    private String code;

    @Display("是否禁用")
    @ConditionOperator(name = "disabled", operator = ClauseOperator.Equal)
    private Boolean disabled;

    @Display("是否默认")
    @ConditionOperator(name = "beDefault", operator = ClauseOperator.Equal)
    private Boolean beDefault;

    @Display("设备")
    @ConditionOperator(name = "equipmentId", operator = ClauseOperator.Equal)
    private String equipmentId;

}
