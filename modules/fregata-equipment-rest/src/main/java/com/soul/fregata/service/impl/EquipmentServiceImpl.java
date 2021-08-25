package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.exception.ExceptionUtils;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.security.UserContext;
import com.flagwind.commons.StringUtils;
import com.flagwind.persistent.model.ChildClause;
import com.flagwind.persistent.model.SingleClause;
import com.google.common.collect.Lists;
import com.soul.fregata.condition.EquipmentCondition;
import com.soul.fregata.domain.EquipmentRelationRepository;
import com.soul.fregata.domain.EquipmentRepository;
import com.soul.fregata.domain.EquipmentTypeRepository;
import com.soul.fregata.entity.Equipment;
import com.soul.fregata.entity.EquipmentType;
import com.soul.fregata.service.EquipmentRelationService;
import com.soul.fregata.service.EquipmentService;
import com.soul.fregata.utils.ValidateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Priority;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created by 迷途小码农
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class EquipmentServiceImpl extends TemplateService<Equipment, String> implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    private final EquipmentRelationRepository equipmentRelationRepository;

    private final EquipmentTypeRepository equipmentTypeRepository;


    @Override
    protected AbstractRepositoryBase<Equipment, String> getRepository() {
        return equipmentRepository;
    }

    @Override
    public PageResult<Equipment> page(QueryModel<EquipmentCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public Map<String, String> getNameMapById() {
        return this.map("id", "name");
    }

    @Override
    public List<Equipment> getCarryList(String equipmentId) {
        ChildClause clause = ChildClause.include("id", "carryEquipmentId", "fregata_equipment_relation");
        clause.add(SingleClause.equal("equipmentId", equipmentId));
        return this.equipmentRepository.query(clause);
    }

    private List<EquipmentType> groupTypeByGrade(int grade, List<Equipment> all) {
        List<String> types;
        List<EquipmentType> equipmentTypes;
        switch (grade) {
            case 1:
                types = all.stream().filter(s -> s.getCategoryType() != null).map(s -> s.getCategoryType().getValue()).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(types)) {
                    return Collections.emptyList();
                }
                equipmentTypes = equipmentTypeRepository.query(SingleClause.in("id", types.toArray()));
                for (EquipmentType equipmentType : equipmentTypes) {
                    List<Equipment> equipments = all.stream().filter(s -> s.getCategoryType() != null)
                            .filter(s -> s.getCategoryType().getValue().equals(equipmentType.getId()))
                            .collect(Collectors.toList());
                    equipmentType.set("equipments", equipments);
                }
                return equipmentTypes;
            case 2:
                types = all.stream().filter(s -> s.getMainType() != null).map(s -> s.getMainType().getValue()).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(types)) {
                    return Collections.emptyList();
                }
                equipmentTypes = equipmentTypeRepository.query(SingleClause.in("id", types.toArray()));
                for (EquipmentType equipmentType : equipmentTypes) {
                    List<Equipment> equipments = all.stream().filter(s -> s.getMainType() != null)
                            .filter(s -> s.getMainType().getValue().equals(equipmentType.getId()))
                            .collect(Collectors.toList());
                    equipmentType.set("equipments", equipments);
                }
                return equipmentTypes;
            case 3:
                types = all.stream().filter(s -> s.getSubType() != null).map(s -> s.getSubType().getValue()).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(types)) {
                    return Collections.emptyList();
                }
                equipmentTypes = equipmentTypeRepository.query(SingleClause.in("id", types.toArray()));
                for (EquipmentType equipmentType : equipmentTypes) {
                    List<Equipment> equipments = all.stream().filter(s -> s.getSubType() != null)
                            .filter(s -> s.getSubType().getValue().equals(equipmentType.getId()))
                            .collect(Collectors.toList());
                    equipmentType.set("equipments", equipments);
                }
                return equipmentTypes;
            case 4:
            default:
                types = all.stream().filter(s -> s.getThirdType() != null).map(s -> s.getThirdType().getValue()).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(types)) {
                    return Collections.emptyList();
                }
                equipmentTypes = equipmentTypeRepository.query(SingleClause.in("id", types.toArray()));
                for (EquipmentType equipmentType : equipmentTypes) {
                    List<Equipment> equipments = all.stream().filter(s -> s.getThirdType() != null)
                            .filter(s -> s.getThirdType().getValue().equals(equipmentType.getId()))
                            .collect(Collectors.toList());
                    equipmentType.set("equipments", equipments);
                }
                return equipmentTypes;
        }
    }

    public List<EquipmentType> getCarryGroupList(String equipmentId, int grade) {
        List<Equipment> all = this.getCarryList(equipmentId);
        return groupTypeByGrade(grade, all);
    }

    @Override
    public String insert(Equipment entity) {
        ValidateUtil.validate(entity);
        String id = getKey();
        entity.setId(id);
        entity.setCode(id);
        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        entity.setCreator(UserContext.username());
        return super.insert(entity);
    }

    @Override
    public void update(Equipment entity) {
        ValidateUtil.validate(entity);
        if (StringUtils.isBlank(entity.getId())) {
            throw ExceptionUtils.api("装备主键不能为空");
        }
        entity.setModifyTime(new Timestamp(System.currentTimeMillis()));
        entity.setModifier(UserContext.username());
        super.update(entity);
    }

    @Override
    public Equipment getById(String s) {
        return super.seekById(s);
    }


}
