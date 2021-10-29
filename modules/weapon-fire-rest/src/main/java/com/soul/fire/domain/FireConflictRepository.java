package com.soul.fire.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fire.entity.FireConflict;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = FireConflict.NAME)
public interface FireConflictRepository extends AbstractRepositoryBase<FireConflict, String> {

}
