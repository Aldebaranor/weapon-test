package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Sorting;
import com.soul.weapon.condition.WaterWeaponTestReportCondition;
import com.soul.weapon.domain.historyInfo.WaterWeaponTestReportRepository;
import com.soul.weapon.entity.historyInfo.WaterWeaponTestReport;
import com.soul.weapon.service.WaterWeaponTestReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.util.List;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Priority(5)
@CacheConfig(cacheNames = WaterWeaponTestReport.NAME)
public class WaterWeaponTestReportServiceImpl extends TemplateService<WaterWeaponTestReport,String> implements WaterWeaponTestReportService {

    private final WaterWeaponTestReportRepository waterWeaponTestReportRepository;

    @Override
    protected AbstractRepositoryBase<WaterWeaponTestReport, String> getRepository() {
        return waterWeaponTestReportRepository;
    }

    @Override
    public PageResult<WaterWeaponTestReport> page(QueryModel<WaterWeaponTestReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<WaterWeaponTestReport> list(WaterWeaponTestReportCondition condition) {
        return super.query(condition,new Sorting[]{Sorting.descending("createTime")});
    }

    @Override
    public WaterWeaponTestReport getNewwaterWeaponByTaskId(String taskId) {
        return waterWeaponTestReportRepository.getNewwaterWeaponByTaskId(taskId);
    }
}
