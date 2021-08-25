package com.soul.fregata.controller.unity;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.egova.web.annotation.RequestDecorating;
import com.soul.fregata.condition.ExperimentAttributeCondition;
import com.soul.fregata.entity.ExperimentAttribute;
import com.soul.fregata.facade.ExperimentAttributeFacade;
import com.soul.fregata.service.ExperimentAttributeService;
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
@RequestMapping("/unity/experiment-attribute")
@RequiredArgsConstructor
public class ExperimentAttributeController implements ExperimentAttributeFacade {

    private final ExperimentAttributeService experimentAttributeService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return ExperimentAttribute
     */
    @Api
    @Override
    public ExperimentAttribute getById(@PathVariable String id) {
        return experimentAttributeService.getById(id);
    }

    /**
     * 保存
     *
     * @param experimentAttribute 试验属性
     * @return 主键
     */
    @Api
    @Override
    public String insert(@RequestBody ExperimentAttribute experimentAttribute) {
        return experimentAttributeService.insert(experimentAttribute);
    }

    /**
     * 更新
     *
     * @param experimentAttribute 试验属性
     */
    @Api
    @Override
    public void update(@RequestBody ExperimentAttribute experimentAttribute) {
        experimentAttributeService.update(experimentAttribute);
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
        return experimentAttributeService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<ExperimentAttribute> page(@RequestBody QueryModel<ExperimentAttributeCondition> model) {
        return experimentAttributeService.page(model);
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
        return experimentAttributeService.deleteByIds(ids);
    }

    /**
     * 列表查询
     *
     * @param condition ExperimentAttributeCondition
     * @return 资源
     */
    @Api
    @PostMapping("/list")
    public List<ExperimentAttribute> list(@RequestBody ExperimentAttributeCondition condition) {
        return experimentAttributeService.list(condition);
    }

}
