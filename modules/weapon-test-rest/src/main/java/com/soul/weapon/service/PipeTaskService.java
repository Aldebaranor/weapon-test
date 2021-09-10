package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.PipeTaskCondition;
import com.soul.weapon.entity.PipeTask;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
public interface PipeTaskService {

    /**
     * get by id
     * @param id
     * @return
     */
    PipeTask getById(String id);

    /**
     * get all
     * @return
     */
    List<PipeTask> getAll();

    /**
     * insert one
     * @param pipeTask
     * @return
     */
    String insert(@RequestBody PipeTask pipeTask);

    /**
     * update a task
     * @param pipeTask
     */
    void update(@RequestBody PipeTask pipeTask);

    /**
     * del by id
     * @param id
     * @return
     */
    int deleteById(@PathVariable("id") String id);

    /**
     * page query by model
     * @param model decide the pageSize and pageIdx to query
     * @return
     */
    PageResult<PipeTask> page(QueryModel<PipeTaskCondition> model);
    // QueryModel<PipeTaskCondition> page(QueryModel<PipeTaskCondition> model);
}
