package com.soul.fregata.facade;

import com.egova.cloud.FeignClient;
import com.soul.fregata.entity.ExperimentTeam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * created by 迷途小码农
 */
@FeignClient(value = "${service.experiment-server:experiment-server}", path = "/unity/experiment-team")
public interface ExperimentTeamFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return ExperimentShare
     */
    @GetMapping(value = "/{id}")
    ExperimentTeam getById(@PathVariable("id") String id);

    /**
     * 保存
     *
     * @param experimentTeam 试验分享
     * @return 主键
     */
    @PostMapping
    String insert(@RequestBody ExperimentTeam experimentTeam);

    /**
     * 更新
     *
     * @param experimentTeam 试验分享
     */
    @PutMapping
    void update(@RequestBody ExperimentTeam experimentTeam);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @DeleteMapping(value = "/{id}")
    int deleteById(@PathVariable("id") String id);

}
