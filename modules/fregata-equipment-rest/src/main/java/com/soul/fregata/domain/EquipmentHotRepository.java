package com.soul.fregata.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fregata.entity.EquipmentHot;
import org.springframework.cache.annotation.CacheConfig;

/**
 * created by 迷途小码农
 */
@CacheConfig(cacheNames = EquipmentHot.NAME)
public interface EquipmentHotRepository extends AbstractRepositoryBase<EquipmentHot, String> {
}
