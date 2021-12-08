package com.soul.weapon.domain.historyInfo;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.AntiMissileShipGunTestReport;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = AntiMissileShipGunTestReport.NAME)

public interface AntiMissileShipGunTestReportRepository extends AbstractRepositoryBase<AntiMissileShipGunTestReport,String> {
}
