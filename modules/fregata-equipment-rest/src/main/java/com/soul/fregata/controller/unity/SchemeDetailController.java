package com.soul.fregata.controller.unity;

import com.soul.fregata.condition.SchemeDetailCondition;
import com.soul.fregata.entity.SchemeDetail;
import com.soul.fregata.facade.SchemeDetailFacade;
import com.soul.fregata.service.SchemeDetailService;
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
@RequestMapping("/unity/scheme-detail")
@RequiredArgsConstructor
public class SchemeDetailController implements SchemeDetailFacade {

    private final SchemeDetailService schemeDetailService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return SchemeDetail
     */
    @Api
    @Override
    public SchemeDetail getById(@PathVariable String id) {
        return schemeDetailService.getById(id);
    }

    /**
     * 保存
     *
     * @param schemeDetail 装备方案与装备关联表
     * @return 主键
     */
    @Api
    @Override
    public String insert(@RequestBody SchemeDetail schemeDetail) {
        return schemeDetailService.insert(schemeDetail);
    }

    /**
     * 更新
     *
     * @param schemeDetail 装备方案与装备关联表
     */
    @Api
    @Override
    public void update(@RequestBody SchemeDetail schemeDetail) {
        schemeDetailService.update(schemeDetail);
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
        return schemeDetailService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<SchemeDetail> page(@RequestBody QueryModel<SchemeDetailCondition> model) {
        return schemeDetailService.page(model);
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
        return schemeDetailService.deleteByIds(ids);
    }

    /**
     * 列表查询
     *
     * @param condition
     * @return
     */
    @Api
    @PostMapping("/list")
    public List<SchemeDetail> list(@RequestBody SchemeDetailCondition condition) {
        return schemeDetailService.list(condition);
    }

    @Api
    @PostMapping("/save/{schemeId}")
    public void save(@PathVariable String schemeId, @RequestBody List<SchemeDetail> details) {
        schemeDetailService.save(schemeId, details);
    }

}
