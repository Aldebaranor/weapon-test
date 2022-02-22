package com.soul.fire.condition;

import com.egova.model.annotation.Display;
import com.flagwind.persistent.annotation.Condition;
import com.flagwind.persistent.annotation.ConditionOperator;
import com.flagwind.persistent.model.ClauseOperator;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@Condition
@Display("武器表")
public class FireWeaponCondition implements Serializable {

    @Display("id")
    @ConditionOperator(name = "id", operator = ClauseOperator.Equal)
    private String id;

    @Display("type")
    @ConditionOperator(name = "type", operator = ClauseOperator.Equal)
    private String type;

    @Display("code")
    @ConditionOperator(name = "code", operator = ClauseOperator.Equal)
    private String code;

    @Display("selfCheck")
    @ConditionOperator(name = "selfCheck",operator = ClauseOperator.Equal)
    private boolean selfCheck;

    @Display("disabled")
    @ConditionOperator(name = "disabled",operator = ClauseOperator.Equal)
    private boolean disabled;

}
