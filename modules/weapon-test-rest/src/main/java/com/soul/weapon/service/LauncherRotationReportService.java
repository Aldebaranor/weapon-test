package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.condition.LauncherRotationReportCondition;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import com.soul.weapon.entity.historyInfo.LauncherRotationReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface LauncherRotationReportService {

    PageResult<LauncherRotationReport> page(QueryModel<LauncherRotationReportCondition> model);

    List<LauncherRotationReport> list(LauncherRotationReportCondition condition);

    LauncherRotationReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<LauncherRotationReport> entities);

    String insert(LauncherRotationReport entity);

    void update(LauncherRotationReport entity);

    List<LauncherRotationReport> getNewlauRotByTaskId(String taskId);
}
