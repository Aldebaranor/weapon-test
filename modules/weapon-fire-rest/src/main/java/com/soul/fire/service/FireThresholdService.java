package com.soul.fire.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.fire.condition.FireThresholdCondition;
import com.soul.fire.condition.FireWeaponCondition;
import com.soul.fire.entity.FireThreshold;
import com.soul.fire.entity.FireWeapon;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface FireThresholdService {

    /**
     * 根据id查询结果
     *
     * @return 阈值表
     */
    FireThreshold getById(String id);

    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    PageResult<FireThreshold> page(QueryModel<FireThresholdCondition> model);

    /**
     * 插入
     *
     * @param fireThreshold 阈值表
     * @return 主键
     */
    String insert(FireThreshold fireThreshold);

    /**
     * 更新
     *
     * @param fireThreshold 阈值表
     */
    void update(FireThreshold fireThreshold);

    /**
     * 删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    int deleteById(String id);

    /**
     * 列表查询
     *
     * @param condition SourceCondition
     * @return 列表数据
     */
    List<FireThreshold> list(FireThresholdCondition condition);


}
