package com.soul.fregata.facade;

import com.egova.cloud.FeignClient;
import com.soul.fregata.entity.Scheme;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * created by 迷途小码农
 */
@FeignClient(value = "${service.equipment-server:equipment-server}", path = "/unity/scheme")
public interface SchemeFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return Scheme
     */
    @GetMapping(value = "/{id}")
    Scheme getById(@PathVariable("id") String id);

    /**
     * 保存
     *
     * @param scheme 装备方案表
     * @return 主键
     */
    @PostMapping
    String insert(@RequestBody Scheme scheme);

    /**
     * 更新
     *
     * @param scheme 装备方案表
     */
    @PutMapping
    void update(@RequestBody Scheme scheme);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @DeleteMapping(value = "/{id}")
    int deleteById(@PathVariable("id") String id);

}
