package com.soul.fregata.controller.unity;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.egova.web.annotation.RequestDecorating;
import com.soul.fregata.condition.EquipmentTypeCondition;
import com.soul.fregata.entity.EquipmentType;
import com.soul.fregata.facade.EquipmentTypeFacade;
import com.soul.fregata.service.EquipmentTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * created by 迷途小码农
 */
@Slf4j
@RestController
@RequestMapping("/unity/equipment-type")
@RequiredArgsConstructor
public class EquipmentTypeController implements EquipmentTypeFacade {

    private final EquipmentTypeService equipmentTypeService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return EquipmentType
     */
    @Api
    @Override
    public EquipmentType getById(@PathVariable String id) {
        return equipmentTypeService.getById(id);
    }

    @Api
    @Override
    public List<EquipmentType> children(String id) {
        return equipmentTypeService.children(id);
    }

    @Api
    @Override
    public List<EquipmentType> grade(int grade) {
        return equipmentTypeService.grade(grade);
    }

    /**
     * 保存
     *
     * @param equipmentType 装备大小类表
     * @return 主键
     */
    @Api
    @Override
    public String insert(@RequestBody EquipmentType equipmentType) {
        return equipmentTypeService.insert(equipmentType);
    }

    /**
     * 更新
     *
     * @param equipmentType 装备大小类表
     */
    @Api
    @Override
    public void update(@RequestBody EquipmentType equipmentType) {
        equipmentTypeService.update(equipmentType);
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
        return equipmentTypeService.deleteById(id);
    }

    @Api
    @Override
    public Map<String, String> getNameMapById() {
        return equipmentTypeService.getNameMapById();
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<EquipmentType> page(@RequestBody QueryModel<EquipmentTypeCondition> model) {
        return equipmentTypeService.page(model);
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
        return equipmentTypeService.deleteByIds(ids);
    }

    /**
     * 树
     *
     * @return 树
     */
    @Api
    @GetMapping("/tree")
    public List<EquipmentType> tree() {
        return equipmentTypeService.tree();
    }

}
