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
    String insert(@RequestBody PipeTest pipeTest);

    /**
     * update a test
     * @param pipeTest
     */
    void update(@RequestBody PipeTest pipeTest);

    /**
     * del by id
     * @param id
     * @return
     */
    int deleteById(@PathVariable("id") String id);

}
