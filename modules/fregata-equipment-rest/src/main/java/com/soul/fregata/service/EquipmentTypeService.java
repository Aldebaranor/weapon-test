package com.soul.fregata.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.fregata.condition.EquipmentTypeCondition;
import com.soul.fregata.entity.EquipmentType;
import com.soul.fregata.facade.EquipmentTypeFacade;

import java.util.List;

/**
 * created by 迷途小码农
 */
public interface EquipmentTypeService extends EquipmentTypeFacade {

    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    PageResult<EquipmentType> page(QueryModel<EquipmentTypeCondition> model);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

    /**
     * 树
     *
     * @return 树
     */
    List<EquipmentType> tree();

    List<EquipmentType> children(String id);

    List<EquipmentType> grade(int grade);

}
