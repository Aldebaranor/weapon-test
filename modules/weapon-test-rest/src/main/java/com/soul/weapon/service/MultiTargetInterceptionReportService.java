package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.MultiTargetInterceptionReportCondition;
import com.soul.weapon.entity.historyInfo.MultiTargetInterceptionReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface MultiTargetInterceptionReportService {

    PageResult<MultiTargetInterceptionReport> page(QueryModel<MultiTargetInterceptionReportCondition> model);

    List<MultiTargetInterceptionReport> list(MultiTargetInterceptionReportCondition condition);

    MultiTargetInterceptionReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<MultiTargetInterceptionReport> entities);

    String insert(MultiTargetInterceptionReport entity);

    void update(MultiTargetInterceptionReport entity);



}
