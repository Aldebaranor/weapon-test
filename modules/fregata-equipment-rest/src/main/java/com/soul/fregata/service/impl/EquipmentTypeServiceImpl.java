package com.soul.fregata.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.SingleClause;
import com.soul.fregata.condition.EquipmentTypeCondition;
import com.soul.fregata.domain.EquipmentTypeRepository;
import com.soul.fregata.entity.EquipmentType;
import com.soul.fregata.service.EquipmentTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * created by 迷途小码农
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class EquipmentTypeServiceImpl extends TemplateService<EquipmentType, String> implements EquipmentTypeService {

    private final EquipmentTypeRepository equipmentTypeRepository;

    @Override
    protected AbstractRepositoryBase<EquipmentType, String> getRepository() {
        return equipmentTypeRepository;
    }

    @Override
    public PageResult<EquipmentType> page(QueryModel<EquipmentTypeCondition> model) {
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public List<EquipmentType> tree() {
        return buildTree(
                getAll(),
                (first, second) -> StringUtils.equals(first.getId(), second.getParentId()),
                item -> StringUtils.isBlank(item.getParentId())
        );
    }

    public List<EquipmentType> children(String id){
       return this.query(SingleClause.equal("parentId",id));
    }

    public List<EquipmentType> grade(int grade){
        return this.query(SingleClause.equal("grade",grade));
    }


    /**
     * 构建树结构
     *
     * @param data              数据
     * @param isParentPredicate 前者是否为后者的父级
     * @param isTopPredicate    元素是否为顶级
     * @return 树型结构
     */
    private List<EquipmentType> buildTree(
            List<EquipmentType> data,
            BiPredicate<EquipmentType, EquipmentType> isParentPredicate,
            Predicate<EquipmentType> isTopPredicate
    ) {
        for (EquipmentType first : data) {
            if (first.getChildren() == null) {
                first.setChildren(new LinkedList<>());
            }
            for (EquipmentType second : data) {
                // first 为 second 父级
                if (isParentPredicate.test(first, second)) {
                    first.getChildren().add(second);
                }
            }
        }
        // 返回顶级
        return data.stream().filter(isTopPredicate).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getNameMapById() {
        return map("id", "name");
    }

}
