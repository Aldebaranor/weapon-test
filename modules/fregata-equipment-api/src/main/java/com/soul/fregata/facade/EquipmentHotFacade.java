package com.soul.fregata.facade;

import com.egova.cloud.FeignClient;
import com.egova.model.PropertyItem;
import com.soul.fregata.condition.TopCondition;
import com.soul.fregata.entity.EquipmentHot;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * created by 迷途小码农
 */
@FeignClient(value = "${service.equipment-server:equipment-server}", path = "/unity/equipment-hot")
public interface EquipmentHotFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return EquipmentHot
     */
    @GetMapping(value = "/{id}")
    EquipmentHot getById(@PathVariable("id") String id);

    /**
     * 保存
     *
     * @param equipmentHot 装备浏览表
     * @return 主键
     */
    @PostMapping
    String insert(@RequestBody EquipmentHot equipmentHot);

    /**
     * 更新
     *
     * @param equipmentHot 装备浏览表
     */
    @PutMapping
    void update(@RequestBody EquipmentHot equipmentHot);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @DeleteMapping(value = "/{id}")
    int deleteById(@PathVariable("id") String id);

    /**
     * 热门排行
     *
     * @param condition 条件
     * @return 分页数据
     */
    @PostMapping(value = "/top")
    List<PropertyItem<Long>> top(TopCondition condition);

}
