package com.soul.weapon.service;

import com.soul.weapon.entity.PipeSelfCheck;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
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
    String insert( PipeSelfCheck pipeSelfCheck);

    /**
     * update one item
     * @param pipeSelfCheck
     */
    void update(PipeSelfCheck pipeSelfCheck);

    /**
     * del by id
     * @param id
     * @return
     */
    int deleteById(String id);

    /**
     * equipment status evaluation air/water
     * @return [
     *     ["搜索雷达", ...],
     *     ["true", ...]
     * ]
     */
    ArrayList<ArrayList<String>> getPipeShow() ;


}
