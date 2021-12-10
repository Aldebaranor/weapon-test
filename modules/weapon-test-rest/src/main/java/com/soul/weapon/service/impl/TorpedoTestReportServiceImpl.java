package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Sorting;
import com.soul.weapon.condition.TorpedoTestReportCondition;
import com.soul.weapon.domain.historyInfo.TorpedoTestReportRepository;
import com.soul.weapon.entity.historyInfo.TorpedoTestReport;
import com.soul.weapon.service.TorpedoTestReportService;
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
@CacheConfig(cacheNames = TorpedoTestReport.NAME)
public class TorpedoTestReportServiceImpl extends TemplateService<TorpedoTestReport,String> implements TorpedoTestReportService {

    private final TorpedoTestReportRepository torpedoTestReportRepository;

    @Override
    protected AbstractRepositoryBase<TorpedoTestReport, String> getRepository() {
        return torpedoTestReportRepository;
    }

    @Override
    public PageResult<TorpedoTestReport> page(QueryModel<TorpedoTestReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<TorpedoTestReport> list(TorpedoTestReportCondition condition) {

        return super.query(condition,new Sorting[]{Sorting.descending("createTime")});
    }
}
