package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.LauncherRotationReportCondition;
import com.soul.weapon.domain.historyInfo.LauncherRotationReportRepository;
import com.soul.weapon.entity.historyInfo.LauncherRotationReport;
import com.soul.weapon.service.LauncherRotationReportService;
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
@CacheConfig(cacheNames = LauncherRotationReport.NAME)
public class LauncherRotationReportServiceImpl extends TemplateService<LauncherRotationReport,String> implements LauncherRotationReportService {

    private final LauncherRotationReportRepository launcherRotationReportRepository;

    @Override
    protected AbstractRepositoryBase<LauncherRotationReport, String> getRepository() {
        return launcherRotationReportRepository;
    }

    @Override
    public PageResult<LauncherRotationReport> page(QueryModel<LauncherRotationReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<LauncherRotationReport> list(LauncherRotationReportCondition condition) {
        //TODO:1209
        return super.query(condition,null);
    }
}
