package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.condition.TorpedoTestReportCondition;
import com.soul.weapon.entity.historyInfo.AntiMissileShipGunTestReport;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import com.soul.weapon.entity.historyInfo.TorpedoTestReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface TorpedoTestReportService {

    PageResult<TorpedoTestReport> page(QueryModel<TorpedoTestReportCondition> model);

    List<TorpedoTestReport> list(TorpedoTestReportCondition condition);

    TorpedoTestReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<TorpedoTestReport> entities);

    String insert(TorpedoTestReport entity);

    void update(TorpedoTestReport entity);

    TorpedoTestReport getNewTorpeByTaskId(String taskId);
}
