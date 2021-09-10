package com.soul.weapon.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.PipeAdvice;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@CacheConfig(cacheNames = PipeAdvice.NAME)
public interface PipeAdviceRepository extends AbstractRepositoryBase<PipeAdvice, String> {
}
