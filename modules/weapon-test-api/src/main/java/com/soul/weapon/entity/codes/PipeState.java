package com.soul.weapon.entity.codes;

import com.egova.associative.Associative;
import com.egova.associative.CodeTypeProvider;
import com.egova.lang.DataDict;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Author: XinLai
 * @Date: 2021/9/15 14:36
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("运动状态字典表")
@Associative(name = "_${name}", providerClass = CodeTypeProvider.class)
public class PipeState extends DataDict {

    public static final String type = "weapon-test:pipeState";

    public PipeState() { super(type, null); }

    public PipeState(String value) { super(type, value); };

    public static PipeState of(String value) {
        return new PipeState(value);
    }
}
