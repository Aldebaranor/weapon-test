package com.soul.weapon.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import lombok.Data;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@Data
@Condition
public class ShipToAirMissileTestReportCondition {

    @Display("主键")
    @ConditionOperator(name="id", operator = ClauseOperator.Equal)
    private String id;

    @Display("外键-任务")
    @ConditionOperator(name = "taskId",operator = ClauseOperator.Equal)
    private String taskId;


}
