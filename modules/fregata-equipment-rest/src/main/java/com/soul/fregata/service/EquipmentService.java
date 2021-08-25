package com.soul.fregata.service;

import com.soul.fregata.condition.EquipmentCondition;
import com.soul.fregata.entity.Equipment;
import com.soul.fregata.facade.EquipmentFacade;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;

import java.util.List;

/**
 * created by 迷途小码农
 */
public interface EquipmentService extends EquipmentFacade {

    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    PageResult<Equipment> page(QueryModel<EquipmentCondition> model);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

}
