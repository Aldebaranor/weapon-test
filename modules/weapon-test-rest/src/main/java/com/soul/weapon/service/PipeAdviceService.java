package com.soul.weapon.service;

import com.soul.weapon.entity.PipeAdvice;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
public interface PipeAdviceService {
    /**
     * get by id
     * @param id
     * @return
     */
    PipeAdvice getById(String id);

    /**
     * get all pipe advice
     * @return
     */
    List<PipeAdvice> getAll();

    /**
     * insert a pipe advice
     * @param pipeAdvice
     * @return
     */
    //TODO:1209
    String insert(@RequestBody PipeAdvice pipeAdvice);

    /**
     * update a pip advice
     * @param pipeAdvice
     */
    //TODO:1209
    void update(@RequestBody PipeAdvice pipeAdvice);

    /**
     * delete by id
     * @param id
     * @return
     */
    //TODO:1209
    int deleteById(@PathVariable("id") String id);
    
}
