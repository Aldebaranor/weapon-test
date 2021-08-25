package com.soul.fregata.controller.unity;

import com.soul.fregata.condition.EquipmentRelationCondition;
import com.soul.fregata.entity.EquipmentRelation;
import com.soul.fregata.facade.EquipmentRelationFacade;
import com.soul.fregata.service.EquipmentRelationService;
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
@RequestMapping("/unity/equipment-relation")
@RequiredArgsConstructor
public class EquipmentRelationController implements EquipmentRelationFacade {

    private final EquipmentRelationService equipmentRelationService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return EquipmentRelation
     */
    @Api
    @Override
    public EquipmentRelation getById(@PathVariable String id) {
        return equipmentRelationService.getById(id);
    }

    /**
     * 保存
     *
     * @param equipmentRelation 某个装备可搭载其他装备的关联关系表
     * @return 主键
     */
    @Api
    @Override
    public String insert(@RequestBody EquipmentRelation equipmentRelation) {
        return equipmentRelationService.insert(equipmentRelation);
    }

    /**
     * 更新
     *
     * @param equipmentRelation 某个装备可搭载其他装备的关联关系表
     */
    @Api
    @Override
    public void update(@RequestBody EquipmentRelation equipmentRelation) {
        equipmentRelationService.update(equipmentRelation);
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
        return equipmentRelationService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<EquipmentRelation> page(@RequestBody QueryModel<EquipmentRelationCondition> model) {
        return equipmentRelationService.page(model);
    }

    /**
     * 列表查询
     *
     * @param condition
     * @return
     */
    @Api
    @PostMapping("/list")
    public List<EquipmentRelation> list(@RequestBody EquipmentRelationCondition condition) {
        return equipmentRelationService.list(condition);
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
        return equipmentRelationService.deleteByIds(ids);
    }

}
