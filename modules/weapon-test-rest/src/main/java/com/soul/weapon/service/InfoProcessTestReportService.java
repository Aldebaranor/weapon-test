package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.condition.InfoProcessTestReportCondition;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import com.soul.weapon.entity.historyInfo.InfoProcessTestReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface InfoProcessTestReportService {

    PageResult<InfoProcessTestReport> page(QueryModel<InfoProcessTestReportCondition> model);

    List<InfoProcessTestReport> list(InfoProcessTestReportCondition condition);

    InfoProcessTestReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<InfoProcessTestReport> entities);

    String insert(InfoProcessTestReport entity);

    void update(InfoProcessTestReport entity);
}
