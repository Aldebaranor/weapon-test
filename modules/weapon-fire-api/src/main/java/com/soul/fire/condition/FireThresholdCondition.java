package com.soul.fire.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import lombok.Data;

@Data
@Condition
@Display("阈值表")
public class FireThresholdCondition {

    @Display("id")
    @ConditionOperator(name = "id", operator = ClauseOperator.Equal)
    private String id;

    @Display("type")
    @ConditionOperator(name = "type", operator = ClauseOperator.Equal)
    private String type;

    @Display("disabled")
    @ConditionOperator(name = "disabled", operator = ClauseOperator.Equal)
    private boolean disabled;

}
