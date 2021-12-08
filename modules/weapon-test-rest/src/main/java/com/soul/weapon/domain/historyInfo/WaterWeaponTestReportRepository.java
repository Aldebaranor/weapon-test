package com.soul.weapon.domain.historyInfo;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.WaterWeaponTestReport;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = WaterWeaponTestReport.NAME)
public interface WaterWeaponTestReportRepository extends AbstractRepositoryBase<WaterWeaponTestReport,String> {
}
