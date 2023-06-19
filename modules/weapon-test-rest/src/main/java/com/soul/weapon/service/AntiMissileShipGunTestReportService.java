package com.soul.weapon.service;


import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.AntiMissileShipGunTestReportCondition;
import com.soul.weapon.condition.PipeTaskCondition;
import com.soul.weapon.entity.PipeTask;
import com.soul.weapon.entity.historyInfo.AntiMissileShipGunTestReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface AntiMissileShipGunTestReportService {

    PageResult<AntiMissileShipGunTestReport> page(QueryModel<AntiMissileShipGunTestReportCondition> model);

    List<AntiMissileShipGunTestReport> list(AntiMissileShipGunTestReportCondition condition);

    AntiMissileShipGunTestReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<AntiMissileShipGunTestReport> entities);

    String insert(AntiMissileShipGunTestReport entity);

    void update(AntiMissileShipGunTestReport entity);

    AntiMissileShipGunTestReport getNewAntiByTaskId(String taskId);
}
