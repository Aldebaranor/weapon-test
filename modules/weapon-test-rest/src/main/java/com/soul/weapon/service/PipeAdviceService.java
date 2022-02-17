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

    String insert(PipeAdvice pipeAdvice);

    /**
     * update a pip advice
     * @param pipeAdvice
     */
    void update( PipeAdvice pipeAdvice);

    /**
     * delete by id
     * @param id
     * @return
     */
    int deleteById(String id);

    PipeAdvice getByCode(String code);
}
