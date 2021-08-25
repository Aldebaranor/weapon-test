package com.soul.fregata.entity.codes;

import com.egova.associative.Associative;
import com.egova.associative.CodeTypeProvider;
import com.egova.lang.DataIntro;
import com.flagwind.application.Application;
import com.soul.fregata.entity.EquipmentType;
import com.soul.fregata.facade.EquipmentTypeFacade;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Slf4j
@Associative(name = "_${name}", providerClass = CodeTypeProvider.class)
public class EquipmentTypeIntro extends DataIntro {

    private String value;

    public EquipmentTypeIntro(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getText() {
        EquipmentTypeFacade facade = Application.resolve(EquipmentTypeFacade.class);
        Map<String, String> nameMap = synchronizeLoad(EquipmentTypeIntro.class, EquipmentType.NAME + ":name-map:id", facade::getNameMapById);
        return nameMap.get(this.value);
    }

}
