package com.soul.fregata.entity.enums;

import com.egova.associative.Associative;
import com.egova.associative.CodeTypeProvider;
import com.egova.model.PropertyDescriptor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 资产类型
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
public enum AssetType implements PropertyDescriptor {

    /**
     * 扩展字段
     */
    EXTEND_COLUMN("0", "扩展字段"),

    /**
     * 装备参数
     */
    EQUIPMENT_PARAM("1", "装备参数");

    private final String value;
    private final String text;
}
