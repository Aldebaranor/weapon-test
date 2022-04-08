package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.condition.FireControlReportCondition;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import com.soul.weapon.entity.historyInfo.FireControlReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface FireControlReportService {

    PageResult<FireControlReport> page(QueryModel<FireControlReportCondition> model);

    List<FireControlReport> list(FireControlReportCondition condition);

    FireControlReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<FireControlReport> entities);

    String insert(FireControlReport entity);

    void update(FireControlReport entity);

    List<FireControlReport> getNewfireControlByTaskId(String taskId);
}
