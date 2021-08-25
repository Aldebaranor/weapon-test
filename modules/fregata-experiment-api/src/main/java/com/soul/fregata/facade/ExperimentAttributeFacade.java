package com.soul.fregata.facade;

import com.egova.cloud.FeignClient;
import com.soul.fregata.entity.ExperimentAttribute;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * created by 迷途小码农
 */
@FeignClient(value = "${service.experiment-server:experiment-server}", path = "/unity/experiment-attribute")
public interface ExperimentAttributeFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return ExperimentAttribute
     */
    @GetMapping(value = "/{id}")
    ExperimentAttribute getById(@PathVariable("id") String id);

    /**
     * 保存
     *
     * @param experimentAttribute 试验属性
     * @return 主键
     */
    @PostMapping
    String insert(@RequestBody ExperimentAttribute experimentAttribute);

    /**
     * 更新
     *
     * @param experimentAttribute 试验属性
     */
    @PutMapping
    void update(@RequestBody ExperimentAttribute experimentAttribute);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @DeleteMapping(value = "/{id}")
    int deleteById(@PathVariable("id") String id);

}
