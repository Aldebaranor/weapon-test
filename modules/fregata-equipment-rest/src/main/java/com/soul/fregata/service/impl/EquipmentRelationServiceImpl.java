package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Sorting;
import com.soul.fregata.condition.EquipmentRelationCondition;
import com.soul.fregata.domain.EquipmentRelationRepository;
import com.soul.fregata.entity.EquipmentRelation;
import com.soul.fregata.service.EquipmentRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.util.List;

/**
 * created by 迷途小码农
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class EquipmentRelationServiceImpl extends TemplateService<EquipmentRelation, String> implements EquipmentRelationService {

    private final EquipmentRelationRepository equipmentRelationRepository;

    @Override
    protected AbstractRepositoryBase<EquipmentRelation, String> getRepository() {
        return equipmentRelationRepository;
    }

    @Override
    public PageResult<EquipmentRelation> page(QueryModel<EquipmentRelationCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public List<EquipmentRelation> list(EquipmentRelationCondition condition) {
        return super.query(condition, Sorting.descending("modifyTime", "createTime"));
    }

}
