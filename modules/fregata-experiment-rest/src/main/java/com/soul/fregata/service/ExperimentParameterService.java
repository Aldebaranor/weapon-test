package com.soul.fregata.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.fregata.condition.ExperimentParameterCondition;
import com.soul.fregata.entity.ExperimentParameter;
import com.soul.fregata.facade.ExperimentParameterFacade;

import java.util.List;

/**
 * created by yangL
 */
public interface ExperimentParameterService extends ExperimentParameterFacade{
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

    ExperimentParameter getByExperimentId(String experimentId);

}
