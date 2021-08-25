package com.soul.fregata.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fregata.entity.EquipmentType;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * created by 迷途小码农
 */
@CacheConfig(cacheNames = EquipmentType.NAME)
public interface EquipmentTypeRepository extends AbstractRepositoryBase<EquipmentType, String> {

    @Cacheable
    default List<EquipmentType> children(String id) {
        return this.many("ParentId", id);
    }
}
