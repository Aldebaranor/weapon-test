package com.soul.weapon.domain.historyInfo;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.ElectronicWeaponTestReport;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = ElectronicWeaponTestReport.NAME)
public interface ElectronicWeaponTestReportRepository extends AbstractRepositoryBase<ElectronicWeaponTestReport,String> {
}
