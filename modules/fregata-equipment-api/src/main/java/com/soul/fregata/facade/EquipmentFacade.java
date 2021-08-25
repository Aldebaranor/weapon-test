package com.soul.fregata.facade;

import com.egova.cloud.FeignClient;
import com.egova.cloud.FeignToken;
import com.soul.fregata.entity.Equipment;
import com.soul.fregata.entity.EquipmentType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * created by 迷途小码农
 */
@FeignClient(value = "${service.equipment-server:equipment-server}", path = "/unity/equipment")
public interface EquipmentFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return Equipment
     */
    @GetMapping(value = "/{id}")
    Equipment getById(@PathVariable("id") String id);


    /**
     * 获取设备可搭建装备，并按指定设备级别分组展示
     *
     * @param id
     * @param grade
     * @return
     */
    @GetMapping(value = "/{id}/carry-group")
    List<EquipmentType> getCarryGroupList(@PathVariable("id") String id, @RequestParam("grade") int grade);


    /**
     * 获取设备可搭建装备
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}/carry-list")
    List<Equipment> getCarryList(@PathVariable("id") String id);

    /**
     * 保存
     *
     * @param equipment 装备基础表
     * @return 主键
     */
    @PostMapping
    String insert(@RequestBody Equipment equipment);

    /**
     * 更新
     *
     * @param equipment 装备基础表
     */
    @PutMapping
    void update(@RequestBody Equipment equipment);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @DeleteMapping(value = "/{id}")
    int deleteById(@PathVariable("id") String id);

    /**
     * id-name map
     *
     * @return map
     */
    @GetMapping({"/name-map/id"})
    @FeignToken(obtain = FeignToken.Obtain.client)
    Map<String, String> getNameMapById();

}
