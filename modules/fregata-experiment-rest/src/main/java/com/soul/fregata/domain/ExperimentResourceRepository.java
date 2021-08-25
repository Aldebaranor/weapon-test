package com.soul.fregata.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fregata.entity.ExperimentResource;
import org.springframework.cache.annotation.CacheConfig;

/**
 * created by 迷途小码农
 */
@CacheConfig(cacheNames = ExperimentResource.NAME)
public interface ExperimentResourceRepository extends AbstractRepositoryBase<ExperimentResource, String> {
}
