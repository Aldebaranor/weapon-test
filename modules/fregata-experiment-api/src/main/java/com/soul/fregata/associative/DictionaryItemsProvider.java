package com.soul.fregata.associative;

import com.egova.associative.AssociativeProvider;
import com.egova.entity.codes.DictionaryIntro;
import com.egova.model.PropertyItem;
import com.google.common.base.Splitter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Component
public class DictionaryItemsProvider implements AssociativeProvider {

    @Override
    public Object associate(Object key) {
        return Optional.ofNullable(key)
                .map(Object::toString)
                .map(e -> {
                    List<String> ids = Splitter.on(",").trimResults().splitToList(e);
                    return ids.stream().map(id -> {
                        DictionaryIntro item = DictionaryIntro.toItem(id);
                        return PropertyItem.build(id, item.getText(), item.getValue());
                    }).collect(Collectors.toList());
                }).orElse(null);
    }

}
