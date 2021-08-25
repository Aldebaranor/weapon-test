package com.soul.fregata.controller.unity;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.egova.web.annotation.RequestDecorating;
import com.soul.fregata.condition.SchemeCondition;
import com.soul.fregata.entity.Scheme;
import com.soul.fregata.facade.SchemeFacade;
import com.soul.fregata.service.SchemeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/unity/scheme")
@RequiredArgsConstructor
public class SchemeController implements SchemeFacade {

    private final SchemeService schemeService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return Scheme
     */
    @Api
    @Override
    public Scheme getById(@PathVariable String id) {
        return schemeService.getById(id);
    }

    /**
     * 保存
     *
     * @param scheme 装备方案表
     * @return 主键
     */
    @Api
    @Override
    public String insert(@RequestBody Scheme scheme) {
        return schemeService.insert(scheme);
    }

    /**
     * 更新
     *
     * @param scheme 装备方案表
     */
    @Api
    @Override
    public void update(@RequestBody Scheme scheme) {
        schemeService.update(scheme);
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
        return schemeService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<Scheme> page(@RequestBody QueryModel<SchemeCondition> model) {
        return schemeService.page(model);
    }

    /**
     * 列表查询
     *
     * @param condition SchemeCondition
     * @return 列表
     */
    @Api
    @PostMapping("/list")
    public List<Scheme> list(@RequestBody SchemeCondition condition) {
        return schemeService.list(condition);
    }

    /**
     * 默认方案
     *
     * @param equipmentId 设备id
     * @return List
     */
    @Api
    @GetMapping("/default/{equipmentId}")
    public List<Scheme> defaultScheme(@PathVariable String equipmentId) {
        return schemeService.defaultScheme(equipmentId);
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
        return schemeService.deleteByIds(ids);
    }

}
