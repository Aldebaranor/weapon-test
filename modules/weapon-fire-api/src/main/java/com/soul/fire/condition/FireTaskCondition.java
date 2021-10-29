package com.soul.fire.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import lombok.Data;

import java.io.Serializable;

@Data
@Condition
public class FireTaskCondition implements Serializable {

    @Display("名称")
    @ConditionOperator(name = "name", operator = ClauseOperator.Like)
    private String name;

    @Display("运行标志")
    @ConditionOperator(name = "running", operator = ClauseOperator.Equal)
    private boolean running;

    @Display("废弃标志")
    @ConditionOperator(name = "disabled", operator = ClauseOperator.Equal)
    private boolean disabled;

}
