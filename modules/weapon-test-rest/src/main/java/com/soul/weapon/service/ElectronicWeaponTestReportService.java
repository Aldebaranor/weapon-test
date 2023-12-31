package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface ElectronicWeaponTestReportService {

    PageResult<ElectronicWeaponTestReport> page(QueryModel<ElectronicWeaponTestReportCondition> model);

    List<ElectronicWeaponTestReport> list(ElectronicWeaponTestReportCondition condition);

    ElectronicWeaponTestReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<ElectronicWeaponTestReport> entities);

    String insert(ElectronicWeaponTestReport entity);

    void update(ElectronicWeaponTestReport entity);

    ElectronicWeaponTestReport getNewelectWeaponByTaskId(String taskId);
}
