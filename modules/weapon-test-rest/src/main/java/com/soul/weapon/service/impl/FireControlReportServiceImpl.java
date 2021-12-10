package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Sorting;
import com.soul.weapon.condition.ExecutionStatusReportCondition;
import com.soul.weapon.condition.FireControlReportCondition;
import com.soul.weapon.condition.InfoProcessTestReportCondition;
import com.soul.weapon.domain.historyInfo.ExecutionStatusReportRepository;
import com.soul.weapon.domain.historyInfo.FireControlReportRepository;
import com.soul.weapon.domain.historyInfo.InfoProcessTestReportRepository;
import com.soul.weapon.entity.historyInfo.ExecutionStatusReport;
import com.soul.weapon.entity.historyInfo.FireControlReport;
import com.soul.weapon.entity.historyInfo.InfoProcessTestReport;
import com.soul.weapon.service.ExecutionStatusReportService;
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
@CacheConfig(cacheNames = FireControlReport.NAME)
public class FireControlReportServiceImpl extends TemplateService<FireControlReport,String> implements FireControlReportService {

    private final FireControlReportRepository fireControlReportRepository;

    @Override
    protected AbstractRepositoryBase<FireControlReport, String> getRepository() {
        return fireControlReportRepository;
    }

    @Override
    public PageResult<FireControlReport> page(QueryModel<FireControlReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<FireControlReport> list(FireControlReportCondition condition) {

        return super.query(condition,new Sorting[]{Sorting.descending("createTime")});
    }
}
