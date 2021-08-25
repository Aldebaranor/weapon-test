package com.soul.fregata.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fregata.entity.ExperimentAttribute;
import org.springframework.cache.annotation.CacheConfig;

/**
 * created by 迷途小码农
 */
@CacheConfig(cacheNames = ExperimentAttribute.NAME)
public interface ExperimentAttributeRepository extends AbstractRepositoryBase<ExperimentAttribute, String> {
}
