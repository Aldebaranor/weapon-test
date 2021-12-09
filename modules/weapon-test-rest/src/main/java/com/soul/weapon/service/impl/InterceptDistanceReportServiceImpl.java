package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.InterceptDistanceReportCondition;
import com.soul.weapon.domain.historyInfo.InterceptDistanceReportRepository;
import com.soul.weapon.entity.historyInfo.InterceptDistanceReport;
import com.soul.weapon.service.InterceptDistanceReportService;
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
@CacheConfig(cacheNames = InterceptDistanceReport.NAME)
public class InterceptDistanceReportServiceImpl extends TemplateService<InterceptDistanceReport,String> implements InterceptDistanceReportService {

    private final InterceptDistanceReportRepository interceptDistanceReportRepository;

    @Override
    protected AbstractRepositoryBase<InterceptDistanceReport, String> getRepository() {
        return interceptDistanceReportRepository;
    }

    @Override
    public PageResult<InterceptDistanceReport> page(QueryModel<InterceptDistanceReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<InterceptDistanceReport> list(InterceptDistanceReportCondition condition) {
        //TODO:1209
        return super.query(condition,null);
    }
}
