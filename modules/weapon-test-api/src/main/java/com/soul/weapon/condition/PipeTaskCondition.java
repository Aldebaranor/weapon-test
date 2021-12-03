package com.soul.weapon.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import com.soul.weapon.entity.codes.PipeState;
import lombok.Data;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@Data
@Condition
public class PipeTaskCondition {
    @Display("主键")
    @ConditionOperator(name="id", operator = ClauseOperator.Equal)
    private String id;

    @Display("运行状态, 0: 未执行, 1: 执行中, 2: 执行完")
    @ConditionOperator(name = "status",operator = ClauseOperator.Equal)
    private PipeState state;

    @Display("任务名称")
    @ConditionOperator(name="name", operator = ClauseOperator.Equal)
    private String name;
}
