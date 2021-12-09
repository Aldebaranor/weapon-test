package com.soul.weapon.domain;

import com.egova.data.service.AbstractRepositoryBase;
import com.soul.weapon.entity.PipeTask;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @Author: nash5
 * @Date: 2021/9/10 15:35
 */
@CacheConfig(cacheNames = PipeTask.NAME)
public interface PipeTaskRepository extends AbstractRepositoryBase<PipeTask, String> {

}
