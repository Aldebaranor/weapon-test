package com.soul.weapon.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.PipeTest;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = PipeTest.NAME)
public interface PipeTestRepository extends AbstractRepositoryBase<PipeTest,String > {
}
