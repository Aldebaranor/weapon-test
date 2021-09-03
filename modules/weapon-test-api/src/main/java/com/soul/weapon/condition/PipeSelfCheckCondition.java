package com.soul.weapon.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import lombok.Data;

@Data
@Condition
public class PipeSelfCheckCondition {
    @Display("主键")
    @ConditionOperator(name="id", operator = ClauseOperator.Equal)
    private String id;
}
