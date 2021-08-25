package com.soul.fregata.associative;

import com.egova.associative.AssociativeProvider;
import com.soul.fregata.entity.codes.EquipmentIntro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class EquipmentNameProvider implements AssociativeProvider {

    @Override
    public Object associate(Object id) {
        return Optional.ofNullable(id)
                .map(Object::toString)
                .map(EquipmentIntro::new)
                .map(EquipmentIntro::getText)
                .orElse(null);
    }

}
