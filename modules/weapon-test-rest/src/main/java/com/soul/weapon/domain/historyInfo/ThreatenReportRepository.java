package com.soul.weapon.domain.historyInfo;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.ThreatenReport;
import org.springframework.cache.annotation.CacheConfig;


/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = ThreatenReport.NAME)
public interface ThreatenReportRepository extends AbstractRepositoryBase<ThreatenReport,String> {
}
