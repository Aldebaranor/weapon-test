package com.soul.weapon.service;

import com.egova.entity.DictionaryItem;
import com.soul.weapon.condition.PipeTestCondition;
import com.soul.weapon.entity.PipeTest;

import java.util.List;
import java.util.Map;

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
     * 查询
     * @param pipeTestCondition
     * @return
     */
    List<PipeTest> list(PipeTestCondition pipeTestCondition);

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

    /**
     * get pipe test tree for show
     * @return pipe test tree
     */
    List<DictionaryItem> getPipeTestTree();

    List<DictionaryItem> getCurrentTaskTestItems(String taskId);


    List<PipeTest> getByTaskId(String takeId);
}
