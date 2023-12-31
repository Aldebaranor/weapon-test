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
public class PipeTestCondition {

    @Display("主键")
    @ConditionOperator(name="id", operator = ClauseOperator.Equal)
    private String id;

    @Display("外键-任务id")
    @ConditionOperator(name="taskId", operator = ClauseOperator.Equal)
    private String taskId;

    @Display("类型")
    @ConditionOperator(name="type", operator = ClauseOperator.Equal)
    private String type;

    @Display("运行状态, 0: 未执行, 1: 执行中, 2: 执行完")
    @ConditionOperator(name="status", operator = ClauseOperator.Equal)
    private String status;
}
