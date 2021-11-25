package com.soul.weapon.service;

import com.soul.weapon.entity.PipeHistory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
public interface PipeHistoryService {

    /**
     * get by id
     * @param id
     * @return
     */
    PipeHistory getById(String id);

    /**
     * get all
     * @return
     */
    List<PipeHistory> getAll();

    /**
     * insert one
     * @param pipeHistory
     * @return
     */
    String insert(@RequestBody PipeHistory pipeHistory);

    /**
     * update one item
     * @param pipeHistory
     */
    void update(@RequestBody PipeHistory pipeHistory);

    /**
     * delete by id
     * @param id
     * @return
     */
    int deleteById(@PathVariable("id") String id);

    /**
     * get by time && id
     */
    List<PipeHistory> getByTimeAndId(String time,String taskId);
}
