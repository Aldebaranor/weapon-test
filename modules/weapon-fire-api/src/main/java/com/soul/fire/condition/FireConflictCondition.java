package com.soul.fire.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import com.flagwind.persistent.model.SingleClause;
import lombok.Data;

@Data
@Condition
@Display("冲突表")
public class FireConflictCondition {

    @Display("id")
    @ConditionOperator(name = "id", operator = ClauseOperator.Equal)
    private String id;

    @Display("ConflictType")
    @ConditionOperator(name = "ConflictType", operator = ClauseOperator.Equal)
    private String conflictType;

    @Display("taskId")
    @ConditionOperator(name = "taskId", operator = ClauseOperator.Equal)
    private String taskId;

    @Display("disabled")
    @ConditionOperator(name = "disabled", operator = ClauseOperator.Equal)
    private Boolean disabled;



}
