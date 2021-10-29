package com.soul.fire.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fire.entity.FireThreshold;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = FireThreshold.NAME)
public interface FireThresholdRepository extends AbstractRepositoryBase<FireThreshold, String> {
}
