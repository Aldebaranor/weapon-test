package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.condition.WaterWeaponTestReportCondition;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import com.soul.weapon.entity.historyInfo.WaterWeaponTestReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface WaterWeaponTestReportService {

    PageResult<WaterWeaponTestReport> page(QueryModel<WaterWeaponTestReportCondition> model);

    List<WaterWeaponTestReport> list(WaterWeaponTestReportCondition condition);

    WaterWeaponTestReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<WaterWeaponTestReport> entities);

    String insert(WaterWeaponTestReport entity);

    void update(WaterWeaponTestReport entity);
}
