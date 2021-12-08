package com.soul.weapon.service;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.condition.InstructionAccuracyReportCondition;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import com.soul.weapon.entity.historyInfo.InstructionAccuracyReport;

import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
public interface InstructionAccuracyReportService {

    PageResult<InstructionAccuracyReport> page(QueryModel<InstructionAccuracyReportCondition> model);

    List<InstructionAccuracyReport> list(InstructionAccuracyReportCondition condition);

    InstructionAccuracyReport getById(String id);

    int deleteByIds(List<String> ids);

    int deleteById(String id);

    void insertList(List<InstructionAccuracyReport> entities);

    String insert(InstructionAccuracyReport entity);

    void update(InstructionAccuracyReport entity);
}
