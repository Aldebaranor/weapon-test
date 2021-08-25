package com.soul.fregata.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fregata.entity.ExperimentParameter;
import org.springframework.cache.annotation.CacheConfig;

/**
 * created by yangL
 */
@CacheConfig(cacheNames = ExperimentParameter.NAME)
public interface ExperimentParameterRepository extends AbstractRepositoryBase<ExperimentParameter, String> {
}
