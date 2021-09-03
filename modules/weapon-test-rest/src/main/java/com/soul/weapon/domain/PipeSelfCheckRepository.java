package com.soul.weapon.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.PipeSelfCheck;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = PipeSelfCheck.NAME)
public interface PipeSelfCheckRepository extends AbstractRepositoryBase<PipeSelfCheck, String> {
}
