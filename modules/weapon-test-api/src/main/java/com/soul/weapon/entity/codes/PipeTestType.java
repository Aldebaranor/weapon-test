package com.soul.weapon.entity.codes;

import com.egova.associative.Associative;
import com.egova.associative.CodeTypeProvider;
import com.egova.lang.DataDict;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.channels.Pipe;

/**
 * @Author: nash5
 * @Date: 2021/9/11 9:56
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("测试项类型字典表")
@Associative(name = "_${name}", providerClass = CodeTypeProvider.class)
public class PipeTestType extends DataDict {

    public static final String type = "weapon-test:pipeTestType";

    public PipeTestType() { super(type, null); }

    public PipeTestType(String value) { super(type, value); };

    public static PipeTestType of(String value) {
        return new PipeTestType(value);
    }

}


