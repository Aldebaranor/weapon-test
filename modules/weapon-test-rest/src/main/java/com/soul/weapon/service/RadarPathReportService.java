package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.condition.RadarPathReportCondition;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import com.soul.weapon.entity.historyInfo.RadarPathReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface RadarPathReportService {


    PageResult<RadarPathReport> page(QueryModel<RadarPathReportCondition> model);

    List<RadarPathReport> list(RadarPathReportCondition condition);

    RadarPathReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<RadarPathReport> entities);

    String insert(RadarPathReport entity);

    void update(RadarPathReport entity);

    List<RadarPathReport> getNewradarPathByTaskId(String taskId);
}
