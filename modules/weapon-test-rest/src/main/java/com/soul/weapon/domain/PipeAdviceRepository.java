package com.soul.weapon.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.PipeAdvice;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = PipeAdvice.NAME)
public interface PipeAdviceRepository extends AbstractRepositoryBase<PipeAdvice, String> {
}
