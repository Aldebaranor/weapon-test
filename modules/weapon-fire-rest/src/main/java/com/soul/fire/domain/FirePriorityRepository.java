package com.soul.fire.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fire.entity.FirePriority;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = FirePriority.NAME)
public interface FirePriorityRepository extends AbstractRepositoryBase<FirePriority, String> {
}
