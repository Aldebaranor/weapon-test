package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Sorting;
import com.soul.weapon.condition.RadarPathReportCondition;
import com.soul.weapon.domain.historyInfo.RadarPathReportRepository;
import com.soul.weapon.entity.historyInfo.RadarPathReport;
import com.soul.weapon.service.RadarPathReportService;
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
@CacheConfig(cacheNames = RadarPathReport.NAME)
public class RadarPathReportServiceImpl extends TemplateService<RadarPathReport,String> implements RadarPathReportService {

    private final RadarPathReportRepository radarPathReportRepository;

    @Override
    protected AbstractRepositoryBase<RadarPathReport, String> getRepository() {
        return radarPathReportRepository;
    }

    @Override
    public PageResult<RadarPathReport> page(QueryModel<RadarPathReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<RadarPathReport> list(RadarPathReportCondition condition) {

        return super.query(condition,new Sorting[]{Sorting.descending("createTime")});
    }
}
