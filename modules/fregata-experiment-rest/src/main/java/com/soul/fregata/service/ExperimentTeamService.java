package com.soul.fregata.service;

import com.egova.entity.Person;
import com.soul.fregata.condition.ExperimentTeamCondition;
import com.soul.fregata.entity.ExperimentTeam;
import com.soul.fregata.facade.ExperimentTeamFacade;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;

import java.util.List;

/**
 * created by 迷途小码农
 */
public interface ExperimentTeamService extends ExperimentTeamFacade {

    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    PageResult<ExperimentTeam> page(QueryModel<ExperimentTeamCondition> model);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

    /**
     * 查询团队成员
     *
     * @param condition
     * @return List<ExperimentTeam>
     */
    List<ExperimentTeam> list(ExperimentTeamCondition condition);

    /**
     * 人员搜索
     *
     * @param experimentId
     * @return List<Person>
     */
    List<Person> getByExperimentId(String experimentId);

    /**
     * 获取操作人权限
     *
     * @param
     * @return boolean
     */
    boolean checkAuthorization(String experimentId);
}
