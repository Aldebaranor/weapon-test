package com.soul.fregata.entity.enums;

import com.egova.associative.Associative;
import com.egova.associative.CodeTypeProvider;
import com.egova.model.PropertyDescriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 兵力类型
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Getter
@RequiredArgsConstructor
@Associative(
        name = "_${name}",
        providerClass = CodeTypeProvider.class
)
public enum ArmyType implements PropertyDescriptor {

    /**
     * 红方
     */
    RED("0", "红方"),

    /**
     * 蓝方
     */
    BLUE("1", "蓝方");

    private final String value;
    private final String text;
}
