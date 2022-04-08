package com.soul.weapon.service;


import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.condition.ReactionReportCondition;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import com.soul.weapon.entity.historyInfo.ReactionReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface ReactionReportService {

    PageResult<ReactionReport> page(QueryModel<ReactionReportCondition> model);

    List<ReactionReport> list(ReactionReportCondition condition);

    ReactionReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<ReactionReport> entities);

    String insert(ReactionReport entity);

    void update(ReactionReport entity);

    List<ReactionReport> getNewreactionByTaskId(String taskId);
}
