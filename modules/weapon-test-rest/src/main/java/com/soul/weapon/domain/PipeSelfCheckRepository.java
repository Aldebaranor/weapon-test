package com.soul.weapon.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.PipeSelfCheck;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@CacheConfig(cacheNames = PipeSelfCheck.NAME)
public interface PipeSelfCheckRepository extends AbstractRepositoryBase<PipeSelfCheck, String> {
}
