package com.soul.fregata.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.fregata.condition.ExperimentCondition;
import com.soul.fregata.entity.Experiment;
import com.soul.fregata.facade.ExperimentFacade;

import java.util.List;

/**
 * created by 迷途小码农
 */
public interface ExperimentService extends ExperimentFacade {

    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    PageResult<Experiment> page(QueryModel<ExperimentCondition> model);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

    /**
     * 更新兵力树
     *
     * @param experiment Experiment
     */
    void upsertArmyTree(Experiment experiment);

}
