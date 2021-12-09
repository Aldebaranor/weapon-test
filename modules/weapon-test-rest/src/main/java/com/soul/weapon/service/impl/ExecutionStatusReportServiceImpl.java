package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.AntiMissileShipGunTestReportCondition;
import com.soul.weapon.condition.ExecutionStatusReportCondition;
import com.soul.weapon.domain.historyInfo.AntiMissileShipGunTestReportRepository;
import com.soul.weapon.domain.historyInfo.ExecutionStatusReportRepository;
import com.soul.weapon.entity.historyInfo.AntiMissileShipGunTestReport;
import com.soul.weapon.entity.historyInfo.ExecutionStatusReport;
import com.soul.weapon.service.AntiMissileShipGunTestReportService;
import com.soul.weapon.service.ExecutionStatusReportService;
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
@CacheConfig(cacheNames = ExecutionStatusReport.NAME)
public class ExecutionStatusReportServiceImpl extends TemplateService<ExecutionStatusReport,String> implements ExecutionStatusReportService {

    private final ExecutionStatusReportRepository executionStatusReportRepository;

    @Override
    protected AbstractRepositoryBase<ExecutionStatusReport, String> getRepository() {
        return executionStatusReportRepository;
    }

    @Override
    public PageResult<ExecutionStatusReport> page(QueryModel<ExecutionStatusReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<ExecutionStatusReport> list(ExecutionStatusReportCondition condition) {
        //TODO:1209
        return super.query(condition,null);
    }
}
