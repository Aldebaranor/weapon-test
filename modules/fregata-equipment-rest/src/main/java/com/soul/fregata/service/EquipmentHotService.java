package com.soul.fregata.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.fregata.condition.EquipmentHotCondition;
import com.soul.fregata.entity.EquipmentHot;
import com.soul.fregata.facade.EquipmentHotFacade;

import java.util.List;

/**
 * created by 迷途小码农
 */
public interface EquipmentHotService extends EquipmentHotFacade {

    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    PageResult<EquipmentHot> page(QueryModel<EquipmentHotCondition> model);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

}
