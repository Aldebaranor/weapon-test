package com.soul.fregata.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.fregata.condition.ExperimentEnvironmentCondition;
import com.soul.fregata.entity.ExperimentEnvironment;
import com.soul.fregata.facade.ExperimentEnvironmentFacade;

import java.util.List;

/**
 * created by 迷途小码农
 */
public interface ExperimentEnvironmentService extends ExperimentEnvironmentFacade {

    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    PageResult<ExperimentEnvironment> page(QueryModel<ExperimentEnvironmentCondition> model);

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
     * @param condition ExperimentEnvironmentCondition
     * @return 场景资源
     */
    List<ExperimentEnvironment> list(ExperimentEnvironmentCondition condition);

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
