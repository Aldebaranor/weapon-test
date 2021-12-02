package com.soul.weapon.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import lombok.Data;

/**
 * @Author: dxq
 * @Date: 2021/12/2 15:35
 */
@Data
@Condition
public class ComDictionaryItemCondition {
    @Display("主键")
    @ConditionOperator(name="id", operator = ClauseOperator.Equal)
    private String id;

    @Display("字体值")
    @ConditionOperator(name="value", operator = ClauseOperator.Equal)
    private String value;
}
