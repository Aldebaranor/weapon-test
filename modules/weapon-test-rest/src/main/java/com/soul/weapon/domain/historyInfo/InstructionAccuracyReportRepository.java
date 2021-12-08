package com.soul.weapon.domain.historyInfo;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.InstructionAccuracyReport;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = InstructionAccuracyReport.NAME)
public interface InstructionAccuracyReportRepository extends AbstractRepositoryBase<InstructionAccuracyReport,String> {
}
