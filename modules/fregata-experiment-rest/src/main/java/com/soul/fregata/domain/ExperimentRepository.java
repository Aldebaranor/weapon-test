package com.soul.fregata.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fregata.entity.Experiment;
import org.springframework.cache.annotation.CacheConfig;

/**
 * created by 迷途小码农
 */
@CacheConfig(cacheNames = Experiment.NAME)
public interface ExperimentRepository extends AbstractRepositoryBase<Experiment, String> {
}
