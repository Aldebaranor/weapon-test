package com.soul.fire.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.fire.entity.FireWeapon;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = FireWeapon.NAME)
public interface FireWeaponRepository extends AbstractRepositoryBase<FireWeapon, String> {
}
