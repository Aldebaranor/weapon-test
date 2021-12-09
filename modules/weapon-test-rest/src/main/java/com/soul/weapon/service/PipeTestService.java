package com.soul.weapon.service;

import com.soul.weapon.entity.PipeTask;
import com.soul.weapon.entity.PipeTest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
public interface PipeTestService {

    /**
     * get by id
     * @param id
     * @return
     */
    PipeTest getById(String id);

    /**
     * get all
     * @return
     */
    List<PipeTest> getAll();

    /**
     * insert one
     * @param pipeTest
     * @return
     */
    String insert(PipeTest pipeTest);

    /**
     * update a test
     * @param pipeTest
     */
    void update(PipeTest pipeTest);

    /**
     * del by id
     * @param id
     * @return
     */
    int deleteById(String id);

    /**
     * query by taskId
     * @param taskId
     * @return
     */
    List<PipeTest> getByTaskId(String taskId);

    /**
     * delete by TaskId
     * @param id
     * @return
     */
    int deleteByTaskId(String id);

    /**
     * save by TaskId
     * @param taskId
     * @param list
     */
    void saveByTaskId(String taskId,List<PipeTest> list);

    /**
     * 批量添加
     * @param entities
     */
    void insertList(List<PipeTest> entities);

}
