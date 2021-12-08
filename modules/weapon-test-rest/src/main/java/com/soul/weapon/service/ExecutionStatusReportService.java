package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ExecutionStatusReportCondition;
import com.soul.weapon.entity.historyInfo.ExecutionStatusReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface ExecutionStatusReportService {


    PageResult<ExecutionStatusReport> page(QueryModel<ExecutionStatusReportCondition> model);

    List<ExecutionStatusReport> list(ExecutionStatusReportCondition condition);

    ExecutionStatusReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<ExecutionStatusReport> entities);

    String insert(ExecutionStatusReport entity);

    void update(ExecutionStatusReport entity);
}
