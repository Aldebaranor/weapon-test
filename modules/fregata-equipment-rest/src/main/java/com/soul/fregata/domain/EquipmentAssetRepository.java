package com.soul.fregata.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.flagwind.persistent.model.SingleClause;
import com.soul.fregata.entity.EquipmentAsset;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * created by 迷途小码农
 */
@CacheConfig(cacheNames = EquipmentAsset.NAME)
public interface EquipmentAssetRepository extends AbstractRepositoryBase<EquipmentAsset, String> {

    /**
     * 设备id查询
     * @param equipmentId
     * @return
     */
    default List<EquipmentAsset> equipmentId(String equipmentId) {
        return many("equipmentId", equipmentId);
    }

    /**
     * 设备id删除
     * @param equipmentId
     * @return
     */
    default int deleteByEquipmentId(String equipmentId) {
        return delete(SingleClause.equal("equipmentId", equipmentId));
    }
}
