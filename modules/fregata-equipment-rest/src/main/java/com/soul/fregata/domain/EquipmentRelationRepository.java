package com.soul.fregata.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fregata.entity.EquipmentRelation;
import org.springframework.cache.annotation.CacheConfig;

/**
 * created by 迷途小码农
 */
@CacheConfig(cacheNames = EquipmentRelation.NAME)
public interface EquipmentRelationRepository extends AbstractRepositoryBase<EquipmentRelation, String> {
}
