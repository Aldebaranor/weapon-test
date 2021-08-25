package com.soul.fregata.controller.unity;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.egova.web.annotation.RequestDecorating;
import com.soul.fregata.condition.ExperimentEnvironmentCondition;
import com.soul.fregata.entity.ExperimentEnvironment;
import com.soul.fregata.facade.ExperimentEnvironmentFacade;
import com.soul.fregata.service.ExperimentEnvironmentService;
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
@RequestMapping("/unity/experiment-environment")
@RequiredArgsConstructor
public class ExperimentEnvironmentController implements ExperimentEnvironmentFacade {

    private final ExperimentEnvironmentService experimentEnvironmentService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return ExperimentEnvironment
     */
    @Api
    @Override
    public ExperimentEnvironment getById(@PathVariable String id) {
        return experimentEnvironmentService.getById(id);
    }

    /**
     * 保存
     *
     * @param experimentEnvironment 试验环境
     * @return 主键
     */
    @Api
    @Override
    public String insert(@RequestBody ExperimentEnvironment experimentEnvironment) {
        return experimentEnvironmentService.insert(experimentEnvironment);
    }

    /**
     * 更新
     *
     * @param experimentEnvironment 试验环境
     */
    @Api
    @Override
    public void update(@RequestBody ExperimentEnvironment experimentEnvironment) {
        experimentEnvironmentService.update(experimentEnvironment);
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
        return experimentEnvironmentService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<ExperimentEnvironment> page(@RequestBody QueryModel<ExperimentEnvironmentCondition> model) {
        return experimentEnvironmentService.page(model);
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
        return experimentEnvironmentService.deleteByIds(ids);
    }

    /**
     * 列表查询
     *
     * @param condition ExperimentEnvironmentCondition
     * @return 资源
     */
    @Api
    @PostMapping("/list")
    public List<ExperimentEnvironment> list(@RequestBody ExperimentEnvironmentCondition condition) {
        return experimentEnvironmentService.list(condition);
    }

}
