package com.soul.fregata.controller.unity;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.egova.web.annotation.RequestDecorating;
import com.soul.fregata.condition.ExperimentCondition;
import com.soul.fregata.entity.Experiment;
import com.soul.fregata.facade.ExperimentFacade;
import com.soul.fregata.service.ExperimentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * created by 迷途小码农
 */
@Slf4j
@RestController
@RequestMapping("/unity/experiment")
@RequiredArgsConstructor
public class ExperimentController implements ExperimentFacade {

    private final ExperimentService experimentService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return Experiment
     */
    @Api
    @Override
    public Experiment getById(@PathVariable String id) {
        return experimentService.getById(id);
    }

    /**
     * 保存
     *
     * @param experiment 我的试验
     * @return 主键
     */
    @Api
    @Override
    public String insert(@RequestBody Experiment experiment) {
        return experimentService.insert(experiment);
    }

    /**
     * 更新
     *
     * @param experiment 我的试验
     */
    @Api
    @Override
    public void update(@RequestBody Experiment experiment) {
        experimentService.update(experiment);
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
        return experimentService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<Experiment> page(@RequestBody QueryModel<ExperimentCondition> model) {
        return experimentService.page(model);
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
        return experimentService.deleteByIds(ids);
    }

    /**
     * 更新兵力树
     *
     * @param experiment 试验
     * @return 试验
     */
    @Api
    @PutMapping("/army-tree")
    public Experiment upsertArmyTree(@RequestBody Experiment experiment) {
        experimentService.upsertArmyTree(experiment);
        return experiment;
    }

}
