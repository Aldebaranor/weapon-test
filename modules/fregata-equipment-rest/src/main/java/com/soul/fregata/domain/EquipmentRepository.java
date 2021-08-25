package com.soul.fregata.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fregata.entity.Equipment;
import org.springframework.cache.annotation.CacheConfig;

/**
 * created by 迷途小码农
 */
@CacheConfig(cacheNames = Equipment.NAME)
public interface EquipmentRepository extends AbstractRepositoryBase<Equipment, String> {
}
