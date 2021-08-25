package com.soul.fregata.controller.unity;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.egova.web.annotation.RequestDecorating;
import com.soul.fregata.condition.EquipmentCondition;
import com.soul.fregata.entity.Equipment;
import com.soul.fregata.entity.EquipmentType;
import com.soul.fregata.facade.EquipmentFacade;
import com.soul.fregata.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

/**
 * created by 迷途小码农
 */
@Slf4j
@RestController
@RequestMapping("/unity/equipment")
@RequiredArgsConstructor
public class EquipmentController implements EquipmentFacade {

    private final EquipmentService equipmentService;

    @Api
    @Override
    public List<EquipmentType> getCarryGroupList(@PathVariable("id") String id, @RequestParam("grade") int grade) {
        return equipmentService.getCarryGroupList(id, grade);
    }

    @Api
    @Override
    public List<Equipment> getCarryList(@PathVariable("id") String id) {
        return equipmentService.getCarryList(id);
    }


    /**
     * 主键获取
     *
     * @param id 主键
     * @return Equipment
     */
    @Api
    @Override
    public Equipment getById(@PathVariable String id) {
        return equipmentService.getById(id);
    }

    /**
     * 保存
     *
     * @param equipment 装备基础表
     * @return 主键
     */
    @Api
    @Override
    public String insert(@RequestBody Equipment equipment) {
        return equipmentService.insert(equipment);
    }

    /**
     * 更新
     *
     * @param equipment 装备基础表
     */
    @Api
    @Override
    public void update(@RequestBody Equipment equipment) {
        equipmentService.update(equipment);
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
        return equipmentService.deleteById(id);
    }

    @Api
    @Override
    public Map<String, String> getNameMapById() {
        return equipmentService.getNameMapById();
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<Equipment> page(@RequestBody QueryModel<EquipmentCondition> model) {
        return equipmentService.page(model);
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
        return equipmentService.deleteByIds(ids);
    }

}
