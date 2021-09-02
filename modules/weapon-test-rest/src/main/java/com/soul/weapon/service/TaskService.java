package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.entity.Task;

import java.util.List;

/**
 * create by nash5
 */
public interface TaskService {

    /**
     * todo: using swagger to generate the interface doc
     * @param id
     * @return
     */
    Task getById(String id);


}
