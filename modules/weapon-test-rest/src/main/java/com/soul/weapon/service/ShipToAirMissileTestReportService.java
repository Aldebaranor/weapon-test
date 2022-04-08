package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.condition.ShipToAirMissileTestReportCondition;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import com.soul.weapon.entity.historyInfo.ShipToAirMissileTestReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface ShipToAirMissileTestReportService {

    PageResult<ShipToAirMissileTestReport> page(QueryModel<ShipToAirMissileTestReportCondition> model);

    List<ShipToAirMissileTestReport> list(ShipToAirMissileTestReportCondition condition);

    ShipToAirMissileTestReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<ShipToAirMissileTestReport> entities);

    String insert(ShipToAirMissileTestReport entity);

    void update(ShipToAirMissileTestReport entity);

    ShipToAirMissileTestReport getNewShipByTaskId(String taskId);
}
