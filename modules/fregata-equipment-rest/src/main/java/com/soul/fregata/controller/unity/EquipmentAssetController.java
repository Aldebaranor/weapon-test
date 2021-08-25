package com.soul.fregata.controller.unity;

import com.soul.fregata.condition.EquipmentAssetCondition;
import com.soul.fregata.entity.EquipmentAsset;
import com.soul.fregata.facade.EquipmentAssetFacade;
import com.soul.fregata.service.EquipmentAssetService;
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
@RequestMapping("/unity/equipment-asset")
@RequiredArgsConstructor
public class EquipmentAssetController implements EquipmentAssetFacade {

    private final EquipmentAssetService equipmentAssetService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return EquipmentAsset
     */
    @Api
    @Override
    public EquipmentAsset getById(@PathVariable String id) {
        return equipmentAssetService.getById(id);
    }

    /**
     * 保存
     *
     * @param equipmentAsset 设备资源
     * @return 主键
     */
    @Api
    @Override
    public String save(@RequestBody EquipmentAsset equipmentAsset) {
        return equipmentAssetService.save(equipmentAsset);
    }

    /**
     * 更新
     *
     * @param equipmentAsset 设备资源
     */
    @Api
    @Override
    public void update(@RequestBody EquipmentAsset equipmentAsset) {
        equipmentAssetService.update(equipmentAsset);
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
        return equipmentAssetService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<EquipmentAsset> page(@RequestBody QueryModel<EquipmentAssetCondition> model) {
        return equipmentAssetService.page(model);
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
        return equipmentAssetService.deleteByIds(ids);
    }

}
