package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.PipeTaskCondition;
import com.soul.weapon.entity.PipeTask;
import com.soul.weapon.entity.PipeTest;
import com.soul.weapon.entity.codes.PipeState;
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
    String insert(PipeTask pipeTask);

    /**
     * update a task
     * @param pipeTask
     */
    void update(PipeTask pipeTask);

    String save(PipeTask pipeTask);

    /**
     * del by id
     * @param id
     * @return
     */
    int deleteById( String id);

    /**
     * page query by model
     * @param model decide the pageSize and pageIdx to query
     * @return
     */
    PageResult<PipeTask> page(QueryModel<PipeTaskCondition> model);

    /**
     * 开始测试
     * @param takeId
     * @param pipeTests
     * @return
     */
    Boolean startTest(String takeId, List<PipeTest> pipeTests);


    /**
     * get by Name
     * @param name
     * @return
     */
    List<PipeTask> getByName(String name);

    /**
     * 停止测试
     * @param takeId
     * @return
     */
    public Boolean stopTest(String takeId);

}
