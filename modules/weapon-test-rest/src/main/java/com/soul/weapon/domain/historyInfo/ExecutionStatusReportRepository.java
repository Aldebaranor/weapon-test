package com.soul.weapon.domain.historyInfo;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.ExecutionStatusReport;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = ExecutionStatusReport.NAME)
public interface ExecutionStatusReportRepository extends AbstractRepositoryBase<ExecutionStatusReport,String> {
}
