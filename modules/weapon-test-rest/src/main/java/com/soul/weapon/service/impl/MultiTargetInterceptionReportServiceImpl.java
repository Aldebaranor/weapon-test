package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Sorting;
import com.soul.weapon.condition.MultiTargetInterceptionReportCondition;
import com.soul.weapon.domain.historyInfo.MultiTargetInterceptionReportRepository;
import com.soul.weapon.entity.historyInfo.MultiTargetInterceptionReport;
import com.soul.weapon.service.MultiTargetInterceptionReportService;
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
@CacheConfig(cacheNames = MultiTargetInterceptionReport.NAME)
public class MultiTargetInterceptionReportServiceImpl extends TemplateService<MultiTargetInterceptionReport,String> implements MultiTargetInterceptionReportService {

    private final MultiTargetInterceptionReportRepository multiTargetInterceptionReportRepository;

    @Override
    protected AbstractRepositoryBase<MultiTargetInterceptionReport, String> getRepository() {
        return multiTargetInterceptionReportRepository;
    }

    @Override
    public PageResult<MultiTargetInterceptionReport> page(QueryModel<MultiTargetInterceptionReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<MultiTargetInterceptionReport> list(MultiTargetInterceptionReportCondition condition) {

        return super.query(condition,new Sorting[]{Sorting.descending("createTime")});
    }
}
