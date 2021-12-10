package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Sorting;
import com.soul.weapon.condition.ThreatenReportCondition;
import com.soul.weapon.domain.historyInfo.ThreatenReportRepository;
import com.soul.weapon.entity.historyInfo.ThreatenReport;
import com.soul.weapon.service.ThreatenReportService;
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
@CacheConfig(cacheNames = ThreatenReport.NAME)
public class ThreatenReportServiceImpl extends TemplateService<ThreatenReport,String> implements ThreatenReportService {

    private final ThreatenReportRepository threatenReportRepository;

    @Override
    protected AbstractRepositoryBase<ThreatenReport, String> getRepository() {
        return threatenReportRepository;
    }

    @Override
    public PageResult<ThreatenReport> page(QueryModel<ThreatenReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<ThreatenReport> list(ThreatenReportCondition condition) {

        return super.query(condition,new Sorting[]{Sorting.descending("createTime")});
    }
}
