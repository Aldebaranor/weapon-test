package com.soul.fregata.facade;

import com.egova.cloud.FeignClient;
import com.soul.fregata.entity.ExperimentParameter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * created by yangL
 */
@FeignClient(value = "${service.experiment-server:experiment-server}", path = "/unity/experiment")
public interface ExperimentParameterFacade {
    /**
     * 主键查询
     *
     * @param id 主键
     * @return Experiment
     */
    @GetMapping(value = "/{id}")
    ExperimentParameter getById(@PathVariable("id") String id);

    /**
     * 保存
     *
     * @param experimentParameter 试验参数
     * @return 主键
     */
    @PostMapping
    String insert(@RequestBody ExperimentParameter experimentParameter);

    /**
     * 更新
     *
     * @param experimentParameter 试验参数
     */
    @PutMapping
    void update(@RequestBody ExperimentParameter experimentParameter);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @DeleteMapping(value = "/{id}")
    int deleteById(@PathVariable("id") String id);

}
