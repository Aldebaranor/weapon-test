package com.soul.fire.entity.enums;

import com.egova.associative.Associative;
import com.egova.associative.CodeTypeProvider;
import com.egova.model.PropertyDescriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author nemo
 * @Auther: 码头工人
 * @Date: 2021/11/01/2:10 下午
 * @Description:
 */
@Associative(name = "_${name}", providerClass = CodeTypeProvider.class)
@Getter
@RequiredArgsConstructor
public enum ConflictSourceType implements PropertyDescriptor {

    REALTIME("0", "管控"),

    ANTICIPATION("1", "预判"),

    ;

    private final String value;
    private final String text;

}