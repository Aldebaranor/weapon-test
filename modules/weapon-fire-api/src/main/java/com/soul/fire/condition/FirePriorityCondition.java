package com.soul.fire.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import lombok.Data;

import java.io.Serializable;

@Data
@Condition
public class FirePriorityCondition implements Serializable {
    @Display("武器AId")
    @ConditionOperator(name = "weaponAId", operator = ClauseOperator.Equal)
    String weaponAId;

    @Display("武器BId")
    @ConditionOperator(name = "weaponBId", operator = ClauseOperator.Equal)
    String weaponBId;

    @Display("规则序号")
    @ConditionOperator(name = "code", operator = ClauseOperator.Equal)
    private String code;

    @Display("冲突类型")
    @ConditionOperator(name = "ConflictType", operator = ClauseOperator.Equal)
    private String ConflictType;

}
