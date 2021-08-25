package com.soul.fregata.entity.codes;

import com.egova.associative.Associative;
import com.egova.associative.CodeTypeProvider;
import com.egova.lang.DataIntro;
import com.flagwind.application.Application;
import com.soul.fregata.entity.Equipment;
import com.soul.fregata.facade.EquipmentFacade;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Slf4j
@Associative(name = "_${name}", providerClass = CodeTypeProvider.class)
public class EquipmentIntro extends DataIntro {

    private String value;

    public EquipmentIntro(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getText() {
        EquipmentFacade facade = Application.resolve(EquipmentFacade.class);
        Map<String, String> nameMap = synchronizeLoad(EquipmentIntro.class, Equipment.NAME + ":name-map:id", facade::getNameMapById);
        return nameMap.get(this.value);
    }

}
