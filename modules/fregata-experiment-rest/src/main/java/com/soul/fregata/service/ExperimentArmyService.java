package com.soul.fregata.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.fregata.condition.ExperimentArmyCondition;
import com.soul.fregata.entity.ExperimentArmy;
import com.soul.fregata.entity.enums.ArmyType;
import com.soul.fregata.facade.ExperimentArmyFacade;

import java.util.List;

/**
 * created by 迷途小码农
 */
public interface ExperimentArmyService extends ExperimentArmyFacade {

    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    PageResult<ExperimentArmy> page(QueryModel<ExperimentArmyCondition> model);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

    /**
     * 根据试验ID查询
     *
     * @param experimentId 试验ID
     * @return List<ExperimentArmy>
     */
    List<ExperimentArmy> getByExperimentId(String experimentId);

    /**
     * 根据试验ID和类型删除
     *
     * @param experimentId 试验ID
     * @param type         兵力类型
     */
    void deleteByExperimentIdAndType(String experimentId, ArmyType type);

    /**
     * 根据试验ID集合删除
     *
     * @param experimentIds 试验ID集合
     */
    void deleteByExperimentIds(List<String> experimentIds);

    /**
     * 批量写入
     *
     * @param armies 兵力
     */
    void insertList(List<ExperimentArmy> armies);

}
