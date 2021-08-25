package com.soul.fregata.facade;

import com.egova.cloud.FeignClient;
import com.soul.fregata.entity.ExperimentEnvironment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * created by 迷途小码农
 */
@FeignClient(value = "${service.experiment-server:experiment-server}", path = "/unity/experiment-environment")
public interface ExperimentEnvironmentFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return ExperimentEnvironment
     */
    @GetMapping(value = "/{id}")
    ExperimentEnvironment getById(@PathVariable("id") String id);

    /**
     * 保存
     *
     * @param experimentEnvironment 试验环境
     * @return 主键
     */
    @PostMapping
    String insert(@RequestBody ExperimentEnvironment experimentEnvironment);

    /**
     * 更新
     *
     * @param experimentEnvironment 试验环境
     */
    @PutMapping
    void update(@RequestBody ExperimentEnvironment experimentEnvironment);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @DeleteMapping(value = "/{id}")
    int deleteById(@PathVariable("id") String id);

}
