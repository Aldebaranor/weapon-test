package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.PropertyItem;
import com.egova.model.QueryModel;
import com.egova.persistent.GroupDescriptor;
import com.egova.persistent.RowIndex;
import com.soul.fregata.condition.EquipmentHotCondition;
import com.soul.fregata.condition.TopCondition;
import com.soul.fregata.domain.EquipmentHotRepository;
import com.soul.fregata.entity.EquipmentHot;
import com.soul.fregata.service.EquipmentHotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.util.List;
import java.util.Optional;

/**
 * created by 迷途小码农
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class EquipmentHotServiceImpl extends TemplateService<EquipmentHot, String> implements EquipmentHotService {

    private final EquipmentHotRepository equipmentHotRepository;

    @Override
    protected AbstractRepositoryBase<EquipmentHot, String> getRepository() {
        return equipmentHotRepository;
    }

    @Override
    public PageResult<EquipmentHot> page(QueryModel<EquipmentHotCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public List<PropertyItem<Long>> top(TopCondition condition) {
        GroupDescriptor groupDescriptor = GroupDescriptor.create("equipmentId");
        Integer top = Optional.ofNullable(condition.getTop()).orElse(10);
        return super.count(groupDescriptor, new EquipmentHotCondition(), RowIndex.create(1, top));
    }

}
