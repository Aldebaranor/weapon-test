package com.soul.fregata.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fregata.entity.ExperimentEnvironment;
import org.springframework.cache.annotation.CacheConfig;

/**
 * created by 迷途小码农
 */
@CacheConfig(cacheNames = ExperimentEnvironment.NAME)
public interface ExperimentEnvironmentRepository extends AbstractRepositoryBase<ExperimentEnvironment, String> {
}
