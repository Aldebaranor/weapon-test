package com.soul.fire.service;

import com.soul.fire.condition.FirePriorityCondition;
import com.soul.fire.entity.FirePriority;
import com.soul.fire.entity.FireTask;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface FirePriorityService{

    /**
     * 获取对应类型的优先级规则
     * @param conflictType
     * @return 对应类型的所有的优先级规则
     */
    List<FirePriority> getPriorityByType(String conflictType);

    FirePriority getPriorityByIds(String idA,String idB);

    /**
     * 根据modPriority中的a,b和优先级，更新优先级
     * @param modPriority
     */
    void updatePriorityByPair(FirePriority modPriority);

    /**
     * 获取所有con条件下的优先级规则
     * @param con
     * @return
     */
    List<FirePriority> list(FirePriorityCondition con);

    /**
     * 按照p更新p
     * @param p
     */
    void update(FirePriority p);
}
