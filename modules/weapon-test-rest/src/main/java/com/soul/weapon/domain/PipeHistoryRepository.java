package com.soul.weapon.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.PipeHistory;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = PipeHistory.NAME)
public interface PipeHistoryRepository extends AbstractRepositoryBase<PipeHistory,String > {
}
