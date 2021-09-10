package com.soul.weapon.service;

import com.soul.weapon.entity.PipeSelfCheck;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
public interface PipeSelfCheckService {

    /**
     * get by id
     * @param id
     * @return
     */
    PipeSelfCheck getById(String id);

    /**
     * get by name
     * @param name
     * @return
     */
    PipeSelfCheck getByName(String name);

    /**
     * get all
     * @return
     */
    List<PipeSelfCheck> getAll();

    /**
     * insert one
     * @param pipeSelfCheck
     * @return
     */
    String insert(@RequestBody PipeSelfCheck pipeSelfCheck);

    /**
     * update one item
     * @param pipeSelfCheck
     */
    void update(@RequestBody PipeSelfCheck pipeSelfCheck);

    /**
     * del by id
     * @param id
     * @return
     */
    int deleteById(@PathVariable("id") String id);
}
