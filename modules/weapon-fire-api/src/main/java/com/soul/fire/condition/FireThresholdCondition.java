package com.soul.fire.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import lombok.Data;

import java.io.Serializable;

@Data
@Condition
@Display("阈值表")
public class FireThresholdCondition implements Serializable {

    @Display("id")
    @ConditionOperator(name = "id", operator = ClauseOperator.Equal)
    private String id;

    @Display("type")
    @ConditionOperator(name = "type", operator = ClauseOperator.Equal)
    private String type;

    @Display("disabled")
    @ConditionOperator(name = "disabled", operator = ClauseOperator.Equal)
    private Boolean disabled;

}
