package com.soul.weapon.service.impl;

import com.egova.data.service.AbstractRepositoryBase;
import com.egova.data.service.TemplateService;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.soul.weapon.condition.ShipToAirMissileTestReportCondition;
import com.soul.weapon.domain.historyInfo.ShipToAirMissileTestReportRepository;
import com.soul.weapon.entity.historyInfo.ShipToAirMissileTestReport;
import com.soul.weapon.service.ShipToAirMissileTestReportService;
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
@CacheConfig(cacheNames = ShipToAirMissileTestReport.NAME)
public class ShipToAirMissileTestReportServiceImpl extends TemplateService<ShipToAirMissileTestReport,String> implements ShipToAirMissileTestReportService {

    private final ShipToAirMissileTestReportRepository shipToAirMissileTestReportRepository;

    @Override
    protected AbstractRepositoryBase<ShipToAirMissileTestReport, String> getRepository() {
        return shipToAirMissileTestReportRepository;
    }

    @Override
    public PageResult<ShipToAirMissileTestReport> page(QueryModel<ShipToAirMissileTestReportCondition> model) {
       return super.page(model.getCondition(),model.getPaging(),model.getSorts());
    }

    @Override
    public List<ShipToAirMissileTestReport> list(ShipToAirMissileTestReportCondition condition) {
        //TODO:
        return super.query(condition,null);
    }
}
