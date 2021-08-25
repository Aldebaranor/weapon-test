package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.security.UserContext;
import com.soul.fregata.condition.EquipmentAssetCondition;
import com.soul.fregata.domain.EquipmentAssetRepository;
import com.soul.fregata.entity.EquipmentAsset;
import com.soul.fregata.service.EquipmentAssetService;
import com.soul.fregata.utils.ValidateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Priority;
import java.sql.Timestamp;

/**
 * created by 迷途小码农
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class EquipmentAssetServiceImpl extends TemplateService<EquipmentAsset, String> implements EquipmentAssetService {

    private final EquipmentAssetRepository equipmentAssetRepository;

    @Override
    protected AbstractRepositoryBase<EquipmentAsset, String> getRepository() {
        return equipmentAssetRepository;
    }

    @Override
    public PageResult<EquipmentAsset> page(QueryModel<EquipmentAssetCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(EquipmentAsset entity) {
        ValidateUtil.validate(entity);
        String id = getKey();
        entity.setId(id);
        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        entity.setCreator(UserContext.username());
        equipmentAssetRepository.deleteByEquipmentId(entity.getEquipmentId());
        equipmentAssetRepository.insert(entity);
        return id;
    }

}
