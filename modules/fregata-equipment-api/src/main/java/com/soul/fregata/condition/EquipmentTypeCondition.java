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
@Display("装备大小类表")
public class EquipmentTypeCondition implements Serializable {

    @Display("编码")
    @ConditionOperator(name = "code", operator = ClauseOperator.Equal)
    private String code;

    @Display("名称")
    @ConditionOperator(name = "name", operator = ClauseOperator.Like)
    private String name;

    @Display("等级")
    @ConditionOperator(name = "grade", operator = ClauseOperator.Equal)
    private Integer grade;

    @Display("上级ID")
    @ConditionOperator(name = "parentId", operator = ClauseOperator.Equal)
    private String parentId;

    @Display("是否禁用")
    @ConditionOperator(name = "disabled", operator = ClauseOperator.Equal)
    private Boolean disabled;

}
