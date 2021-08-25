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
@Display("我的试验")
public class ExperimentCondition implements Serializable {

    @Display("试验名称")
    @ConditionOperator(name = "name", operator = ClauseOperator.Like)
    private String name;

    @Display("任务类型【字典】")
    @ConditionOperator(name = "taskType", operator = ClauseOperator.Equal)
    private String taskType;

}
