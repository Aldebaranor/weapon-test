package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.condition.ThreatenReportCondition;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import com.soul.weapon.entity.historyInfo.InstructionAccuracyReport;
import com.soul.weapon.entity.historyInfo.ThreatenReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface ThreatenReportService {

    PageResult<ThreatenReport> page(QueryModel<ThreatenReportCondition> model);

    List<ThreatenReport> list(ThreatenReportCondition condition);

    ThreatenReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<ThreatenReport> entities);

    String insert(ThreatenReport entity);

    void update(ThreatenReport entity);

    List<ThreatenReport> getNewthreatenTestByTaskId(String taskId);

}
