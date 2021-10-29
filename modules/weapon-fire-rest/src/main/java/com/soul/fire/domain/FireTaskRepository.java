package com.soul.fire.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fire.entity.FireTask;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = FireTask.NAME)
public interface FireTaskRepository extends AbstractRepositoryBase<FireTask, String> {
}
