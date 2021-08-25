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
@Display("试验环境")
public class ExperimentEnvironmentCondition implements Serializable {

    @Display("试验ID")
    @ConditionOperator(name = "experimentId", operator = ClauseOperator.Equal)
    private String experimentId;

}
