package com.soul.weapon.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import lombok.Data;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Data
@Condition
public class PipeHistoryCondition {
    @Display("主键")
    @ConditionOperator(name="id", operator = ClauseOperator.Equal)
    private String id;

    @Display("外键-任务")
    @ConditionOperator(name = "taskId",operator = ClauseOperator.Equal)
    private String taskId;

    @Display("外键-测试项类型")
    @ConditionOperator(name = "type",operator = ClauseOperator.Equal)
    private String type;

    @Display("外键-测试项")
    @ConditionOperator(name = "pipeTestId",operator = ClauseOperator.Equal)
    private String pipeTestId;
}
