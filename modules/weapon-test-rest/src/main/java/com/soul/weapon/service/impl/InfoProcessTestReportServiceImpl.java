package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Sorting;
import com.soul.weapon.condition.FireControlReportCondition;
import com.soul.weapon.condition.InfoProcessTestReportCondition;
import com.soul.weapon.domain.historyInfo.FireControlReportRepository;
import com.soul.weapon.domain.historyInfo.InfoProcessTestReportRepository;
import com.soul.weapon.entity.historyInfo.FireControlReport;
import com.soul.weapon.entity.historyInfo.InfoProcessTestReport;
import com.soul.weapon.service.FireControlReportService;
import com.soul.weapon.service.InfoProcessTestReportService;
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
@CacheConfig(cacheNames = InfoProcessTestReport.NAME)
public class InfoProcessTestReportServiceImpl extends TemplateService<InfoProcessTestReport,String> implements InfoProcessTestReportService {

    private final InfoProcessTestReportRepository infoProcessTestReportRepository;

    @Override
    protected AbstractRepositoryBase<InfoProcessTestReport, String> getRepository() {
        return infoProcessTestReportRepository;
    }

    @Override
    public PageResult<InfoProcessTestReport> page(QueryModel<InfoProcessTestReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<InfoProcessTestReport> list(InfoProcessTestReportCondition condition) {

        return super.query(condition,new Sorting[]{Sorting.descending("createTime")});
    }
}
