package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.flagwind.persistent.model.Sorting;
import com.soul.weapon.condition.AntiMissileShipGunTestReportCondition;
import com.soul.weapon.domain.historyInfo.AntiMissileShipGunTestReportRepository;
import com.soul.weapon.entity.historyInfo.AntiMissileShipGunTestReport;
import com.soul.weapon.service.AntiMissileShipGunTestReportService;
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
@CacheConfig(cacheNames = AntiMissileShipGunTestReport.NAME)
public class AntiMissileShipGunTestReportServiceImpl extends TemplateService<AntiMissileShipGunTestReport,String> implements AntiMissileShipGunTestReportService {

    private final AntiMissileShipGunTestReportRepository antiMissileShipGunTestReportRepository;

    @Override
    protected AbstractRepositoryBase<AntiMissileShipGunTestReport, String> getRepository() {
        return antiMissileShipGunTestReportRepository;
    }

    @Override
    public PageResult<AntiMissileShipGunTestReport> page(QueryModel<AntiMissileShipGunTestReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<AntiMissileShipGunTestReport> list(AntiMissileShipGunTestReportCondition condition) {
        return super.query(condition,new Sorting[]{Sorting.descending("createTime")});
    }

    @Override
    public AntiMissileShipGunTestReport getNewAntiByTaskId(String taskId) {
        return antiMissileShipGunTestReportRepository.getNewAntiByTaskId(taskId);
    }
}
