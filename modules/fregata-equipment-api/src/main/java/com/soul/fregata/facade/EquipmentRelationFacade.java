package com.soul.fregata.facade;

import com.egova.cloud.FeignClient;
import com.soul.fregata.entity.EquipmentRelation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * created by 迷途小码农
 */
@FeignClient(value = "${service.equipment-server:equipment-server}", path = "/unity/equipment-relation")
public interface EquipmentRelationFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return EquipmentRelation
     */
    @GetMapping(value = "/{id}")
    EquipmentRelation getById(@PathVariable("id") String id);

    /**
     * 保存
     *
     * @param equipmentRelation 某个装备可搭载其他装备的关联关系表
     * @return 主键
     */
    @PostMapping
    String insert(@RequestBody EquipmentRelation equipmentRelation);

    /**
     * 更新
     *
     * @param equipmentRelation 某个装备可搭载其他装备的关联关系表
     */
    @PutMapping
    void update(@RequestBody EquipmentRelation equipmentRelation);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @DeleteMapping(value = "/{id}")
    int deleteById(@PathVariable("id") String id);

}
