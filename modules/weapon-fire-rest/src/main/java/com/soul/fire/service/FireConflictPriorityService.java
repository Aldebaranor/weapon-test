package com.soul.fire.service;

import com.soul.fire.entity.FireConflictPriority;

import java.util.List;

public interface FireConflictPriorityService {

    /**
     * 获取所有的冲突类型优先级规则
     * @return 
     */
    List<FireConflictPriority> getAll();

    /**
     * 按照更新所有的冲突类型优先级
     * @param list
     */
    void updateList(List<FireConflictPriority> list);
}
