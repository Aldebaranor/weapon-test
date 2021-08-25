package com.soul.fregata.controller.unity;

import com.egova.model.PageResult;
import com.egova.model.PropertyItem;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.egova.web.annotation.RequestDecorating;
import com.soul.fregata.condition.EquipmentHotCondition;
import com.soul.fregata.condition.TopCondition;
import com.soul.fregata.entity.EquipmentHot;
import com.soul.fregata.facade.EquipmentHotFacade;
import com.soul.fregata.service.EquipmentHotService;
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
@RequestMapping("/unity/equipment-hot")
@RequiredArgsConstructor
public class EquipmentHotController implements EquipmentHotFacade {

    private final EquipmentHotService equipmentHotService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return EquipmentHot
     */
    @Api
    @Override
    public EquipmentHot getById(@PathVariable String id) {
        return equipmentHotService.getById(id);
    }

    /**
     * 保存
     *
     * @param equipmentHot 装备浏览表
     * @return 主键
     */
    @Api
    @Override
    public String insert(@RequestBody EquipmentHot equipmentHot) {
        return equipmentHotService.insert(equipmentHot);
    }

    /**
     * 更新
     *
     * @param equipmentHot 装备浏览表
     */
    @Api
    @Override
    public void update(@RequestBody EquipmentHot equipmentHot) {
        equipmentHotService.update(equipmentHot);
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
        return equipmentHotService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<EquipmentHot> page(@RequestBody QueryModel<EquipmentHotCondition> model) {
        return equipmentHotService.page(model);
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
        return equipmentHotService.deleteByIds(ids);
    }

    /**
     * 热门排行
     *
     * @param condition 条件
     * @return PageResult
     */
    @Api
    @Override
    public List<PropertyItem<Long>> top(@RequestBody TopCondition condition) {
        return equipmentHotService.top(condition);
    }

}
