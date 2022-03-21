package com.soul.weapon.domain.historyInfo;


import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.config.Constant;
import com.soul.weapon.entity.historyInfo.ShipToAirMissileTestReport;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = ShipToAirMissileTestReport.NAME)
public interface ShipToAirMissileTestReportRepository extends AbstractRepositoryBase<ShipToAirMissileTestReport,String> {
}
