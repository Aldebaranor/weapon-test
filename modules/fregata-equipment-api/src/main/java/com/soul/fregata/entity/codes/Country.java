package com.soul.fregata.entity.codes;

import com.egova.associative.Associative;
import com.egova.associative.CodeTypeProvider;
import com.egova.lang.DataDict;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author chendb
 * @description:
 * @date 2020-11-07 00:28:03
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("国家")
@Associative(name = "_${name}", providerClass = CodeTypeProvider.class)
public class Country extends DataDict {

    public static final String type = "fregata:country";

    public Country() {
        super(type, null);
    }

    public Country(String value) {
        super(type, value);
    }


    public static Country of(String value) {
        return new Country(value);
    }

}
