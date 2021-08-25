package com.soul.fregata.service;

import com.soul.fregata.condition.EquipmentRelationCondition;
import com.soul.fregata.entity.EquipmentRelation;
import com.soul.fregata.facade.EquipmentRelationFacade;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;

import java.util.List;

/**
 * created by 迷途小码农
 */
public interface EquipmentRelationService extends EquipmentRelationFacade {

    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    PageResult<EquipmentRelation> page(QueryModel<EquipmentRelationCondition> model);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

    /**
     * 列表查询
     *
     * @param condition SourceCondition
     * @return 列表数据
     */
    List<EquipmentRelation> list(EquipmentRelationCondition condition);

}
