package com.soul.weapon.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.ComDictionaryItem;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: dxq
 * @Date: 2021/12/2 15:35
 */
@CacheConfig(cacheNames = ComDictionaryItem.NAME)
public interface ComDictionaryItemRepository extends AbstractRepositoryBase<ComDictionaryItem, String> {
}