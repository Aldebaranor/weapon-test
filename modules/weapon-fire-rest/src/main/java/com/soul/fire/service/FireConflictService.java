package com.soul.fire.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Paging;
import com.soul.fire.condition.FireConflictCondition;
import com.soul.fire.entity.FireConflict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface FireConflictService {

    /**
     * 根据id查询结果
     *
     * @return 武器表
     */
    FireConflict getById(String id);


    List<FireConflict> list(FireConflictCondition condition);


    PageResult<FireConflict> page(QueryModel<FireConflictCondition> model);
    /**
     * 保存
     *
     * @param fireConflict 冲突表
     * @return 主键
     */
    String insert(@RequestBody FireConflict fireConflict);

    /**
     * 更新
     *
     * @param fireConflict 冲突表
     */
    void update(@RequestBody FireConflict fireConflict);

    /**
     * 删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    int deleteById(@PathVariable("id") String id);

    /**
     * 根据冲突类型type和任务id查询
     *
     * @return 冲突表
     */
    List<FireConflict> getByTypeAndTask(String type,String taskId);

}
