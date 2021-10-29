package com.soul.fire.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fire.entity.FireConflictPriority;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = FireConflictPriority.NAME)
public interface FireConflictPriorityRepository extends AbstractRepositoryBase<FireConflictPriority, String> {
}
