package com.soul.fregata.facade;

import com.egova.cloud.FeignClient;
import com.egova.cloud.FeignToken;
import com.egova.web.annotation.Api;
import com.soul.fregata.entity.EquipmentType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * created by 迷途小码农
 */
@FeignClient(value = "${service.equipment-server:equipment-server}", path = "/unity/equipment-type")
public interface EquipmentTypeFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return EquipmentType
     */
    @GetMapping(value = "/{id}")
    EquipmentType getById(@PathVariable("id") String id);


    @GetMapping(value = "/{id}/children")
    List<EquipmentType> children(@PathVariable("id") String id);

    @GetMapping(value = "/grade/{grade}")
    List<EquipmentType> grade(@PathVariable("grade") int grade);

    /**
     * 保存
     *
     * @param equipmentType 装备大小类表
     * @return 主键
     */
    @PostMapping
    String insert(@RequestBody EquipmentType equipmentType);

    /**
     * 更新
     *
     * @param equipmentType 装备大小类表
     */
    @PutMapping
    void update(@RequestBody EquipmentType equipmentType);

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
