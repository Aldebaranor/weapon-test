package com.soul.fregata.controller.unity;

import com.soul.fregata.condition.ExperimentArmyCondition;
import com.soul.fregata.entity.ExperimentArmy;
import com.soul.fregata.facade.ExperimentArmyFacade;
import com.soul.fregata.service.ExperimentArmyService;
import com.egova.web.annotation.Api;
import com.egova.web.annotation.RequestDecorating;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * created by 迷途小码农
 */
@Slf4j
@RestController
@RequestMapping("/unity/experiment-army")
@RequiredArgsConstructor
public class ExperimentArmyController implements ExperimentArmyFacade {

    private final ExperimentArmyService experimentArmyService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return ExperimentArmy
     */
    @Api
    @Override
    public ExperimentArmy getById(@PathVariable String id) {
        return experimentArmyService.getById(id);
    }

    /**
     * 保存
     *
     * @param experimentArmy 我的试验兵力
     * @return 主键
     */
    @Api
    @Override
    public String insert(@RequestBody ExperimentArmy experimentArmy) {
        return experimentArmyService.insert(experimentArmy);
    }

    /**
     * 更新
     *
     * @param experimentArmy 我的试验兵力
     */
    @Api
    @Override
    public void update(@RequestBody ExperimentArmy experimentArmy) {
        experimentArmyService.update(experimentArmy);
    }

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @Api
    @Override
    public int deleteById(@PathVariable String id) {
        return experimentArmyService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<ExperimentArmy> page(@RequestBody QueryModel<ExperimentArmyCondition> model) {
        return experimentArmyService.page(model);
    }

    /**
     * 批量删除
     *
     * @param ids 主键列表
     * @return 影响记录行数
     */
    @Api
    @PostMapping("/batch")
    @RequestDecorating(value = "delete")
    public int batchDelete(@RequestBody List<String> ids) {
        return experimentArmyService.deleteByIds(ids);
    }

}
