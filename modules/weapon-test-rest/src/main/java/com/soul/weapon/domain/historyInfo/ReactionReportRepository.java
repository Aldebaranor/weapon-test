package com.soul.weapon.domain.historyInfo;


import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.historyInfo.ReactionReport;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: dxq
 * @Date: 2021/12/8 15:35
 */
@CacheConfig(cacheNames = ReactionReport.NAME)
public interface ReactionReportRepository extends AbstractRepositoryBase<ReactionReport,String> {
}
