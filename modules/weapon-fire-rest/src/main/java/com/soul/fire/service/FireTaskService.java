package com.soul.fire.service;

import com.soul.fire.condition.FireTaskCondition;
import com.soul.fire.entity.FireTask;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface FireTaskService {


    /**
     * 获取兼容预判任务
     * @param name 任务名称
     * @return
     */
    FireTask getByName(String name);

    /**
     * 兼容预判任务的更新
     * @param running 新的running
     * @return
     */
    void update(@RequestBody FireTask task);

    /**
     * 插入兼容预判任务
     * @param task
     */
    String insert(@RequestBody FireTask task);


    /**
     * 获取当前最新的正在运行的task，若没有正在运行的，则返回null
     * @return
     */
    FireTask getCurTask();

    /**
     * 获取所有的可用的任务
     * @return
     */
    List<FireTask> list(FireTaskCondition con);

}
