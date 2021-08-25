package com.soul.fregata.facade;

import com.egova.cloud.FeignClient;
import com.soul.fregata.entity.SchemeDetail;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * created by 迷途小码农
 */
@FeignClient(value = "${service.equipment-server:equipment-server}", path = "/unity/scheme-detail")
public interface SchemeDetailFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return SchemeDetail
     */
    @GetMapping(value = "/{id}")
    SchemeDetail getById(@PathVariable("id") String id);

    /**
     * 保存
     *
     * @param schemeDetail 装备方案与装备关联表
     * @return 主键
     */
    @PostMapping
    String insert(@RequestBody SchemeDetail schemeDetail);

    /**
     * 更新
     *
     * @param schemeDetail 装备方案与装备关联表
     */
    @PutMapping
    void update(@RequestBody SchemeDetail schemeDetail);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @DeleteMapping(value = "/{id}")
    int deleteById(@PathVariable("id") String id);

}
