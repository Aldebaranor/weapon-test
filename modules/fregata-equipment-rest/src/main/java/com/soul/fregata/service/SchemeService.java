package com.soul.fregata.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.fregata.condition.SchemeCondition;
import com.soul.fregata.entity.Scheme;
import com.soul.fregata.facade.SchemeFacade;

import java.util.List;

/**
 * created by 迷途小码农
 */
public interface SchemeService extends SchemeFacade {

    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    PageResult<Scheme> page(QueryModel<SchemeCondition> model);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

    List<Scheme> defaultScheme(String equipmentId);

    List<Scheme> list(SchemeCondition condition);

}
