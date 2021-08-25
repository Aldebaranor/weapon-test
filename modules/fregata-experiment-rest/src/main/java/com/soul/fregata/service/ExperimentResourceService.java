package com.soul.fregata.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.fregata.condition.ExperimentResourceCondition;
import com.soul.fregata.entity.ExperimentResource;
import com.soul.fregata.facade.ExperimentResourceFacade;

import java.util.List;

/**
 * created by 迷途小码农
 */
public interface ExperimentResourceService extends ExperimentResourceFacade {

    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    PageResult<ExperimentResource> page(QueryModel<ExperimentResourceCondition> model);

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
     * @param condition ExperimentResourceCondition
     * @return 场景资源
     */
    List<ExperimentResource> list(ExperimentResourceCondition condition);

    /**
     * 根据试验ID删除
     *
     * @param experimentId 试验ID
     */
    void deleteByExperimentId(String experimentId);

    /**
     * 根据试验ID集合删除
     *
     * @param experimentIds 试验ID集合
     */
    void deleteByExperimentIds(List<String> experimentIds);

}
