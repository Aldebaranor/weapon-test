package com.soul.fire.service;

import com.soul.fire.condition.FireWeaponCondition;
import com.soul.fire.entity.FireWeapon;

import java.util.List;

public interface FireWeaponService {


    /**
     * 根据id查询结果
     * @param id 武器id
     * @return 武器表
     */
    FireWeapon getById(String id);

    /**
     * 插入
     *
     * @param fireWeapon 武器表
     * @return 主键
     */
    String insert(FireWeapon fireWeapon);

    /**
     * 更新
     *
     * @param fireWeapon 武器表
     */
    void update(FireWeapon fireWeapon);

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
    List<FireWeapon> list(FireWeaponCondition condition);

}
