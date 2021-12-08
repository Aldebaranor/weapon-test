package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.condition.InterceptDistanceReportCondition;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import com.soul.weapon.entity.historyInfo.InterceptDistanceReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface InterceptDistanceReportService {


    PageResult<InterceptDistanceReport> page(QueryModel<InterceptDistanceReportCondition> model);

    List<InterceptDistanceReport> list(InterceptDistanceReportCondition condition);

    InterceptDistanceReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<InterceptDistanceReport> entities);

    String insert(InterceptDistanceReport entity);

    void update(InterceptDistanceReport entity);
}
