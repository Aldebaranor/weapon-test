package com.soul.fregata.controller.unity;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.egova.web.annotation.RequestDecorating;
import com.soul.fregata.condition.ExperimentResourceCondition;
import com.soul.fregata.entity.ExperimentResource;
import com.soul.fregata.facade.ExperimentResourceFacade;
import com.soul.fregata.service.ExperimentResourceService;
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
@RequestMapping("/unity/experiment-resource")
@RequiredArgsConstructor
public class ExperimentResourceController implements ExperimentResourceFacade {

    private final ExperimentResourceService experimentResourceService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return ExperimentResource
     */
    @Api
    @Override
    public ExperimentResource getById(@PathVariable String id) {
        return experimentResourceService.getById(id);
    }

    /**
     * 保存
     *
     * @param experimentResource 场景资源
     * @return 主键
     */
    @Api
    @Override
    public String insert(@RequestBody ExperimentResource experimentResource) {
        return experimentResourceService.insert(experimentResource);
    }

    /**
     * 更新
     *
     * @param experimentResource 场景资源
     */
    @Api
    @Override
    public void update(@RequestBody ExperimentResource experimentResource) {
        experimentResourceService.update(experimentResource);
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
        return experimentResourceService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<ExperimentResource> page(@RequestBody QueryModel<ExperimentResourceCondition> model) {
        return experimentResourceService.page(model);
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
        return experimentResourceService.deleteByIds(ids);
    }

    /**
     * 列表查询
     *
     * @param condition ExperimentResourceCondition
     * @return 资源
     */
    @Api
    @PostMapping("/list")
    public List<ExperimentResource> list(@RequestBody ExperimentResourceCondition condition) {
        return experimentResourceService.list(condition);
    }

}
