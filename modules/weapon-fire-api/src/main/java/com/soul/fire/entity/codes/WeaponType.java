package com.soul.fire.entity.codes;

import com.egova.associative.Associative;
import com.egova.associative.CodeTypeProvider;
import com.egova.lang.DataDict;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Auther: 码头工人
 * @Date: 2021/11/01/2:10 下午
 * @Description:
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("运动状态字典表")
@Associative(name = "_${name}", providerClass = CodeTypeProvider.class)
public class WeaponType extends DataDict {

    public static final String type = "weapon-test:weaponType";

    public WeaponType() { super(type, null); }

    public WeaponType(String value) { super(type, value); };

    public static WeaponType of(String value) {
        return new WeaponType(value);
    }
}
