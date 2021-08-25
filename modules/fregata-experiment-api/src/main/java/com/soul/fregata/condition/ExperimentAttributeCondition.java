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
@Display("试验属性")
public class ExperimentAttributeCondition implements Serializable {

    @Display("试验ID")
    @ConditionOperator(name = "experimentId", operator = ClauseOperator.Equal)
    private String experimentId;

    @Display("兵力ID")
    @ConditionOperator(name = "armyId", operator = ClauseOperator.Equal)
    private String armyId;


}
