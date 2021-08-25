package com.soul.fregata.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fregata.entity.ExperimentArmy;
import org.springframework.cache.annotation.CacheConfig;

/**
 * created by 迷途小码农
 */
@CacheConfig(cacheNames = ExperimentArmy.NAME)
public interface ExperimentArmyRepository extends AbstractRepositoryBase<ExperimentArmy, String> {
}
