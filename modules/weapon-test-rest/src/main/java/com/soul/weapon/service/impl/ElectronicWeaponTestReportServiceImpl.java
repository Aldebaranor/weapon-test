package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Sorting;
import com.soul.weapon.condition.ElectronicWeaponTestReportCondition;
import com.soul.weapon.domain.historyInfo.ElectronicWeaponTestReportRepository;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import com.soul.weapon.service.ElectronicWeaponTestReportService;
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
@CacheConfig(cacheNames = ElectronicWeaponTestReport.NAME)
public class ElectronicWeaponTestReportServiceImpl extends TemplateService<ElectronicWeaponTestReport,String> implements ElectronicWeaponTestReportService {

    private final ElectronicWeaponTestReportRepository electronicWeaponTestReportRepository;

    @Override
    protected AbstractRepositoryBase<ElectronicWeaponTestReport, String> getRepository() {
        return electronicWeaponTestReportRepository;
    }

    @Override
    public PageResult<ElectronicWeaponTestReport> page(QueryModel<ElectronicWeaponTestReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<ElectronicWeaponTestReport> list(ElectronicWeaponTestReportCondition condition) {

        return super.query(condition,new Sorting[]{Sorting.descending("createTime")});
    }

    @Override
    public ElectronicWeaponTestReport getNewelectWeaponByTaskId(String taskId) {
        return electronicWeaponTestReportRepository.getNewelectWeaponByTaskId(taskId);
    }
}
