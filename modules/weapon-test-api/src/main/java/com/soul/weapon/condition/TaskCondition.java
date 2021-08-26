package com.soul.weapon.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import lombok.Data;

/**
 * created by nash5
 */
@Data
@Condition
public class TaskCondition {
    @Display("主键")
    @ConditionOperator(name="id", operator = ClauseOperator.Equal)
    private String id;

    @Display("任务名称")
    @ConditionOperator(name="taskTitle", operator = ClauseOperator.Equal)
    private String name;

    @Display("描述")
    @ConditionOperator(name="taskDescription", operator = ClauseOperator.Equal)
    private String taskDescription;

    @Display("创建人")
    @ConditionOperator(name="owner", operator = ClauseOperator.Equal)
    private String owner;

}
