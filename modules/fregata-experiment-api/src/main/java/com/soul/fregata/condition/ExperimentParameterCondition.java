package com.soul.fregata.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import lombok.Data;

import java.io.Serializable;

/**
 * created by yangL
 */
@Data
@Condition
@Display("试验参数")
public class ExperimentParameterCondition {

    @Display("试验ID")
    @ConditionOperator(name = "experimentId", operator = ClauseOperator.Equal)
    private String experimentId;

}
